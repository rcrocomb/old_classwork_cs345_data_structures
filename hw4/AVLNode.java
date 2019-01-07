import net.datastructures.BTNode;
import net.datastructures.BTPosition;

/**
 *
 * @author Robert Crocombe
 *
 * Tore the guts out of static inner class from authors.
 *
 * Other than that: no changes.
 */

  /** Nested class for the nodes of an AVL tree. */
  public class AVLNode extends BTNode {
    protected int height;  // we add a height field to a BTNode
    AVLNode() {/* default constructor */}
    /** Preferred constructor */
    AVLNode(Object element, BTPosition parent,
	    BTPosition left, BTPosition right) {
      super(element, parent, left, right);
      height = 0;
      if (left != null)
        height = Math.max(height, 1 + ((AVLNode) left).getHeight());
      if (right != null)
        height = Math.max(height, 1 + ((AVLNode) right).getHeight());
    } // we assume that the parent will revise its height if needed
    public void setHeight(int h) { height = h; }
    public int getHeight() { return height; }
  }

