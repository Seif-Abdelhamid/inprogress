/**
 * A generic double-ended queue (deque) implementation using a circular doubly-linked list
 * with a sentinel node.
 * 
 * @param <T> the type of elements in this deque
 */

// Represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

// Abstract class representing a node in the deque
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  /**
   * Counts the number of data nodes starting from this node
   * (not counting sentinel nodes)
   */
  int countNodes() {
    if (this.isSentinel()) {
      return 0;
    } else {
      return 1 + this.next.countNodes();
    }
  }

  /**
   * Checks if this node is a sentinel
   */
  abstract boolean isSentinel();

  /**
   * Updates the links when adding a node before this one
   */
  void addBefore(ANode<T> newNode) {
    newNode.next = this;
    newNode.prev = this.prev;
    this.prev.next = newNode;
    this.prev = newNode;
  }

  /**
   * Updates the links when adding a node after this one
   */
  void addAfter(ANode<T> newNode) {
    newNode.prev = this;
    newNode.next = this.next;
    this.next.prev = newNode;
    this.next = newNode;
  }

  /**
   * Removes this node from the list by updating links
   */
  void remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
  }

  /**
   * Helper method to find the first node matching the predicate
   * 
   * @param pred the predicate to test
   * @return the first matching node, or the sentinel if no match is found
   */
  ANode<T> findHelper(IPred<T> pred) {
    if (this.isSentinel()) {
      return this; // Return sentinel if we've reached the end
    } else {
      Node<T> node = (Node<T>) this;
      if (pred.apply(node.data)) {
        return this; // Found a match
      } else {
        return this.next.findHelper(pred); // Continue searching
      }
    }
  }
}

// Sentinel node marking the boundaries of the deque
class Sentinel<T> extends ANode<T> {
  /**
   * Constructor that initializes the sentinel to point to itself
   */
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  @Override
  boolean isSentinel() {
    return true;
  }
}

// Data-carrying node in the deque
class Node<T> extends ANode<T> {
  T data;

  /**
   * Constructor that takes just a value and initializes next and prev to null
   */
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  /**
   * Convenience constructor that takes a value and two nodes,
   * initializes the node, and updates the given nodes to refer back to this node
   * 
   * @param data the data value
   * @param next the next node
   * @param prev the previous node
   * @throws IllegalArgumentException if either next or prev is null
   */
  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Next and prev nodes cannot be null");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    // Update the links in the adjacent nodes
    next.prev = this;
    prev.next = this;
  }

  @Override
  boolean isSentinel() {
    return false;
  }
}

// Main deque class
class Deque<T> {
  Sentinel<T> header;

  /**
   * Constructor that initializes the header to a new Sentinel
   */
  Deque() {
    this.header = new Sentinel<T>();
  }

  /**
   * Convenience constructor that takes a particular Sentinel value to use
   */
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  /**
   * Counts the number of nodes in this deque (not including the header node)
   * 
   * @return the number of data nodes
   */
  int size() {
    return this.header.next.countNodes();
  }

  /**
   * Adds a value at the head (front) of the deque
   * 
   * @param value the value to add
   */
  void addAtHead(T value) {
    Node<T> newNode = new Node<T>(value);
    this.header.addAfter(newNode);
  }

  /**
   * Adds a value at the tail (back) of the deque
   * 
   * @param value the value to add
   */
  void addAtTail(T value) {
    Node<T> newNode = new Node<T>(value);
    this.header.addBefore(newNode);
  }

  /**
   * Removes the first node from the deque
   * 
   * @return the data from the removed node
   * @throws RuntimeException if the deque is empty
   */
  T removeFromHead() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty deque");
    }
    Node<T> firstNode = (Node<T>) this.header.next;
    T data = firstNode.data;
    firstNode.remove();
    return data;
  }

  /**
   * Removes the last node from the deque
   * 
   * @return the data from the removed node
   * @throws RuntimeException if the deque is empty
   */
  T removeFromTail() {
    if (this.size() == 0) {
      throw new RuntimeException("Cannot remove from an empty deque");
    }
    Node<T> lastNode = (Node<T>) this.header.prev;
    T data = lastNode.data;
    lastNode.remove();
    return data;
  }

  /**
   * Finds the first node in this deque for which the given predicate returns true
   * 
   * @param pred the predicate to test
   * @return the first matching node, or the header if no match is found
   */
  ANode<T> find(IPred<T> pred) {
    return this.header.next.findHelper(pred);
  }

  /**
   * Removes the given node from this deque
   * Does nothing if the given node is the Sentinel header
   * 
   * @param node the node to remove
   */
  void removeNode(ANode<T> node) {
    if (node.isSentinel()) {
      return; // Don't remove the sentinel
    }
    node.remove();
  }
}

