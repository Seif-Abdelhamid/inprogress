import java.util.ArrayList;
import java.util.Random;

/**
 * A class that encodes and decodes secret messages using a permutation of the alphabet.
 * Each letter in the alphabet is mapped to a corresponding letter in a permuted alphabet.
 */
class PermutationCode {
  ArrayList<Character> alphabet;
  ArrayList<Character> code;

  /**
   * Constructor that allows you to specify explicitly what will be your encoding permutation.
   * 
   * @param code the permutation of the alphabet to use for encoding/decoding
   */
  PermutationCode(ArrayList<Character> code) {
    this.alphabet = new ArrayList<Character>();
    // Initialize alphabet with a-z
    for (char c = 'a'; c <= 'z'; c++) {
      this.alphabet.add(c);
    }
    this.code = code;
  }

  /**
   * Constructor that generates a new encoding permutation that may be given to parties
   * that wish to communicate in secret.
   * 
   * @param alphabet a proper permutation of the alphabet field
   * @param generateRandom if true, generates a random permutation; if false, uses alphabet as code
   */
  PermutationCode(ArrayList<Character> alphabet, boolean generateRandom) {
    this.alphabet = alphabet;
    if (generateRandom) {
      this.code = this.initEncoder();
    } else {
      // For testing: use alphabet as code (identity permutation)
      this.code = new ArrayList<Character>(alphabet);
    }
  }

  /**
   * Decodes the encoded message using the ArrayList code to decipher the message,
   * one character at a time.
   * 
   * @param encoded the encoded message
   * @return the decoded message
   */
  String decode(String encoded) {
    StringBuilder decoded = new StringBuilder();
    for (int i = 0; i < encoded.length(); i++) {
      char encodedChar = encoded.charAt(i);
      // Find the index of this character in the code
      int codeIndex = this.code.indexOf(encodedChar);
      if (codeIndex != -1) {
        // Map it back to the corresponding character in the alphabet
        char decodedChar = this.alphabet.get(codeIndex);
        decoded.append(decodedChar);
      } else {
        // If character not found, keep it as is (for spaces, punctuation, etc.)
        decoded.append(encodedChar);
      }
    }
    return decoded.toString();
  }

  /**
   * Encodes the message using the ArrayList code to produce the encoded message,
   * one character at a time.
   * 
   * @param message the message to encode
   * @return the encoded message
   */
  String encode(String message) {
    StringBuilder encoded = new StringBuilder();
    for (int i = 0; i < message.length(); i++) {
      char messageChar = message.charAt(i);
      // Find the index of this character in the alphabet
      int alphabetIndex = this.alphabet.indexOf(messageChar);
      if (alphabetIndex != -1) {
        // Map it to the corresponding character in the code
        char encodedChar = this.code.get(alphabetIndex);
        encoded.append(encodedChar);
      } else {
        // If character not found, keep it as is (for spaces, punctuation, etc.)
        encoded.append(messageChar);
      }
    }
    return encoded.toString();
  }

  /**
   * Produces a random permutation of the 26 letters of the alphabet
   * and returns it as an ArrayList of Characters.
   * 
   * @return a random permutation of the alphabet
   */
  ArrayList<Character> initEncoder() {
    // Make a copy of the alphabet list
    ArrayList<Character> alphabetCopy = new ArrayList<Character>(this.alphabet);
    ArrayList<Character> encoder = new ArrayList<Character>();
    Random random = new Random();

    // Remove one character at random and add it to the encoder list,
    // until all letters have been consumed
    while (!alphabetCopy.isEmpty()) {
      int randomIndex = random.nextInt(alphabetCopy.size());
      char selectedChar = alphabetCopy.remove(randomIndex);
      encoder.add(selectedChar);
    }

    return encoder;
  }
}

/**
 * Examples and tests for the PermutationCode class
 */
class ExamplesPermutationCode {
  // Example with explicit permutation: a->b, b->e, c->a, d->c, e->d
  // For a 5-letter alphabet: (a, b, c, d, e) -> (b, e, a, c, d)
  PermutationCode code1;

  // Example with full 26-letter alphabet
  PermutationCode code2;

  /**
   * Initialize examples
   */
  void initExamples() {
    // Create a 5-letter example
    ArrayList<Character> smallAlphabet = new ArrayList<Character>();
    smallAlphabet.add('a');
    smallAlphabet.add('b');
    smallAlphabet.add('c');
    smallAlphabet.add('d');
    smallAlphabet.add('e');

    ArrayList<Character> smallCode = new ArrayList<Character>();
    smallCode.add('b');
    smallCode.add('e');
    smallCode.add('a');
    smallCode.add('c');
    smallCode.add('d');

    this.code1 = new PermutationCode(smallCode);
    this.code1.alphabet = smallAlphabet; // Override to use 5-letter alphabet

    // Create a full 26-letter example
    ArrayList<Character> fullCode = new ArrayList<Character>();
    fullCode.add('z');
    fullCode.add('y');
    fullCode.add('x');
    fullCode.add('w');
    fullCode.add('v');
    fullCode.add('u');
    fullCode.add('t');
    fullCode.add('s');
    fullCode.add('r');
    fullCode.add('q');
    fullCode.add('p');
    fullCode.add('o');
    fullCode.add('n');
    fullCode.add('m');
    fullCode.add('l');
    fullCode.add('k');
    fullCode.add('j');
    fullCode.add('i');
    fullCode.add('h');
    fullCode.add('g');
    fullCode.add('f');
    fullCode.add('e');
    fullCode.add('d');
    fullCode.add('c');
    fullCode.add('b');
    fullCode.add('a');

    this.code2 = new PermutationCode(fullCode);
  }

