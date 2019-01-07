import net.datastructures.Entry;

/*
 * Created on Mar 7, 2004
 */

/**
 * @author Robert Crocombe
 *
 * Implements the spec outlined in CS345 HW#4.  Provides
 * thesaurus-like functionality: given a word for which to have
 * alternatives, and a list of such alternatives, can rotate through
 * these alternatives so that each is used as few times as possible in
 * place of the original key word.
 *
 * Stores keywords in an AVLTree.
 */
public class Thesaurus
{
  private SynonymTree synTree;  // Data goes here.

  // Basic constructor.  Creates an empty tree.
  public Thesaurus()
  {
    synTree = new SynonymTree();
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Returns the number of nodes in the tree: number of words for
  // which we are storing synonyms.
  public int getSize()
  {
    return synTree.size();
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Returns true if there are no words in the thesaurus, else false.
  public boolean isEmpty()
  {
    return synTree.size() == 0;
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Given a keyword "word" and a synonym "syn", creates a node with
  // "word" as its key and "syn" as an alternative, if no node exists
  // with "word" as it's keyword already.  If that is not the case
  // (there is a node with "word" as keyword), simply appends "syn" to
  // the list of synonyms (assuming it is not a duplicate: duplicates
  // are ignored).
  public void insert(String word, String syn)
  {
    Entry e = synTree.find(word);
    if (e != null)
    {
      // word already exists in tree: append syn to synonym list if not
      // already present.
      ((SynonymInfo)e.value()).append(syn);
    } else
    {
      // word not in tree.  Create new node with the word and add synonym as
      // first synonym in list.
      SynonymInfo newInfo = new SynonymInfo(syn);
      synTree.insert(word,newInfo);
    }
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Remove the node with the keyword "word" from the tree.
  public SynonymNode remove(String word)
  {
    Entry x = synTree.find(word);
    if( x == null)
    {
      return null;
    }

    Entry e = synTree.remove(x);
    if (e == null)
    {
      return null;
    }

    // oops.  Can't get to SynonymNode from Entry 'e' in book hierarchy,
    // because position() is protected on BSTEntry.  Hopefully having
    // null links won't do any harm.
    return new SynonymNode(e, null, null, null);
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Okay.  Given keyword "word", remove the node with "word" as it's
  // keyword, if there is one.  If there isn't, do nothing.
  //
  // If there is such a node, try and create a new node using "word"'s
  // first alternative.  Keep trying this for all synonyms until one
  // is found for which there is no already existing node, or until
  // the list of synonyms is exhausted.  If a collision occurs (find a
  // node already exists for synonym), re-append this synonym to list
  // of synonyms.  Result is 1 of 2 possibilities:
  //
  // 1. Created a node with a synonym.  In this case, add all
  // remaining synonyms from original keyword as synonyms to new
  // keyword.  Append original keyword as final synonym.  Reset access
  // counter to 0.
  //
  // 2. Rotated through entire list of synonyms and found all had
  // existing nodes.  Just re-insert "word" as a node (again), and
  // append all synonyms.  Reset access counter to 0.
  public void removeAndInsertNew(String word)
  {
    // Is there a node with "word" as its key?
    Entry e = synTree.find(word);
    if (e == null)
    {
      // No.
      return;
    }

    // Yes.  Remove it from the tree.
    e = synTree.remove(e);
    String key = (String) e.key();
    SynonymInfo info = (SynonymInfo)e.value();

    // Word is in tree.  Loop on alternatives until we find 1 (if any) that
    // doesn't also have an independent node.

    int listSize = info.getSynonymsCount();
    int elementsTried = 0;
    do
    {
      // We cannot have zero synonyms for a word: there must be at least 1.
      String  synonym = info.remove();

      // is synonym already in the tree as an independent node?
      if (synTree.find(synonym) != null)
      {
        // Yes it is.  So we want to try the next synonym.  We don't want to
        // lose this current one, though, so stick it back in node on the end.
        info.append(synonym);
      } else
      {
        // No it isn't.  Create new node with this word as "key".  Insert all
        // synonyms from previous list + original word @ end.  Count == 0 for
        // new node.

        // 1.  New node with 1st synonym as key.  Also add empty list to hold
        // synonyms.
        SynonymInfo newInfo = new SynonymInfo();
        synTree.insert(synonym, newInfo);

        // 2. Add remaining from synonyms (if any).
        synonym = info.remove();
        while (synonym != null)
        {
          // 2a.  Append to synonym list.
          newInfo.append(synonym);
          // 2b.  Look for next element in list.
          synonym = info.remove();
        }

        // 3. Add old original word as last synonym
        // We thus always have at least 1 synonym (necessary).
        newInfo.append(key);

        // 4. Okay, inserted node as best we could.  Done.
        break;
      }

      ++elementsTried;

      // True once we've gone through the list once: since we reinsert elements,
      // cannot test on list being null, cuz that'd be infinite.
    }
    while (elementsTried < listSize);

    // worst case: all synonyms on the list were already present as separate
    // nodes.  Reinsert original word.  Append synonyms.  0 frequency info.
    if (elementsTried == listSize)
    {
      SynonymInfo newInfo = new SynonymInfo();
      synTree.insert(key, newInfo);

      // Add synonyms: must be at least 1.
      String synonym = info.remove();
      boolean inserted = false;
      while (synonym != null)
      {
        newInfo.append(synonym);
        inserted = true;
        synonym = info.remove();
      }

      if (!inserted)
      {
        System.out.println("Do not have minimum of 1 synonym for word: " + key);
      }
    }
  }

  // FROM SPECIFICATION: don't fiddle with prototype.
  //
  // Given keyword "word", get 1st alternative from list of synonyms.
  // Append this synonym to end of list.  Bump count of accesses of
  // this keyword.
  public String nextSyn(String word)
  {
    // Do we have synonyms for word "word"
    Entry e = synTree.find(word);
    if (e == null)
    {
      return null;
    }

    // Get list of synonyms associated with "word"
    SynonymInfo info  = (SynonymInfo) e.value();

    // Increase count.
    info.count(info.count() + 1);

    // Get 1st synonym from list.
    String synonym = info.remove();

    // Add to end.
    info.append(synonym);

    return synonym;
  }

  // Formatted tree output.  Contains more info (frequency, height, list of
  // synonyms) than printTree() routine.
  public String toString()
  {
    return synTree.toString();
  }

  // True if a search on "word" return a non-null value.
  public boolean inTree(String word)
  {
    return synTree.find(word) != null;
  }

  // A basic routine that shows the keyword for each node.
  public void printTree()
  {
    synTree.printTree();
  }
}
