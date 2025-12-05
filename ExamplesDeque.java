/**
 * Examples and tests for the Deque class
 */
class ExamplesDeque {
  // Empty deque
  Deque<String> deque1;

  // Deque with ("abc", "bcd", "cde", "def")
  Deque<String> deque2;

  // Deque with non-lexicographically ordered values (at least 4 values)
  Deque<String> deque3;

  // Additional examples for testing
  Deque<Integer> deque4;

  /**
   * Initialize examples
   */
  void initExamples() {
    // Empty deque
    this.deque1 = new Deque<String>();

    // Deque with ("abc", "bcd", "cde", "def")
    // Using the convenience constructor to build the list
    Sentinel<String> sentinel2 = new Sentinel<String>();
    Node<String> node1 = new Node<String>("abc", sentinel2, sentinel2);
    Node<String> node2 = new Node<String>("bcd", sentinel2, node1);
    Node<String> node3 = new Node<String>("cde", sentinel2, node2);
    @SuppressWarnings("unused")
    Node<String> node4 = new Node<String>("def", sentinel2, node3); // Completes the chain
    this.deque2 = new Deque<String>(sentinel2);

    // Deque with non-lexicographically ordered values: ("zebra", "apple", "banana", "cat")
    Sentinel<String> sentinel3 = new Sentinel<String>();
    Node<String> node5 = new Node<String>("zebra", sentinel3, sentinel3);
    Node<String> node6 = new Node<String>("apple", sentinel3, node5);
    Node<String> node7 = new Node<String>("banana", sentinel3, node6);
    @SuppressWarnings("unused")
    Node<String> node8 = new Node<String>("cat", sentinel3, node7); // Completes the chain
    this.deque3 = new Deque<String>(sentinel3);

    // Integer deque for additional testing
    this.deque4 = new Deque<Integer>();
  }

  /**
   * Test the size method
   */
  boolean testSize(Tester t) {
    this.initExamples();
    return t.checkExpect(this.deque1.size(), 0, "Empty deque size")
        && t.checkExpect(this.deque2.size(), 4, "Deque2 size")
        && t.checkExpect(this.deque3.size(), 4, "Deque3 size");
  }

  /**
   * Test addAtHead method
   */
  boolean testAddAtHead(Tester t) {
    this.initExamples();
    this.deque1.addAtHead("test");
    this.deque2.addAtHead("xyz");
    return t.checkExpect(this.deque1.size(), 1, "Add to empty deque")
        && t.checkExpect(this.deque1.header.next instanceof Node, true, "First node is Node")
        && t.checkExpect(((Node<String>) this.deque1.header.next).data, "test", "Correct data")
        && t.checkExpect(this.deque2.size(), 5, "Add to non-empty deque")
        && t.checkExpect(((Node<String>) this.deque2.header.next).data, "xyz", "New head data");
  }

  /**
   * Test addAtTail method
   */
  boolean testAddAtTail(Tester t) {
    this.initExamples();
    this.deque1.addAtTail("test");
    this.deque2.addAtTail("xyz");
    return t.checkExpect(this.deque1.size(), 1, "Add to empty deque")
        && t.checkExpect(((Node<String>) this.deque1.header.prev).data, "test", "Correct data")
        && t.checkExpect(this.deque2.size(), 5, "Add to non-empty deque")
        && t.checkExpect(((Node<String>) this.deque2.header.prev).data, "xyz", "New tail data");
  }

  /**
   * Test removeFromHead method
   */
  boolean testRemoveFromHead(Tester t) {
    this.initExamples();
    String removed = this.deque2.removeFromHead();
    return t.checkExpect(removed, "abc", "Removed correct value")
        && t.checkExpect(this.deque2.size(), 3, "Size decreased")
        && t.checkExpect(((Node<String>) this.deque2.header.next).data, "bcd", "New head");
  }

  /**
   * Test removeFromTail method
   */
  boolean testRemoveFromTail(Tester t) {
    this.initExamples();
    String removed = this.deque2.removeFromTail();
    return t.checkExpect(removed, "def", "Removed correct value")
        && t.checkExpect(this.deque2.size(), 3, "Size decreased")
        && t.checkExpect(((Node<String>) this.deque2.header.prev).data, "cde", "New tail");
  }

  /**
   * Test find method
   */
  boolean testFind(Tester t) {
    this.initExamples();
    
    // Predicate to find "cde"
    IPred<String> findCde = new IPred<String>() {
      public boolean apply(String s) {
        return s.equals("cde");
      }
    };

    // Predicate that never matches
    IPred<String> neverMatch = new IPred<String>() {
      public boolean apply(String s) {
        return false;
      }
    };

    ANode<String> found = this.deque2.find(findCde);
    ANode<String> notFound = this.deque2.find(neverMatch);

    return t.checkExpect(found instanceof Node, true, "Found node is a Node")
        && t.checkExpect(((Node<String>) found).data, "cde", "Found correct data")
        && t.checkExpect(notFound instanceof Sentinel, true, "Not found returns sentinel")
        && t.checkExpect(notFound, this.deque2.header, "Not found returns header");
  }

  /**
   * Test removeNode method
   */
  boolean testRemoveNode(Tester t) {
    this.initExamples();
    ANode<String> nodeToRemove = this.deque2.find(new IPred<String>() {
      public boolean apply(String s) {
        return s.equals("bcd");
      }
    });
    
    int sizeBefore = this.deque2.size();
    this.deque2.removeNode(nodeToRemove);
    
    return t.checkExpect(this.deque2.size(), sizeBefore - 1, "Size decreased")
        && t.checkExpect(this.deque2.find(new IPred<String>() {
          public boolean apply(String s) {
            return s.equals("bcd");
          }
        }) instanceof Sentinel, true, "Node removed");
  }

  /**
   * Test that removing from empty deque throws exception
   */
  boolean testRemoveFromEmpty(Tester t) {
    this.initExamples();
    try {
      this.deque1.removeFromHead();
      return t.checkExpect(false, true, "Should have thrown exception");
    } catch (RuntimeException e) {
      return t.checkExpect(true, true, "Exception thrown correctly");
    }
  }

  /**
   * Test that removing sentinel does nothing
   */
  boolean testRemoveSentinel(Tester t) {
    this.initExamples();
    int sizeBefore = this.deque2.size();
    this.deque2.removeNode(this.deque2.header);
    return t.checkExpect(this.deque2.size(), sizeBefore, "Size unchanged");
  }
}

// Simple Tester class for running tests
class Tester {
  boolean checkExpect(Object actual, Object expected, String message) {
    boolean result = actual.equals(expected);
    if (!result) {
      System.out.println("FAIL: " + message + " - Expected: " + expected + ", Got: " + actual);
    } else {
      System.out.println("PASS: " + message);
    }
    return result;
  }
}
