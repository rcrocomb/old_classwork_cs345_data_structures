import junit.framework.TestCase;

/*
 * Created on Mar 8, 2004
 */

/**
 * @author Robert Crocombe
 *
 * Unit test for SynonymInfo class.
 */
public class SynonymInfoTest extends TestCase
{
	private SynonymInfo info;

	private static final String a1 = "monkey";
	private static final String a2 = "cow";
	private static final String a3 = "pony";
	private static final String evil_incarnate = "Nyarlathotep";


  /**
   * Constructor for SynonymInfoTest.
   * @param arg0
   */
  public SynonymInfoTest(String arg0)
  {
    super(arg0);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    info = new SynonymInfo();
  }

	// test a 1 element insert and remove
	public void testAppend1()
	{
		info.append(a2);
		assertEquals(a2,info.remove());
	}

	// test append stuff
	public void testAppend2()
	{
		info.append(a2);
		info.append(a3);
		assertEquals(a2,info.remove());
		assertEquals(a3,info.remove());
	}

	//
	// remove() has been adequately tested by above
	//

	// Test ability to find out number of synonyms()
	public void testCount()
	{
		info.append(a1);
		info.append(a2);
		info.append(a3);
		assertEquals(3,info.getSynonymsCount());
	}

	public void testIgnoreDupes()
	{
		info.append(a2);
		info.append(a2);
		info.append(a2);
		info.append(a2);
		assertEquals(1,info.getSynonymsCount());
	}

	public void testToString()
	{
		info.append(a1);
		info.append(a2);
		info.append(a3);
		info.append(evil_incarnate);
		info.count(42);


		System.out.println(info);
	}
}
