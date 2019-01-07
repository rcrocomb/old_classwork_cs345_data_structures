import java.util.Comparator;
import java.util.Iterator;

import net.datastructures.BTPosition;
import net.datastructures.Entry;
import net.datastructures.Position;

/*
 * Created on Mar 7, 2004
 */

/**
 * @author Robert Crocombe
 *
 * This class extends a modified version of the authors' AVLTree
 * class, using it to store information about a word and synonyms for
 * that word.
 */
public class SynonymTree extends AVLTree
{

  /**
   *  Comparisons should be case insensitive.
   */
  public SynonymTree()
  {
    super(new StringComparator());
  }

  /**
   * @param arg0
   *
   * Build yer own Comparator craziness.
   */
  public SynonymTree(Comparator arg0)
  {
    super(arg0);
  }

  // Extends createNode() so that we create SynonymNode-based rather than
  // AVLNode-based trees.
  protected BTPosition createNode(
    Object element,
    BTPosition parent,
    BTPosition left,
    BTPosition right)
  {
    return new SynonymNode(element,parent,left,right);
  }

  // Print the tree contents in sorted order by calling the private
  // printTree method with the root of the tree and an empty list of tabs.
  public void printTree( )
  {
     if( isEmpty( ) )
       System.out.println( "Empty tree" );
     else
       printTree( root ,"");
  }

  // Internal, recursive method to print a tree in sorted order.
  // param t is the node that roots the tree,
  // param tabs is a string of tabs to indent tree correctly
  //
  // Changes made to support "external node" style trees.
  private void printTree( Position t, String tabs)
  {
     if( (t != null) && isInternal(t))
     {
       if(hasRight(t))
       {
         printTree( right(t), tabs +"\t");
       }
       System.out.print(tabs);
       System.out.println(((SynonymNode)t).minimal());
       if(hasLeft(t))
       {
         printTree( left(t),tabs+"\t");
       }
     }
  }

  // Prints out tree nodes in order, using the provided format.
  public String toString()
  {
    String out = new String();
    Iterator i = positions();
    while( i.hasNext())
    {
      SynonymNode n = (SynonymNode)i.next();
      if( n.element() == null)
      {
        continue;
      }
      out += n;
    }
    return out;
  }
}
