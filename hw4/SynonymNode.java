import net.datastructures.BTPosition;
import net.datastructures.Entry;

/*
 * Created on Mar 07, 2004
 */

/**
 *
 * @author Robert Crocombe
 *
 * An extension of the book authors' class heirarchy, SynonymNode has
 * a reference to an "entry", which stores a key and a value.  The key
 * is the main word for which we are storing synonyms.  The value is
 * an instance of SynonymInfo, which contains the frequency
 * information and the list of alternatives.
 */

public class SynonymNode extends AVLNode
{

  // Constructor that dovetails with that of AVLNode and others in
  // that heirarchy so that createNode() correctly inserts nodes into
  // the AVLTree.
  public SynonymNode(
    Object e,
    BTPosition parent,
    BTPosition left,
    BTPosition right)
  {
    super(e, parent, left, right);
  }

  // Append new synonym to existing list of synonyms.
  public void append(String synonym)
  {
    SynonymInfo i = getInfo();
    i.append(synonym);
  }

  // Remove 1st synonym from list of synonyms.  See SynonymInfo for
  // what is meant by "first".
  public String remove()
  {
    SynonymInfo i = getInfo();
    return i.remove();
  }

  // How many synonyms are there?
  public int getSynonymsCount()
  {
    SynonymInfo i = getInfo();
    return i.getSynonymsCount();
  }

  // Find out how often we have looked up this word.
  public int count()
  {
    SynonymInfo i = getInfo();
    return i.count();
  }

  // Change count of how often we've looked up this word.  I have this
  // rather than a simple increment.
  public void count(int c)
  {
    SynonymInfo i = getInfo();
    i.count(c);
  }

  // Get the word for which we are keeping synonyms: the "key" value.
  public String key()
  {
    return (String) ((Entry) element()).key();
  }

  // Along the lines of the example printTree() routines, this only
  // returns the keyword for use in checking the structure of a tree.
  public String minimal()
  {
  	return key();
  }

  // Hopefully follows provided format.  Gives information about:
  //
  // keyword
  // height of the tree at this node
  // frequency of access of synonyms for keyword
  // list of synonyms for keyword
  public String toString()
  {
  	SynonymInfo i = getInfo();
    String out = new String();
    out += key() + "\theight: " + height() + "\t" + i + "\n";
    return out;
  }

////////////////////////////////////////////////////////////////////////////////
// Private
////////////////////////////////////////////////////////////////////////////////

  // Returns the "value" part of the entry that this node is storing:
  // this is an instance of the SynonymInfo class.
  //
  // Java: almost as bad as LISP.
  private SynonymInfo getInfo()
  {
		return (SynonymInfo) ((Entry) element()).value();
  }

  // Return the AVLTree-ish height of this node.
  private int height()
  {
    return getHeight();
  }

}
