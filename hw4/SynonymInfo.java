import java.util.ArrayList;

/*
 * Created on Mar 7, 2004
 */

/**
 * @author Robert Crocombe
 *
 * This class holds values related to a key word (not stored here).
 * The data is:
 *
 *      count: a count of how many times this word has been looked up
 *
 *      synonyms: a list of user-provided synonyms for the key word.
 *      Each time one of these synonyms is retrieved, an external
 *      routine will increment count above to reflect that the word
 *      has been looked up.  Words with smaller indices (closer to 0),
 *      have been accessed less frequently than words with larger
 *      indices, except when no lookups at all have been performed,
 *      obviously.  No duplicates are allowed: any such are silently
 *      ignored.
 */
public class SynonymInfo
{
  private int count;            // how often word has been looked up
  private ArrayList synonyms;   // list of synonyms for "key".

  // Basic initialization
  public SynonymInfo()
  {
    count = 0;
    synonyms = new ArrayList();
  }

  // As above, but also supplies the first synonym for the list.
  public SynonymInfo(String alternate)
  {
    count = 0;
    synonyms = new ArrayList();
    append(alternate);
  }

  // Insert a new synonym, "synonym", on the list of synonyms.  No
  // duplicates are allowed: all such are silently ignored.
  public void append(String synonym)
  {
    if (!synonyms.contains(synonym))
    {
      synonyms.add(synonym);
    }
  }

  // Remove the least-recently-used (LRU) synonym from the list.
  //
  // Weird.  We have an indexless add, but no indexless remove from front?
  public String remove()
  {
    if( synonyms.size() > 0)
    {
      return (String)synonyms.remove(0);
    }
    return null;
  }

  // Get a count of how many synonyms there are in the list.
  public int getSynonymsCount()
  {
    return synonyms.size();
  }

  // Gets the number of times the keyword has been accessed.
  public int count()
  {
    return count;
  }

  // Modifies the count of the number of times the keyword has been
  // accessed.
  public void count(int c)
  {
    count = c;
  }

  // Hopefully follows the correct format.  Maybe an extra tab?
  public String toString()
  {
    String out = new String();
    out += "frequency: " + count + "\n";
    for(int i = 0; i < synonyms.size(); i++)
    {
      out += "\t\t" + get(i) + "\n";
    }
    out += "\n";
    return out;
  }

  // Private routine to take out some of Java's casting insanity.
   private String get(int index)
   {
     return (String)synonyms.get(index);
   }
}
