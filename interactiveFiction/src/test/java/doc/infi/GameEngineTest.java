package doc.infi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GameEngineTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameEngineTest( String testName )
    {
        super( testName );
    }

		public void setUp() {
						GameEngine gameEngine = new GameEngine();
		}
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameEngineTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testGameEngine()
    {
        assertTrue( true );
    } 

		public void testDefaultValuesGameEngine() {
						GameEngine gameEngine = new GameEngine();
						assertEquals(0, gameEngine.getRooms().size());
						assertEquals(0, gameEngine.getObjects().size());
						assertEquals(0, gameEngine.getVerbs().size());
						assertEquals(0, gameEngine.getActions().size());
						assertEquals(0, gameEngine.getNouns().size());
		}
		

}
