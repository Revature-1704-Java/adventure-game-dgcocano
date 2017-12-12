package doc.infi;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.*;

public class GameObject {
				
				//Inferrable through Element
				private String nodeType;
				private String name;

				private GameObject parent;
				private String parentName;

				private String alias;
				private String listalias;
				private HashSet<String> alt;

				//Rest are obtained through Element children analysis
				private String look;
				//When the object is looked at
				
				private String description;
				//When the room is looked at
				
				private boolean visible;
				private boolean scenery;

				private boolean visited;
				//visited can also be a number in order to keep track of number of times visited, if so desired
				private String to;

				private boolean locked;
				private boolean open;

				private HashMap<String, GameObject> objects;
				private HashMap<String, GameObject> exits;
				private HashMap<String, String> actions;
				private HashSet<String> availableActions;

				public GameObject() {
								nodeType = "";
								name = "";

								parent = null; 
								parentName = "";

								alias = "";
								listalias = "";
								alt = new HashSet<String>();

								look = "Looking at that wouldn't do much.";
								description = "A room.";

								visible = true;
								scenery = false;

								visited = false;
								to = "";

								locked = false;
								open = true;

								objects = new HashMap<String, GameObject>();
								actions = new HashMap<String, String>();
								exits = new HashMap<String, GameObject>();
								availableActions = new HashSet<String>();//To recognize if a room has an object with an action vs the room having it's own action
				}

				public GameObject(Node node) {
								this();
								if(node == null) {
												return;
								}
								Element gameNode = (Element) node;
								nodeType = gameNode.getNodeName();
								name = gameNode.getAttribute("name");
								alias = name;
								listalias = alias;
								to = gameNode.getAttribute("to");

								Element parentNode = (Element) gameNode.getParentNode();
								if(parentNode != null) {
												String pName = parentNode.getAttribute("name");
												if(pName != null) {
																parentName = pName.equals("game") ? "" : pName;
												}
								}

								loadObjects(gameNode);
								loadActions(gameNode);
								loadExits(gameNode);

								Node nNode = gameNode.getFirstChild();
								if(nNode == null) return;
								while(nNode.getNextSibling()!=null) {
												nNode = nNode.getNextSibling();
												if(nNode.getNodeName().equals("object") | nNode.getNodeName().equals("exit")) {
																Element eNode = (Element) nNode;
																this.objects.put(eNode.getAttribute("name"), new GameObject(eNode));
												}
												if(nNode.getNodeName().equals("exit")) {
																loadThisExit(gameNode);
												}
												if(nNode.getNodeName().equals("object")) {
												}
												switch(nNode.getNodeName()) {
																case "alt":
																				alt.add(nNode.getTextContent());
																				break;
																case "alias":
																				alias = nNode.getTextContent();
																				break;
																case "listalias":
																				listalias = nNode.getTextContent();
																				break;
																case "look":
																				look = nNode.getTextContent();
																				break;
																case "description":
																				description = nNode.getTextContent();
																				break;
																case "visible":
																				visible = nNode.getTextContent().toLowerCase().equals("true");
																				break;
																case "scenery":
																				scenery = nNode.getTextContent().toLowerCase().equals("true");
																				break;
																case "visited":
																				visited = nNode.getTextContent().toLowerCase().equals("true");
																				break;
																case "locked":
																				locked = nNode.getTextContent().toLowerCase().equals("true");
																				break;
																case "open":
																				open = nNode.getTextContent().toLowerCase().equals("true");
																				break;
												}
								}
				}

				private void loadObjects(Element node) {
								Node nNode = node.getFirstChild();
								if(nNode == null) return;
								while(nNode.getNextSibling()!=null) {
												nNode = nNode.getNextSibling();
												if(nNode.getNodeName().equals("object") | nNode.getNodeName().equals("exit")) {
																Element eNode = (Element) nNode;
																this.objects.put(eNode.getAttribute("name"), new GameObject(eNode));
												}
								}
				}

				private void loadExits(Element node) {
								Node nNode = node.getFirstChild();
								if(nNode == null) return;
								while(nNode.getNextSibling()!=null) {
												nNode = nNode.getNextSibling();
												if(nNode.getNodeName().equals("exit")) {
																Element eNode = (Element) nNode;
																this.objects.put(eNode.getAttribute("name"), new GameObject(eNode));
																this.exits.put(eNode.getAttribute("name"), new GameObject(eNode));
																//System.out.println("Exit " + eNode.getAttribute("name") + " for " + name + " leading to "  + this.objects.get(eNode.getAttribute("name")).getTo());
												}
								}
				}

