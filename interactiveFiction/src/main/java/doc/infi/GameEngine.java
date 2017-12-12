package doc.infi;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

//The inbetween the game logic and the user
//User interacts by typing in text, which is handled by the game engine, and then makes appropriate calls for a result
public class GameEngine {
				
				private HashMap<String, GameObject> rooms;
				private HashMap<String, GameObject> objects;
				private GameObject player;

				private HashSet<String> verbs;
				private HashSet<String> nouns;
				private HashSet<String> actions;

				private HashSet directions;
				private Scanner input;
				private HashMap <String, String> commands;
				//private HashSet<Command> command;
				//Which of these ^v two options is correct?
				//private HashSet<String> commands;
				//private HashSet<String> actions;

				

				private XmlParser xmlParser;

				public GameEngine() {
								input = new Scanner(System.in);
								rooms = new HashMap<String, GameObject>();
								objects = new HashMap<String, GameObject>();
								commands = new HashMap<String, String>();
								nouns = new HashSet<String>();
								verbs = new HashSet<String>();
								actions = new HashSet<String>();
								player = new GameObject();
								xmlParser = new XmlParser();
				}

				public GameEngine(String filename) {
								input = new Scanner(System.in);
								rooms = new HashMap<String, GameObject>();
								objects = new HashMap<String, GameObject>();
								commands = new HashMap<String, String>();
								nouns = new HashSet<String>();
								actions = new HashSet<String>();
							  player = new GameObject();	
								xmlParser = new XmlParser(filename);
								loadGame();
				}

				public void loadGame() {
								//Extrapolate the actions possible
								xmlParser.loadFile();
								rooms = xmlParser.loadRooms();
								for(String room : rooms.keySet()) {
												//System.out.println("Loaded room " + room);
								}
								objects = xmlParser.loadObjects();
								actions = xmlParser.loadActions();
								player = xmlParser.loadPlayer();
								for(String room : rooms.keySet()) {
												for(String obj : rooms.get(room).getObjects().keySet()) {
																nouns.add(obj);
												}
								}
								for(String object : objects.keySet()) {
												for(String alt : objects.get(object).getAlt()) {
																nouns.add(alt);
												}
												for(String obj : objects.get(object).getObjects().keySet()) {
																nouns.add(obj);
												}
								}
								if(rooms.containsKey(player.getParentName())) {
												player.setParent(rooms.get(player.getParentName()));
								}
								for(String obj : objects.keySet()) {
												String parentName = objects.get(obj).getParentName();
												//System.out.println("Parent name for " + obj + " " + parentName);
												objects.get(obj).setParent(objects.get(parentName));
								}
								//Default actions with special behavior
								actions.add("take");
								actions.add("drop");
								actions.add("use");
								actions.add("open");
								actions.add("close");
								actions.add("look");
				}

