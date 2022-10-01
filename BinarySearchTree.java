// BinarySearchTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the tree.
     */
    public BinarySearchTree( )
    {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        root = remove( x, root );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( )
    {
        if( isEmpty( ) )
            throw new RuntimeException ("Underflow Exception");
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( )
    {
        if( isEmpty( ) )
            throw new RuntimeException ("Underflow Exception");
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if not found.
     */
    public boolean contains( AnyType x )
    {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }


    /**
     * Recursively traverses the tree and returns the count of nodes.
     */

    private int nodeCount ()
    {
        return nodeCount(root);
    }

    private int nodeCount (BinaryNode<AnyType> t)
    {
        if ( t == null )
            return 0;
        else
            return 1 + nodeCount(t.left) + nodeCount(t.right);
    }


    /**
     * Returns true if the tree is full.
     * A full tree has every node as either a leaf or a parent with two children.
     */
    public boolean isFull ()
    {
        return isFull(root);
    }

    private boolean isFull (BinaryNode<AnyType> t)
    {
        // empty
        if ( t == null)
            return true;

        // both children null
        if (t.left == null && t.right == null)
            return true;

        // both children non-null
        if (t.left != null && t.right != null)
            return (isFull(t.left) && isFull(t.right));

        // any other case
        return false;
    }



    /**
     * Compares the structure of current tree to another tree and returns
     * true if they match.
     */
    public boolean compareStructure (BinarySearchTree<AnyType> t)
    {
        return compareStructure(this.root, t.root);
    }

    private boolean compareStructure (BinaryNode<AnyType> t1, BinaryNode<AnyType> t2)
    {
        // both has null nodes
        if ( t1 == null && t2 == null)
            return true;

        // if both have non-null nodes, compare left with left and right with right
        if ( t1 != null && t2 != null)
            return (compareStructure(t1.left, t2.left) &&
                    compareStructure(t1.right, t2.right));

        // any other case
        return false;
    }


    /**
     * Compares the current tree to another tree and returns true
     * if they are identical.
     */
    public boolean equals (BinarySearchTree<AnyType> t)
    {
        return equals(this.root, t.root);
    }

    private boolean equals (BinaryNode<AnyType> t1, BinaryNode<AnyType> t2)
    {
        // both has null nodes
        if ( t1 == null && t2 == null)
            return true;

        // if both have non-null nodes with same data
        // check equality left with left and right with right
        if ( (t1.element == t2.element) && t1 != null && t2 != null)
            return (equals(t1.left, t2.left) &&
                    equals(t1.right, t2.right));

        // any other case
        return false;
    }

    /**
     * Creates and returns a new tree that is a copy of the original tree.
     **/

    public BinarySearchTree<AnyType> copy ()
    {
        BinarySearchTree<AnyType> t = new BinarySearchTree<>();
        t.root = copy(root);
        return t;
    }

    private BinaryNode<AnyType> copy (BinaryNode<AnyType> t)
    {
        if (t == null)
            return null;

        BinaryNode<AnyType> node = new BinaryNode<AnyType>(t.element,null, null);
        node.left = copy(t.left);
        node.right = copy(t.right);

        return node;
    }


    /**
     * Creates and returns a new tree that is a mirror image of the original tree.
     **/
    public BinarySearchTree<AnyType> mirror ()
    {
        BinarySearchTree<AnyType> t = new BinarySearchTree<>();
        t.root = mirror(root);
        return t;
    }

    private BinaryNode<AnyType> mirror (BinaryNode<AnyType> t)
    {
        if (t == null)
            return null;

        BinaryNode<AnyType> node = new BinaryNode<AnyType>(t.element,null, null);
        node.left = mirror(t.right);
        node.right = mirror(t.left);

        return node;
    }

    /**
     * Compares the current tree to another tree and returns true
     * if they are identical.
     */

    public boolean isMirror (BinarySearchTree<AnyType> t)
    {
        return isMirror(this.root, t.root);
    }

    public boolean isMirror (BinaryNode<AnyType> t1, BinaryNode<AnyType> t2)
    {
        // both has null nodes
        if ( t1 == null && t2 == null)
            return true;

        // one has null nodes
        if ( t1 == null || t2 == null)
            return false;

        // if both have non-null nodes with same data
        // check equality left with right and right with left
        if (t1.element == t2.element)
            return true && (isMirror(t1.left, t2.right) &&
                    isMirror(t1.left, t2.right));

        // any other case
        return false;
    }


    /**
     * Performs a single rotation on the node having the passed value.
     */
    public void rotateRight (AnyType elem)
    {
        if ( root == null ) return;
        BinaryNode<AnyType> p = contains(elem, root);
        // no right rotation possible, if no left child present
        if (p.left == null) return;

        p = rotateRight(p);
    }

    private BinaryNode<AnyType> rotateRight (BinaryNode<AnyType> t)
    {
        if ( t == null ) return null;
        if (t == root) root = t.right;

        BinaryNode<AnyType> p = t.left;
        BinaryNode<AnyType> q = p.right;
        p.right = t;
        t.left = q;
        return p;
    }


    /**
     * Performs a single rotation on the node having the passed value.
     */
    public void rotateLeft (AnyType elem)
    {
        if ( root == null ) return;
        BinaryNode<AnyType> p = contains(elem, root);
        // no left rotation possible, if no right child present
        if (p.right == null) return;

        p = rotateLeft(p);
    }

    private BinaryNode<AnyType> rotateLeft (BinaryNode<AnyType> t)
    {
        if ( t == null ) return null;
        if (t == root) root = t.left;

        BinaryNode<AnyType> p = t.right;
        BinaryNode<AnyType> q = p.left;
        p.left = t;
        t.right = q;
        return p;
    }


    /**
     * Performs a level-by-level printing of the tree.
     */
    private void printLevels ()
    {
        if ( root == null )
            return;

        Queue<BinaryNode> queue = new LinkedList<>();
        queue.add(root);

        while (true)
        {
            int count = queue.size();
            if (count == 0) break;

            while(count > 0)
            {
                BinaryNode<AnyType> x = queue.peek();
                System.out.print(x.element + " ");
                queue.remove();

                if (x.left != null)
                    queue.add(x.left);

                if (x.right != null)
                    queue.add(x.right);
                count--;
            }
            System.out.println();
        }
    }



    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return new BinaryNode<>( x, null, null );

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            t.left = insert( x, t.left );
        else if( compareResult > 0 )
            t.right = insert( x, t.right );
        else
            ;  // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
    {
        if( t == null )
            return null;
        else if( t.left == null )
            return t;
        return findMin( t.left );
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
    {
        if( t != null )
            while( t.right != null )
                t = t.right;

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private BinaryNode<AnyType> contains( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return null;

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            return contains( x, t.left );
        else if( compareResult > 0 )
            return contains( x, t.right );
        else
            return t;    // Match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the subtree.
     */
    private void printTree( BinaryNode<AnyType> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }

    /**
     * Internal method to compute height of a subtree.
     * @param t the node that roots the subtree.
     */
    private int height( BinaryNode<AnyType> t )
    {
        if( t == null )
            return -1;
        else
            return 1 + Math.max( height( t.left ), height( t.right ) );
    }

    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode<AnyType>
    {
        // Constructors
        BinaryNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
    }


    /** The tree root. */
    private BinaryNode<AnyType> root;


    // Test program
    public static void main( String [ ] args )
    {
        BinarySearchTree<Integer> t1 = new BinarySearchTree<>( );
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>( );

        /* first BST
              5
           /     \
          3       7
         /  \    /  \
        2    4  6    8 */
        t1.insert(5);
        t1.insert(3);
        t1.insert(2);
        t1.insert(4);
        t1.insert(7);
        t1.insert(6);
        t1.insert(8);
        t1.printLevels();

        // second tree
        /* first BST
              5
           /     \
          3       7
         /  \    /  \
        2    4  6    8 */
        t2.insert(5);
        t2.insert(3);
        t2.insert(2);
        t2.insert(4);
        t2.insert(7);
        t2.insert(6);
        t2.insert(8);
        t2.printLevels();

        // a) nodeCount
        // Recursively traverses the tree and returns the count of nodes.
        System.out.println("Node Count: " + t1.nodeCount());

        // b) isFull
        // Returns true if the tree is full.  A full tree has every node
        // as either a leaf or a parent with two children.
        System.out.println("Is Full: " + t1.isFull());

        // c) compareStructure
        // Compares the structure of current tree to another tree and returns
        // true if they match.
        System.out.println("Compare Structures: " + t1.compareStructure(t2));

        // d) equals
        // Compares the current tree to another tree and returns true
        // if they are identical.
        System.out.println("Is Equal: " + t1.equals(t2));

        // e) copy
        // Creates and returns a new tree that is a copy of the original tree.
        BinarySearchTree<Integer> t3 = t1.copy();
        System.out.println("Copy: ");
        t3.printLevels();

        // f) mirror
        // Creates and returns a new tree that is a mirror image of the original tree.
        BinarySearchTree<Integer> t4 = t1.mirror();
        System.out.println("Mirror: ");
        t4.printLevels();

        // g) isMirror
        // Returns true if the tree is a mirror of the passed tree.
        System.out.println("Is Mirror: " + t1.isMirror(t4));

        // h) rotateRight
        // Performs a single rotation on the node having the passed value.
        BinaryNode<Integer> t = t1.contains(2);
        t1.rotateRight(t);
        System.out.println("Right rotate: ");
        t1.printLevels();

        // g) rotateLeft
        // As above but left rotation.
        t2.rotateLeft(2);
        System.out.println("Left rotate: ");
        t2.printLevels();
    }
}