				private void loadActions(Element node) {
								NodeList actions = node.getElementsByTagName("action");
								for(int i = 0; i < actions.getLength(); i++) {
												Element action = (Element) actions.item(i);
												availableActions.add(action.getAttribute("name"));
								}
								Node nNode = node.getFirstChild();
								if(nNode == null) return;
								while(nNode.getNextSibling()!=null) {
												nNode = nNode.getNextSibling();
												if(nNode.getNodeName().equals("action")) {
																Element eNode = (Element) nNode;
																this.actions.put(eNode.getAttribute("name"), nNode.getTextContent());
												}
								}
				}

				private void loadThisExit(Element node) {
								to = node.getAttribute("to");
								actions.put("go", to);
								availableActions.add("go");
								//System.out.println("implicit go for " + name + " leading to " + to);
				}

				private boolean hasAction(String action) {
								return availableActions.contains(action);
				}

				public boolean hasObject(String object) {
								boolean ret = false;
								ret = objects.containsKey(object) && objects.get(object).getVisible();
								return ret;
				}

				private String prefix(String term) {
								String pre = "";
								if(term.length() == 0) pre = term;
								else if(term.charAt(0) >= 'a' && term.charAt(0) <= 'z') {
												if(term.charAt(0) == 'a' || term.charAt(0) == 'e' || term.charAt(0) == 'i' || term.charAt(0) == 'o' || term.charAt(0) == 'u') {
																pre = "an ";
												}
												else {
																pre = "a ";
												}
								}
								else {
												pre = "the ";
								}
								return pre;
				}

				@Override
				public int hashCode() {
								return (name + to).hashCode();
				}

				@Override
				public String toString() {
								String print = "";
								print += name + " ";
								for(String act : availableActions) {
												print+=act + " action ";
								}
								for(String obj : getAvailableObjects()) {
												print += obj + " object ";
								}
								return print;
				}

				private int getNumberOfListObjects() {
								int result = 0;
								for(String obj : objects.keySet()) {
												GameObject ref = objects.get(obj);
												if(ref.getVisible() && !ref.getScenery() && !exits.containsKey(obj)) {
																result++;
												}
								}
								return result;
				}

				public String getDetails() {
								String details = "\n";
								String objRep = "";
								String postFix = ".";
								int i = 1;
								int l = getNumberOfListObjects();
								if(l == 0) return "";
								else {
												details +="You see ";
								}
								for(String obj : objects.keySet()) {
												GameObject ref = objects.get(obj);
												if(!exits.containsKey(obj) && ref.getVisible() && !ref.getScenery()) {
																if(!ref.getListAlias().equals("")) {
																				objRep = prefix(ref.getListAlias()) + ref.getListAlias();
																}
																else if(!ref.getAlias().equals("")) {
																				objRep = prefix(ref.getAlias()) + ref.getAlias();
																}
																else {
																				objRep = prefix(ref.getName()) + ref.getName();
																}
																details += objRep;
																if(i == l) {
																				postFix = ".";
																}
																else if(i == (l-1)) {
																				if(l ==2) {
																								postFix = " and ";
																				}
																				else {
																								postFix = " and, ";
																				}
																}
																else {
																				postFix = ", ";
																}
																details += postFix;
																i++;
												}
								}
								return details;
				}


				public String describeInv() {
								String ret = "You have";
								for(String obj : getObjects().keySet()) {
												GameObject object = getObjects().get(obj);
												if(objects.get(obj).getVisible() && !objects.get(obj).getScenery()) {
																ret += " " + prefix(object.getListAlias()) + object.getListAlias();
												}
								}
								if(ret.equals("You have")) return "You don't have anything.";
								return ret;
				}

				public String describeRoom() {
								String ret = description;
								for(String obj : getAvailableObjects()) {
												if(objects.get(obj).getVisible() && objects.get(obj).getScenery()) {
																ret += " " + objects.get(obj).getDescription();
												}
								}
								ret += "\n" + getDetails();
								return ret;
				}
				public String take(GameObject container, String object) {
								String output = "You can't take " + prefix(object) + " " + object + " here.";
								if(objects.containsKey(object)) {
												GameObject getObject = objects.get(object);
												if(getObject.getActions().containsKey("take")) {
																output = takeActionOn("take", object);
																getObject.setParent(container);
																getObject.setScenery(false);
																container.getObjects().put(object, getObject);
																for(String act : getObject.getActions().keySet()) {
																				container.getAvailableActions().add(act);
																}
																//container.putObject(object, getObject);
																objects.remove(object);
												}
								}
								return output;
				}

				public String drop(GameObject container, String object) {
								String output = "You don't have a " + object + " to drop.";
								if(container.getObjects().containsKey(object)) {
												GameObject getObject = container.getObjects().get(object);
												for(String act : getObject.getActions().keySet()) {
																container.getAvailableActions().remove(act);
												}
												container.getObjects().remove(object);
												getObject.setParent(container.getParent());
												getObject.getParent().putObject(object, getObject);
												output = "You place the " + object + " down.";
								}
								return output;
				}

