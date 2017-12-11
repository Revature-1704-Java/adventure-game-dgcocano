package doc.infi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GameObjectTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameObjectTest( String testName )
    {
        super( testName );
    }

		public void setUp() {
						GameObject gameObject = new GameObject();
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
    public void testGameObject()
    {
        assertTrue( true );
    } 

		public void testDefaultValuesGameObject() {
						GameObject gameObject = new GameObject();
						assertEquals("", gameObject.getNodeType());
						assertEquals("", gameObject.getName());
						assertEquals("", gameObject.getAlias());
						assertEquals("", gameObject.getListAlias());
						assertEquals("Looking at that wouldn't do much.", gameObject.getLook());
						assertEquals("A room.", gameObject.getDescription());
						assertEquals(null, gameObject.getParent());
						assertEquals(true, gameObject.getVisible());
						assertEquals(false, gameObject.getScenery());
						assertEquals(false, gameObject.getVisited());
						assertEquals("", gameObject.getTo());
						assertEquals(0, gameObject.getObjects().size());
						assertEquals(0, gameObject.getActions().size());
						assertEquals(0, gameObject.getExits().size());
						assertEquals(0, gameObject.getAvailableActions().size());
		}
		

}
