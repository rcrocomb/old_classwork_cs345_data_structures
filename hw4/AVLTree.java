import java.util.Comparator;

import net.datastructures.BinarySearchTree;
import net.datastructures.BTPosition;
import net.datastructures.Dictionary;
import net.datastructures.Entry;
import net.datastructures.InvalidEntryException;
import net.datastructures.InvalidKeyException;
import net.datastructures.Position;

/**  Implementation of an AVL tree. */
/**
 * AVLTree class - implements an AVL Tree by extending a binary
 * search tree.
 *
 * @author Michael Goodrich, Roberto Tamassia, Eric Zamore
 *
 * Modifcations: Tore AVLNode static inner class out of here.  Other
 * than that: no changes.
 */

public class AVLTree extends BinarySearchTree implements Dictionary {
  public AVLTree(Comparator c)  { super(c); }
  public AVLTree() { super(); }

  /** Creates a new binary search tree node (overrides super's version). */
  protected BTPosition createNode(Object element, BTPosition parent,
              BTPosition left, BTPosition right) {
    return new AVLNode(element,parent,left,right);  // now use AVL nodes
  }
  /** Returns the height of a node (call back to an AVLNode). */
  protected int height(Position p)  {
    return ((AVLNode) p).getHeight();
  }
  /** Sets the height of an internal node (call back to an AVLNode). */
  protected void setHeight(Position p)  { // called only if p is internal
    ((AVLNode) p).setHeight(1+Math.max(height(left(p)), height(right(p))));
  }
  /** Returns whether a node has balance factor between -1 and 1. */
  protected boolean isBalanced(Position p)  {
    int bf = height(left(p)) - height(right(p));
    return ((-1 <= bf) &&  (bf <= 1));
  }
  /** Returns a child of p with height no smaller than that of the other child */
  /**
    * Return a child of p with height no smaller than that of the
    * other child.
    */
  protected Position tallerChild(Position p)  {
    if (height(left(p)) > height(right(p))) return left(p);
    else if (height(left(p)) < height(right(p))) return right(p);
    // equal height children - break tie using parent's type
    if (isRoot(p)) return left(p);
    if (p == left(parent(p))) return left(p);
    else return right(p);
  }
  /**
    * Rebalance method called by insert and remove.  Traverses the path from
    * zPos to the root. For each node encountered, we recompute its height
    * and perform a trinode restructuring if it's unbalanced.
    */
  protected void rebalance(Position zPos) {
    if(isInternal(zPos))
       setHeight(zPos);
    while (!isRoot(zPos)) {  // traverse up the tree towards the root
      zPos = parent(zPos);
      setHeight(zPos);
      if (!isBalanced(zPos)) {
	// perform a trinode restructuring at zPos's tallest grandchild
        Position xPos =  tallerChild(tallerChild(zPos));
        zPos = restructure(xPos); // tri-node restructure (from parent class)
        setHeight(left(zPos));  // recompute heights
        setHeight(right(zPos));
        setHeight(zPos);
      }
    }
  }
  // overridden methods of the dictionary ADT
  /**
    * Inserts an item into the dictionary and returns the newly created
    * entry.
    */
  public Entry insert(Object k, Object v) throws InvalidKeyException  {
    Entry toReturn = super.insert(k, v); // calls our new createNode method
    rebalance(actionPos); // rebalance up from the insertion position
    return toReturn;
  }
  /** Removes and returns an entry from the dictionary. */
  public Entry remove(Entry ent) throws InvalidEntryException {
    Entry toReturn = super.remove(ent);
    if (toReturn != null)   // we actually removed something
      rebalance(actionPos);  // rebalance up the tree
    return toReturn;
  }
} // end of AVLTree class