				public String open(GameObject container, String object) {
								String output = "You can't open " + prefix(object) +  object + " here.";
								if(objects.containsKey(object)) {
												GameObject getObject = objects.get(object);
												if(getObject.getActions().containsKey("open")) {
																output = (getObject.getOpen() == true) ? "That's already open." : getObject.takeAction("open");
																getObject.setOpen(true);
																for(String obj : getObject.getObjects().keySet()) {
																				//getObject.setParent(container);
																				container.getObjects().put(obj, getObject.getObjects().get(obj));
																}
																for(String act : getObject.getActions().keySet()) {
																				container.getActions().put(act, getObject.getActions().get(act));
																				//getObject.getParent().getActions().put(act, getObject.getActions().get(act));
																}
																for(String act : getObject.getAvailableActions()) {
																				container.getAvailableActions().add(act);
																				//getObject.getParent().getAvailableActions().add(act);
																}
																for(String ex : getObject.getExits().keySet()) {
																				container.getExits().put(ex, getObject.getExits().get(ex));
																}
												}
								}
								return output;
				}

				public String look(String object) {
								String output = "Looking at that wouldn't do much.";
								if(objects.containsKey(object)) {
												GameObject getObject = objects.get(object);
												output = getObject.getLook();
								}
								return output;
				}


				public String takeAction(String action) {
								if(nodeType.equals("exit")) {
												return to;
								}
								String preName = prefix(name) + name;
								String output = nodeType.equals("room") ? "You can't " + action + " " + preName + " here." : "You can't " + action + " the " + preName;
								if(hasAction(action)) {
												output = actions.get(action);
								}
								return output;
				}
				public String takeActionOn(String action, String object) {
								String preObject = prefix(object) + object;
								System.out.println("Taking action " + action + " in " + name + " on " + preObject);
								String output = "You can't " + action + " " + preObject + " here.";
								if(hasAction(action)) {
												if(getAvailableObjects().contains(object)) {
																if(objects.get(object).getVisible()) {
																				output = objects.get(object).takeAction(action);
																}
																else {
																				output = "There's no " + object + " here to " + action + ".";
																}
												}
								}
								return output;
				}

				public void putObject(String object, GameObject gObject) {
								objects.put(object, gObject);
				}
				public HashSet<String> getAvailableObjects() {
								HashSet<String> availableObjects = new HashSet<String>();
								for(String obj : objects.keySet()) {
												availableObjects.add(obj);
								}
								return availableObjects;
				}
								//Getters and Setters
								public HashMap<String, String> getActions() {
												return actions;
								}
								public boolean getVisited() {
												return visited;
								}
								public void setVisited(boolean visited) {
												this.visited = visited;
								}
								public String getNodeType() {
												return nodeType;
								}
								public String getAlias() {
												return alias;
								}
								public String getListAlias() {
												return listalias;
								}
								public HashMap<String, GameObject> getExits() {
												return exits;
								}
								public HashSet<String> getAvailableActions() {
												return availableActions;
								}
								public HashSet<String> getAlt() {
												return alt;
								}
								public void setAlt(HashSet<String> alt) {
												this.alt = alt;
								}
								public HashMap<String, GameObject> getObjects() {
												return objects;
								}
								public void setObjects(HashMap<String, GameObject> objects) {
												this.objects = objects;
								}
								public String getName() {
												return name;
								}

								public String getTo() {
												return to;
								}

								public void setTo(String to) {
												this.to = to;
								}

								public boolean getOpen() {
												return open;
								}

								public void setOpen(boolean open) {
												this.open = open;
								}

								public boolean getLocked() {
												return locked;
								}

								public void setLocked(boolean locked) {
												this.locked = locked;
								}

								public GameObject getParent() {
												return parent;
								}

								public void setParent(GameObject parent) {
												this.parent = parent;
								}

								public String getParentName() {
												return parentName;
								}

								public void setParentName(String parentName) {
												this.parentName = parentName;
								}

								public boolean getVisible() {
												return visible;
								}

								public void setVisible(boolean visible) {
												this.visible = visible;
								}

								public boolean getScenery() {
												return scenery;
								}

								public void setScenery(boolean scenery) {
												this.scenery = scenery;
								}

								//get Description vs the description of the room plus it's objects etc.
								public String getDescription() {
												return description;
								}
								
								public void setDescription(String description) {
												this.description = description;
								}

								public String getLook() {
												return look;
								}

								public void setLook(String look) {
												this.look = look;
								}
}
