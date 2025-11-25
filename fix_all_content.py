#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from pathlib import Path
import re
import sys
sys.stdout.reconfigure(encoding='utf-8')

def fix_file(file_path):
    """Fix all location and branding references"""
    try:
        # Read file with explicit UTF-8 encoding
        with open(file_path, 'r', encoding='utf-8', errors='replace') as f:
            content = f.read()
        
        original = content
        changes = []
        
        # Handle em dash (U+2014), en dash (U+2013), and regular dash
        em_dash = '\u2014'
        en_dash = '\u2013'
        
        # Remove Nationwide Shipping (all variations)
        patterns_nationwide = [
            f'{em_dash} Nationwide Shipping',
            f'{en_dash} Nationwide Shipping',
            '- Nationwide Shipping',
            'Nationwide Shipping',
        ]
        for pattern in patterns_nationwide:
            if pattern in content:
                content = content.replace(pattern, '')
                changes.append('removed Nationwide Shipping')
        
        # Replace state references
        state_replacements = [
            ('NY, NJ, GA, & CT', 'NJ'),
            ('NY, NJ, GA & CT', 'NJ'),
            ('NY, NJ, GA, CT', 'NJ'),
            ('NY, NJ, GA', 'NJ'),
            ('in NY, NJ, GA, & CT', 'in NJ'),
            ('in NY, NJ, GA & CT', 'in NJ'),
            ('in NY, NJ, GA, CT', 'in NJ'),
            ('in NY, NJ, GA', 'in NJ'),
        ]
        for old, new in state_replacements:
            if old in content:
                content = content.replace(old, new)
                changes.append('replaced states')
        
        # Replace Middle Eastern with Halal
        middle_eastern_replacements = [
            ('Middle Eastern Restaurant', 'Halal Restaurant'),
            ('Middle Eastern Cuisine', 'Halal Cuisine'),
            ('Middle Eastern Food', 'Halal Food'),
            ('Middle,Eastern,Restaurant', 'Halal,Restaurant'),
            ('Middle,Eastern,Food', 'Halal,Food'),
            ('Middle Eastern & Mediterranean', 'Halal'),
            ('authentic Middle Eastern', 'authentic Halal'),
            ('Middle Eastern', 'Halal'),
        ]
        for old, new in middle_eastern_replacements:
            if old in content:
                content = content.replace(old, new)
                changes.append('replaced Middle Eastern')
        
        # Remove Nationwide Shipping links using regex
        content = re.sub(
            r'<a[^>]*aria-label=["\']Nationwide Shipping["\'][^>]*>Nationwide Shipping</a>',
            '',
            content,
            flags=re.IGNORECASE
        )
        content = re.sub(
            r'<li[^>]*>\s*<a[^>]*aria-label=["\']Nationwide Shipping["\'][^>]*>Nationwide Shipping</a>\s*</li>',
            '',
            content,
            flags=re.IGNORECASE | re.DOTALL
        )
        
        # Remove Nationwide Shipping headings
        content = re.sub(r'<h[12][^>]*>\s*Nationwide\s+Shipping\s*</h[12]>', '', content, flags=re.IGNORECASE)
        content = content.replace('Now Shipping Hot Sauce Nationwide', 'Hot Sauce Available')
        content = content.replace('Shipping Hot Sauce Nationwide', 'Hot Sauce Available')
        
        # Remove non-NJ locations from dropdowns
        content = re.sub(
            r'<option\s+value=["\']mamouns-[^"\']*-(?:ct|ny|nyc|ga)["\'][^>]*>[^<]*</option>',
            '',
            content,
            flags=re.IGNORECASE
        )
        
        if content != original:
            with open(file_path, 'w', encoding='utf-8', newline='') as f:
                f.write(content)
            return True, list(set(changes))
        return False, []
    except Exception as e:
        print(f"  ERROR: {file_path}: {e}")
        import traceback
        traceback.print_exc()
        return False, []

# Main execution
script_dir = Path(__file__).parent.resolve()
print(f"Working in: {script_dir}")

html_files = [
    f for f in script_dir.rglob('*.html')
    if not any(x in str(f) for x in ['signals', 'schema.org', 'replace_', 'update_', 'fix_', 'final_'])
]

print(f"Found {len(html_files)} HTML files\n")

processed = 0
for html_file in html_files:
    # Debug: check first file
    if html_file.name == 'index.html':
        with open(html_file, 'r', encoding='utf-8') as f:
            sample = f.read(500)
            print(f"DEBUG - First 500 chars of index.html:")
            print(repr(sample))
            print(f"Has 'Nationwide': {'Nationwide' in sample}")
            print(f"Has 'NY, NJ, GA': {'NY, NJ, GA' in sample}")
            print(f"Has 'Middle Eastern': {'Middle Eastern' in sample}")
            print()
    changed, changes = fix_file(html_file)
    if changed:
        processed += 1
        rel_path = html_file.relative_to(script_dir)
        print(f"Processed: {rel_path}")
        if changes:
            print(f"  Changes: {', '.join(changes)}")

print(f"\nProcessed {processed} files with changes")

