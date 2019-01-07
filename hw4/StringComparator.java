import java.util.Comparator;

/*
 * Created on Mar 11, 2004
 */

/**
 * @author Robert Crocombe
 *
 * Used in order to make comparison case-insensitive in extension of
 * AVLTree to SynonymTree.
 */

public class StringComparator implements Comparator
{

  public int compare(Object arg0, Object arg1)
  {
    return ((String)arg0).compareToIgnoreCase((String)arg1);
  }

}
