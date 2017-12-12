package doc.infi;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
				GameEngine main = new GameEngine();
				if(args.length == 0) {
								main = new GameEngine("./files/story.xml");
				}
				if(args.length == 1) {
								main = new GameEngine(args[0]);
				}
				main.loadGame();
				main.runGame();

		}
}
