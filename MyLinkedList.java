/**
 * LinkedList class implements a doubly-linked list.
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType> {
    /**
     * Construct an empty LinkedList.
     */
    public MyLinkedList() {
        doClear();
    }

    private void clear() {
        doClear();
    }

    /**
     * Change the size of this collection to zero.
     */
    public void doClear() {
        beginMarker = new Node<>(null, null, null);
        endMarker = new Node<>(null, beginMarker, null);
        beginMarker.next = endMarker;

        theSize = 0;
        modCount++;
    }

    /**
     * Returns the number of items in this collection.
     *
     * @return the number of items in this collection.
     */
    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Adds an item to this collection, at the end.
     *
     * @param x any object.
     * @return true.
     */
    public boolean add(AnyType x) {
        add(size(), x);
        return true;
    }

    /**
     * Adds an item to this collection, at specified position.
     * Items at or after that position are slid one position higher.
     *
     * @param x   any object.
     * @param idx position to add at.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */
    public void add(int idx, AnyType x) {
        addBefore(getNode(idx, 0, size()), x);
    }

    /**
     * Adds an item to this collection, at specified position p.
     * Items at or after that position are slid one position higher.
     *
     * @param p Node to add before.
     * @param x any object.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */
    private void addBefore(Node<AnyType> p, AnyType x) {
        Node<AnyType> newNode = new Node<>(x, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
        modCount++;
    }


    /**
     * Returns the item at position idx.
     *
     * @param idx the index to search in.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public AnyType get(int idx) {
        return getNode(idx).data;
    }

    /**
     * Changes the item at position idx.
     *
     * @param idx    the index to change.
     * @param newVal the new value.
     * @return the old value.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public AnyType set(int idx, AnyType newVal) {
        Node<AnyType> p = getNode(idx);
        AnyType oldVal = p.data;

        p.data = newVal;
        return oldVal;
    }

    /**
     * Gets the Node at position idx, which must range from 0 to size( ) - 1.
     *
     * @param idx index to search at.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size( ) - 1, inclusive.
     */
    private Node<AnyType> getNode(int idx) {
        return getNode(idx, 0, size() - 1);
    }

    /**
     * Gets the Node at position idx, which must range from lower to upper.
     *
     * @param idx   index to search at.
     * @param lower lowest valid index.
     * @param upper highest valid index.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between lower and upper, inclusive.
     */
    private Node<AnyType> getNode(int idx, int lower, int upper) {
        Node<AnyType> p;

        if (idx < lower || idx > upper)
            throw new IndexOutOfBoundsException("getNode index: " + idx + "; size: " + size());

        if (idx < size() / 2) {
            p = beginMarker.next;
            for (int i = 0; i < idx; i++)
                p = p.next;
        } else {
            p = endMarker;
            for (int i = size(); i > idx; i--)
                p = p.prev;
        }

        return p;
    }

    /**
     * Removes an item from this collection.
     *
     * @param idx the index of the object.
     * @return the item was removed from the collection.
     */
    public AnyType remove(int idx) {
        return remove(getNode(idx));
    }

    /**
     * Removes the object contained in Node p.
     *
     * @param p the Node containing the object.
     * @return the item was removed from the collection.
     */
    private AnyType remove(Node<AnyType> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount++;

        return p.data;
    }


    //**************************************************************************
    // 5343 Project 01
    //**************************************************************************

    /**
     * a. Swaps the nodes at two given indices int p and int q.
     *
     * @param p the index of first node.
     * @param q the index of second node.
     */

    public void swap(int p, int q) {
        // special cases, no swap needed
        if (p == q || p > size() || q > size() || 0 == size()) return;

        // p node
        Node<AnyType> pNode = getNode(p);
        // q node
        Node<AnyType> qNode = getNode(q);

        if (Math.abs(p - q) == 1) {
            // nodes are adjacent
            // swap p and q self pointers
            pNode.next = qNode.next;
            qNode.prev = pNode.prev;

            // self swap
            if (qNode.next != null) qNode.next.prev = pNode;
            if (pNode.prev != null) pNode.prev.next = qNode;
        } else {   // node are not adjacent
            // swap p and q self pointers
            Node<AnyType> ptemp, ntemp;
            ptemp = pNode.prev;
            ntemp = pNode.next;

            pNode.prev = qNode.prev;
            pNode.next = qNode.next;

            qNode.prev = ptemp;
            qNode.next = ntemp;

            // manage pointers pointing to swapped p and q
            if (qNode.next != null) qNode.next.prev = qNode;
            if (qNode.prev != null) qNode.prev.next = qNode;

            if (pNode.next != null) pNode.next.prev = pNode;
            if (pNode.prev != null) pNode.prev.next = pNode;

        }
    }

    /**
     * Shifts/Rotates the list this many positions forward (if positive) or backward (if negative).
     * 1,2,3,4    shifted +2    3,4,1,2
     * 1,2,3,4    shifted -1    4,1,2,3
     *
     * @param pos the index of first node.
     */
    public void shift(int pos) {
        Node<AnyType> posNode;

        if (pos == 0) return; //no shift
        else if (pos > 0) posNode = getNode(pos);
        else posNode = getNode(size() + pos); // pos is -ve

        // store old head and tail nodes
        Node<AnyType> oldHeadNode = beginMarker.next;
        Node<AnyType> oldTailNode = endMarker.prev;

        // move old head to end of old end
        oldTailNode.next = oldHeadNode;
        oldHeadNode.prev = oldTailNode;

        // update head node
        beginMarker.next = posNode;
        endMarker.prev = posNode.prev;

        // change sentinels
        posNode.prev.next = endMarker;
        posNode.prev = beginMarker;
    }

    /** Erase:
     * receives an index position and number of elements as parameters, and
     * removes elements beginning at the index position for the number of
     * elements specified, provided the index position is within the size
     * and together with the number of elements does not exceed the size
     * @param idx the index of starting node.
     * @param num the number of elements
     */
    public void erase(int idx, int num) {
        // boundary checks skipped because of question constraints
        Node<AnyType> idxNode = getNode(idx);
        Node<AnyType> numNode = getNode((idx + num));

        if (idxNode.prev != null) idxNode.prev.next = numNode.next;
        if (numNode.next != null) numNode.next.prev = idxNode.prev;
    }

    /** insertList
     * receives another MyLinkedList and an index position as parameters, and
     * copies the list from the passed list into the list at the specified
     * position, provided the index position does not exceed the size.
     */
    public void insertList(MyLinkedList<AnyType> lst, int idx) {
        // boundary checks skipped because of question constraints
        Node<AnyType> idxNode = getNode(idx);
        Node<AnyType> idxPrev = idxNode.prev;


        if (idx == 0) { // special case: insert in the front
            if (beginMarker.next != null) beginMarker.next.prev = lst.endMarker.prev;
            if (lst.endMarker.prev != null) lst.endMarker.prev.next = beginMarker.next;

            beginMarker = lst.beginMarker;

        } else if (idx == size() - 1) { // special case: insert in the end
            if (endMarker.prev != null) endMarker.prev.next = lst.beginMarker.next;
            if (lst.beginMarker.next != null) lst.beginMarker.next.prev = endMarker.prev;

            endMarker = lst.endMarker;

        } else { // general case
            // connect to head
            if (lst.beginMarker.next != null) {
                idxPrev.next = lst.beginMarker.next;
                lst.beginMarker.next.prev = idxPrev;
            }

            // connect to tail
            if (lst.endMarker.prev != null) {
                idxNode.prev = lst.endMarker.prev;
                lst.endMarker.prev.next = idxNode;
            }
        }
    }

    /**
     * Returns a String representation of this collection.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");

        for (AnyType x : this)
            sb.append(x + " ");
        sb.append("]");

        return new String(sb);
    }

    /**
     * Obtains an Iterator object used to traverse the collection.
     *
     * @return an iterator positioned prior to the first element.
     */
    public java.util.Iterator<AnyType> iterator() {
        return new LinkedListIterator();
    }

    /**
     * This is the implementation of the LinkedListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyLinkedList.
     */
    private class LinkedListIterator implements java.util.Iterator<AnyType> {
        private Node<AnyType> current = beginMarker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;

        public boolean hasNext() {
            return current != endMarker;
        }

        public AnyType next() {
            if (modCount != expectedModCount)
                throw new java.util.ConcurrentModificationException();
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            AnyType nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }

        public void remove() {
            if (modCount != expectedModCount)
                throw new java.util.ConcurrentModificationException();
            if (!okToRemove)
                throw new IllegalStateException();

            MyLinkedList.this.remove(current.prev);
            expectedModCount++;
            okToRemove = false;
        }
    }

    /**
     * This is the doubly-linked list node.
     */
    private static class Node<AnyType> {
        public Node(AnyType d, Node<AnyType> p, Node<AnyType> n) {
            data = d;
            prev = p;
            next = n;
        }

        public AnyType data;
        public Node<AnyType> prev;
        public Node<AnyType> next;
    }

    private int theSize;
    private int modCount = 0;
    private Node<AnyType> beginMarker;
    private Node<AnyType> endMarker;
}

class TestLinkedList {
    public static void main(String[] args) {
        MyLinkedList<Integer> lst = new MyLinkedList<>();

        for (int i = 0; i < 10; i++)
            lst.add(i);
        System.out.println("Original List: " + lst);

        // TEST CODE
        //**********************************
        // SWAP
        lst.swap(2, 4);
        System.out.println("Swap idx:2 with idx:4 : " + lst);

        // SHIFT +ve
        lst.shift(3);
        System.out.println("Shift 3 position forward: " + lst);

        // SHIFT -ve
        lst.shift(-4);
        System.out.println("Shift 4 position backward: " + lst);

        // ERASE
        lst.erase(3, 2);
        System.out.println("Erase from 3 position upto next 2: " + lst);

        // INSERT NEW LIST
        MyLinkedList<Integer> lst2 = new MyLinkedList<>();
        for (int j = 20; j < 25; j++)
            lst2.add(j);
        System.out.println("List 2 elements: " + lst2);

        lst.insertList(lst2, 0);
        System.out.println("Inserted elements at position 0: " + lst);

    }
}

