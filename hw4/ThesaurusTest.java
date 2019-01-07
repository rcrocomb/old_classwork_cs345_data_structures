import junit.framework.TestCase;

/*
 * Created on Mar 8, 2004
 */

/**
 * @author Robert Crocombe
 *
 * Unit tests for Thesaurus class.
 */
public class ThesaurusTest extends TestCase
{
  private Thesaurus rogets;

// various test strings

  private static final String divider =
  new String("------------------------------------------------------------");

  private static final String a1 = "Garfield";
  private static final String a1s = "unfunny comic";
  private static final String a2s = "fat cat";

  private static final String la1 = "garfield";
  private static final String la1s = "lasanga disposal device";


  private static final String b1 = "Jam";
  private static final String b1s = "Jelly";
  private static final String b2s = "Preserve";
  private static final String b3s = "Sticky Situation";

  private static final String c1 = "Dog";
  private static final String c1s = "Canine";
  private static final String c2s = "Korean Delicacy";
  private static final String c3s = "Man's Best Friend";

  /**
   * Constructor for ThesaurusTest.
   * @param arg0
   */
  public ThesaurusTest(String arg0)
  {
    super(arg0);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    rogets = new Thesaurus();
  }

  // Ooh baby.
  public void testEmpty1()
  {
    assertTrue(rogets.isEmpty());
  }

  // Test insert of a single node.
  public void testInsert1()
  {
    // insert 1st node into tree
    rogets.insert(a1,a1s);

    // Run a number of checks:
    //  (a) Insertion seems to have worked: word is found in tree
    //  (b) Tree is no longer empty
    //  (c) Size reflects insertion
    assertTrue(rogets.inTree(a1));
    assertTrue(!rogets.isEmpty());
    assertEquals(1, rogets.getSize());
    System.out.println(rogets);
  }

  // a single node with > 1 synonyms
  //
  // This tests to make sure:
  //    (a) No double insertions happen
  //    (b) Synonyms are correctly appended
  public void testInsert2()
  {
    rogets.insert(a1,a1s);
    rogets.insert(a1,a2s);

    assertTrue(!rogets.isEmpty());
    assertEquals(1, rogets.getSize());
    System.out.println(rogets);
  }

  // Test having several nodes
  public void testInsert3()
  {
    System.out.println(divider);
    rogets.insert(a1,a1s);
    rogets.insert(a1,a2s);

    rogets.insert(b1, b1s);
    rogets.insert(b1, b1s);  // Look for only single insertion
    rogets.insert(b1, b2s);
    rogets.insert(b1, b3s);

    rogets.insert(c1, c1s);
    rogets.insert(c1, c2s);
    rogets.insert(c1, c3s);

    assertTrue(!rogets.isEmpty());
    assertEquals(3,rogets.getSize());

    rogets.printTree();
    System.out.println("\n" + rogets);
    System.out.println(divider);
  }

  // Make sure "garfield" and "Garfield" compare the same
  public void testCaseInsensitivity()
  {
    rogets.insert(a1,a1s);
    rogets.insert(la1,la1s);

    assertTrue(!rogets.isEmpty());
    assertEquals(1,rogets.getSize());

    System.out.println(rogets);
  }

  // Ensure that when every synonym for a word already has it's own node, that
  // the original word is correctly reinserted with its synonyms in proper
  // order.
  public void testFullRotation()
  {
    String a1 = "happy";
    String a1s = "cheerful";
    String a2s = "delirious";

    String b1 = "cheerful";
    String b1s = "perky";

    String c1 = "delirious";
    String c1s = "overjoyed";
    String c2s = "ill";    // damn you, homonyms! *shakes fist*

    rogets.insert(a1,a1s);  // build node for "happy"
    rogets.insert(a1,a2s);

    rogets.insert(b1,b1s);  // build node for "cheerful"

    rogets.insert(c1,c1s);  // build node for "delirious"
    rogets.insert(c1,c2s);

    // Should do a full rotation though all "happy" synonyms and come back
    // and re-insert happy.
    rogets.removeAndInsertNew(a1);

    // Test size and status
    assertEquals(3,rogets.getSize());
    assertTrue(!rogets.isEmpty());

    System.out.println(divider);
    System.out.println(rogets);

    // Make sure "happy" is still a top-level node
    assertTrue(rogets.inTree(a1));
  }

  public static Thesaurus buildTree()
  {
    Thesaurus t = new Thesaurus();

  // same code as in insert cases
    TextReader synFile = new TextReader("syn1.txt");
    TextReader kb = new TextReader();

    String word, syn;
    while (synFile.ready()){
      word = synFile.readWord();
      syn = synFile.readWord();
      t.insert(word,syn);
    }
  // add a few other cases to make things more interesting
    t.insert("poor", "impoverished");
  // end of insert cases

    System.out.println("Starting tree: ");
    t.printTree();
    return t;
  }

  // Looks good after I checked results by hand.
  //
  // No RemoveandInsertNews here.
  public void testReadAText()
  {
    Thesaurus blah = buildTree();

    System.out.println(divider + "\n" + blah + divider + "\n");

    TextReader infile = new TextReader("infile.txt");
    String output = new String();
    String word;
    while( infile.ready())
    {
      word = infile.readWord();
      String synonym = blah.nextSyn(word);
      output += ((synonym == null) ? word : synonym) + " ";
    }
    System.out.println("Revised to: " + output + "\n");
    System.out.println(blah);
  }

}