				private String doCommand(String[] tokens) {
								String output = "";
								GameObject currentRoom = player.getParent();
								if(tokens.length == 2) {
												if(actions.contains(tokens[0])) {
																if(nouns.contains(tokens[1])) {
																				switch(tokens[0]) {
																								case "take":
																												output = currentRoom.take(player, tokens[1]);
																												break;
																								case "drop":
																												output = currentRoom.drop(player, tokens[1]);
																												break;
																								case "open":
																												output = currentRoom.open(currentRoom, tokens[1]);
																												break;
																								case "look":
																												output = currentRoom.look(tokens[1]);
																												break;
																								default:
																												output = player.getObjects().containsKey(tokens[1]) ? player.takeActionOn(tokens[0], tokens[1]) : currentRoom.takeActionOn(tokens[0], tokens[1]);
																				}

																				//output = currentRoom.takeActionOn(tokens[0], tokens[1]);
																				//if(!output.equals("You can't " + tokens[0] + " " + tokens[1] + " here.") && !output.equals("There's no " + tokens[1] + " here to " + tokens[0]) + ".") {
																								//switch(tokens[0]) {
																												//case "take":
																																//GameObject taken = currentRoom.take(tokens[1]);
																																//taken.setParent(player);
																																//break;
																												//case "drop":
																																
																}
																else {
																				output = "I don't recognize the noun " + tokens[1];
																}
												}
												else {
																output = "I don't recognize the verb " + tokens[0];
												}
								}

								if(tokens.length == 1) {
												if(actions.contains(tokens[0])) {
																output = currentRoom.takeAction(tokens[0]);
												}
												else if(nouns.contains(tokens[0])){
																output = "There's no verb in that sentence";
												}
												else {
																output = "I don't recognize the word " + tokens[0];
												}
								}
								//System.out.println("Preprocess output " + output);
								if(rooms.containsKey(output)) {
												currentRoom = rooms.get(output);
												player.setParent(currentRoom);
												player.setParentName(output);
								}
								else {
												System.out.println(output);
								}
								return output;
				}
				public void runGame() {
								GameObject currentRoom;
								//Check valid player, in room, that player.parent is not null
								while(true) {
								if(!rooms.containsKey(player.getParentName())) {
												input.close();
												System.exit(0);
												//Need a better full teardown method
								}
								else if(player.getParentName().equals("quit")) {
												System.exit(0);
								}
								currentRoom = player.getParent();
								currentRoom.setVisited(true);
								System.out.println(currentRoom.getName());
								HashSet<String> exitSet = new HashSet<String>();
								HashSet<String> objectSet = new HashSet<String>();
								HashSet<String> actionSet = new HashSet<String>();

								String exitString = "Exits in room: ";
								String objectString = "Objects in room: ";
								String actionString = "Actions in room: ";
								for(String exit : currentRoom.getExits().keySet()) {
												exitSet.add(exit);
								}
								for(String obj : currentRoom.getObjects().keySet()) {
												if(currentRoom.getObjects().get(obj).getVisible() && currentRoom.getObjects().get(obj).getNodeType().equals("object")) {
																objectSet.add(obj);	
												}
								}
								for(String act : currentRoom.getActions().keySet()) {
												actionSet.add(act);
								}
								for(String obj : currentRoom.getObjects().keySet()) {
												GameObject tempRef = currentRoom.getObjects().get(obj);
												if(tempRef.getVisible() && tempRef.getOpen()) {
																for(String act : tempRef.getAvailableActions()) {
																				actionSet.add(act);
																}
												}
								}
								for(String ex : exitSet) {
												exitString += ex + " ";
								}
								for(String ob : objectSet) {
												objectString += ob + " ";
								}
								for(String act : actionSet) {
												actionString += act + " ";
								}
								


								//for(String act : currentRoom.getAvailableActions()) {
												//acts += act + " ";
								//}/
								System.out.println();
								System.out.println(currentRoom.describeRoom());
								System.out.println(player.describeInv());
								System.out.println();
								System.out.println(actionString);
								//System.out.println(objectString);
								System.out.println(exitString);
								System.out.println();
								System.out.print("> ");
								//Take input
								String command = input.nextLine();
								System.out.println();
								//Parse Input, reducing words down and providing input in a command structure
								//Allow for parsing in between here by return tokens as the parsed input from the parser
								//With dictionary definitions produced by aliases and alt names
								String[] tokens = command.split("\\s+");
								doCommand(tokens);
								System.out.println();
								}
								//Interpret command structure for viable commands, check if it matches a command structure
								//Verbs with no special command structure default to VERB OBJECT
								//Commands should be top level, they map to VERB OBJECT, the verb is in the object
								//Possible verbs are passed up to the object parent, then the parent there, until you are at room status (parent = null)
								//Commands parsed to verbs so that works right, weirdly strucutred commands will still work
								//
								//Find if a room has a verb result, optional if all verbs work all the time in any given room, just default output
								//If it does/it is a valid verb, use the name of the object to get the object -> String object -> object alias -> return object.
								//Room has list of object aliases and names that map to GameObjects in the room, passed up from objects to their parents
				}

				public HashMap<String, GameObject> getRooms() {
								return rooms;
				}
				public HashMap<String, GameObject> getObjects() {
								return objects;
				}
				public GameObject getPlayer() {
								return player;
				}
				public HashSet<String> getVerbs() {
								return verbs;
				}

				public HashSet<String> getActions() {
								return actions;
				}
				public HashSet<String> getNouns() {
								return nouns;
				}
}				
