package doc.infi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class XmlParserTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public XmlParserTest( String testName )
    {
        super( testName );
    }

		public void setUp() {
						XmlParser xmlParser = new XmlParser();
		}
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameObjectTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testXmlParser()
    {
        assertTrue( true );
    } 

		public void testDefaultValuesXmlParser() {
						XmlParser xmlParser = new XmlParser();
						assertEquals("./files/default.xml", xmlParser.getFileName());
						assertEquals(null, xmlParser.getFile());
						assertEquals(null, xmlParser.getDocumentBuilderFactory());
						assertEquals(null, xmlParser.getDocumentBuilder());
						assertEquals(null, xmlParser.getDocument());
		}
		

}
