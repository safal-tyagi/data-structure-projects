// MyStack class that uses an ArrayList
// **************************************
// Code from MyArrayList by author reused to implement MyStack


public class MyStack<AnyType> implements Iterable<AnyType> {
    /**
     * Construct an empty ArrayList.
     */
    public MyStack() {
        doClear();
    }

    /**
     * Returns the number of items in this collection.
     *
     * @return the number of items in this collection.
     */
    public int size() {
        return theSize;
    }

    /**
     * Returns true if this collection is empty.
     *
     * @return true if this collection is empty.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the item at position idx.
     *
     * @param idx the index to search in.
     * @throws ArrayIndexOutOfBoundsException if index is out of range.
     */
    public AnyType get(int idx) {
        if (idx < 0 || idx >= size())
            throw new ArrayIndexOutOfBoundsException("Index " + idx + "; size " + size());
        return theItems[idx];
    }

    /**
     * Changes the item at position idx.
     *
     * @param idx    the index to change.
     * @param newVal the new value.
     * @return the old value.
     * @throws ArrayIndexOutOfBoundsException if index is out of range.
     */
    public AnyType set(int idx, AnyType newVal) {
        if (idx < 0 || idx >= size())
            throw new ArrayIndexOutOfBoundsException("Index " + idx + "; size " + size());
        AnyType old = theItems[idx];
        theItems[idx] = newVal;

        return old;
    }

    @SuppressWarnings("unchecked")
    public void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize)
            return;

        AnyType[] old = theItems;
        theItems = (AnyType[]) new Object[newCapacity];
        for (int i = 0; i < size(); i++)
            theItems[i] = old[i];
    }

    /**
     * Pushes an item to this collection, at the end.
     *
     * @param x any object.
     * @return true.
     */
    public boolean push(AnyType x) {
        add(size(), x);
        return true;
    }

    /**
     * Adds an item to this collection, at the specified index.
     *
     * @param x any object.
     * @return true.
     */
    public void add(int idx, AnyType x) {
        if (theItems.length == size())
            ensureCapacity(size() * 2 + 1);

        for (int i = theSize; i > idx; i--)
            theItems[i] = theItems[i - 1];

        theItems[idx] = x;
        theSize++;
    }

    /**
     * Pops an item to this collection, at the end.
     *
     * @return top item from the collection.
     */
    public AnyType pop() {
        return remove(size() - 1);
    }

    /**
     * Removes an item from this collection.
     *
     * @param idx the index of the object.
     * @return the item was removed from the collection.
     */
    public AnyType remove(int idx) {
        AnyType removedItem = theItems[idx];

        for (int i = idx; i < size() - 1; i++)
            theItems[i] = theItems[i + 1];
        theSize--;

        return removedItem;
    }

    /**
     * Change the size of this collection to zero.
     */
    public void clear() {
        doClear();
    }

    private void doClear() {
        theSize = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    /**
     * Obtains an Iterator object used to traverse the collection.
     *
     * @return an iterator positioned prior to the first element.
     */
    public java.util.Iterator<AnyType> iterator() {
        return new ArrayListIterator();
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
     * This is the implementation of the ArrayListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyStack.
     */
    private class ArrayListIterator implements java.util.Iterator<AnyType> {
        private int current = 0;
        private boolean okToRemove = false;

        public boolean hasNext() {
            return current < size();
        }


        public AnyType next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            okToRemove = true;
            return theItems[current++];
        }

        public void remove() {
            if (!okToRemove)
                throw new IllegalStateException();

            MyStack.this.remove(--current);
            okToRemove = false;
        }
    }

    private static final int DEFAULT_CAPACITY = 10;

    private AnyType[] theItems;
    private int theSize;
}


//  Use MyStack class to test balancing of nested symbols
// ************************************************************
class TestBalancedSymbols {
    static boolean isPaired(Character first, Character second) {
        if ((first == '(' && second == ')') ||
                (first == '{' && second == '}') ||
                (first == '[' && second == ']'))
            return true;
        else
            return false;
    }

    static boolean isBalanced(Character exp[]) {
        MyStack<Character> myStack = new MyStack<>();

        boolean isBalc = false;
        int len = exp.length;
        for (int i = 0; i < exp.length; i++) {
            // if opening expression push to stack
            if (exp[i] == '{' || exp[i] == '(' || exp[i] == '[')
                myStack.push(exp[i]);
            // if closing then pop one symbol and match with incoming
            if (exp[i] == '}' || exp[i] == ')' || exp[i] == ']') {
                if (myStack.isEmpty())
                    break;
                else if (!isPaired(myStack.pop(), exp[i]))
                    break;
            }
        }
        if (myStack.isEmpty())
            isBalc = true;

        return isBalc;
    }

    public static void main(String[] args) {
        System.out.println("Enter symbol expression: ");
        Character exp[] = {'[', '{', '(', ')', '}', ']', '[', ']'};

        System.out.println("Expression is balanced: " + isBalanced(exp));
    }
}