  /**
   * Test the decode method
   */
  boolean testDecode(Tester t) {
    this.initExamples();
    // For code1: a->b, b->e, c->a, d->c, e->d
    // So "abeedc" should decode to "cabbad" (wait, let me recalculate)
    // Actually: a->b, b->e, c->a, d->c, e->d
    // To decode: find char in code, get index, use alphabet[index]
    // "abeedc": a is at code[0], so alphabet[0] = a
    //           b is at code[1], so alphabet[1] = b
    //           e is at code[4], so alphabet[4] = e
    //           e is at code[4], so alphabet[4] = e
    //           d is at code[3], so alphabet[3] = d
    //           c is at code[2], so alphabet[2] = c
    // So "abeedc" decodes to "abeedc" - that doesn't seem right
    
    // Let me recalculate: code = (b, e, a, c, d)
    // alphabet = (a, b, c, d, e)
    // To encode "a": find 'a' in alphabet at index 0, use code[0] = 'b'
    // To encode "b": find 'b' in alphabet at index 1, use code[1] = 'e'
    // So "ab" encodes to "be"
    // To decode "be": find 'b' in code at index 0, use alphabet[0] = 'a'
    //                  find 'e' in code at index 1, use alphabet[1] = 'b'
    // So "be" decodes to "ab" - correct!
    
    // According to assignment: "abeedc" should decode to "cabbad"
    // Let me check: "abeedc" -> find each in code
    // a is at code[2], so alphabet[2] = c
    // b is at code[0], so alphabet[0] = a
    // e is at code[1], so alphabet[1] = b
    // e is at code[1], so alphabet[1] = b
    // d is at code[4], so alphabet[4] = e
    // c is at code[3], so alphabet[3] = d
    // So "abeedc" decodes to "cabbed" not "cabbad"
    
    // Wait, the assignment says "abeedc" decodes to "cabbed"
    String result1 = this.code1.decode("abeedc");
    return t.checkExpect(result1, "cabbed", "Decode abeedc");
  }

  /**
   * Test the encode method
   */
  boolean testEncode(Tester t) {
    this.initExamples();
    // According to assignment: "badace" should encode to "ebcbad"
    // Let me verify: "badace"
    // b -> find in alphabet at index 1, use code[1] = e
    // a -> find in alphabet at index 0, use code[0] = b
    // d -> find in alphabet at index 3, use code[3] = c
    // a -> find in alphabet at index 0, use code[0] = b
    // c -> find in alphabet at index 2, use code[2] = a
    // e -> find in alphabet at index 4, use code[4] = d
    // So "badace" encodes to "ebcbad" - correct!
    
    String result1 = this.code1.encode("badace");
    return t.checkExpect(result1, "ebcbad", "Encode badace");
  }

  /**
   * Test that encode and decode are inverses
   */
  boolean testEncodeDecode(Tester t) {
    this.initExamples();
    String original = "hello";
    String encoded = this.code2.encode(original);
    String decoded = this.code2.decode(encoded);
    return t.checkExpect(decoded, original, "Encode and decode are inverses");
  }

  /**
   * Test initEncoder produces a valid permutation
   */
  boolean testInitEncoder(Tester t) {
    // Initialize alphabet
    ArrayList<Character> alphabet = new ArrayList<Character>();
    for (char c = 'a'; c <= 'z'; c++) {
      alphabet.add(c);
    }
    
    PermutationCode testCode = new PermutationCode(alphabet, true);
    ArrayList<Character> encoder = testCode.code;
    
    // Check that encoder has 26 characters
    boolean has26Chars = encoder.size() == 26;
    
    // Check that all characters from alphabet are present
    boolean hasAllChars = true;
    for (char c = 'a'; c <= 'z'; c++) {
      if (!encoder.contains(c)) {
        hasAllChars = false;
        break;
      }
    }
    
    // Check that no character appears twice
    boolean noDuplicates = true;
    for (int i = 0; i < encoder.size(); i++) {
      for (int j = i + 1; j < encoder.size(); j++) {
        if (encoder.get(i).equals(encoder.get(j))) {
          noDuplicates = false;
          break;
        }
      }
      if (!noDuplicates) break;
    }
    
    return t.checkExpect(has26Chars, true, "Encoder has 26 characters")
        && t.checkExpect(hasAllChars, true, "Encoder has all characters")
        && t.checkExpect(noDuplicates, true, "Encoder has no duplicates");
  }
}
