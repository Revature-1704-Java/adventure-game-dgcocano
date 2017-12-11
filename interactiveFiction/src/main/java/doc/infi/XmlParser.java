package doc.infi;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XmlParser {

				private String filename;
				private File fXmlFile;
				DocumentBuilderFactory dbFactory;
				DocumentBuilder dBuilder;
				Document doc;

				public XmlParser() {
								filename = "./files/default.xml";
				}
				public String getFileName() {
								return filename;
				}
				public DocumentBuilderFactory getDocumentBuilderFactory() {
								return dbFactory;
				}
				public File getFile() {
								return fXmlFile;
				}
				public DocumentBuilder getDocumentBuilder() {
								return dBuilder;
				}
				public Document getDocument() {
								return doc;
				}
				public XmlParser(String filename) {
								this.filename = filename;
				}

				public void loadFile() {
								try {
												fXmlFile = new File(filename);
												dbFactory = DocumentBuilderFactory.newInstance();
												dBuilder = dbFactory.newDocumentBuilder();
												doc = dBuilder.parse(fXmlFile);
												doc.getDocumentElement().normalize();
								}
								catch(Exception ex) {
												ex.printStackTrace();
								}
				}

				private NodeList returnAll(String tagName) {

								NodeList tagList = doc.getElementsByTagName(tagName);
								printAll(tagList);
								return tagList;
								//for(int temp = 0; temp < tagList.getLength(); temp++) {
												//Node tagNode = tagList.item(temp);
												//Element tagElement = (Element) tagNode;
								//}

				}

				public HashMap<String, GameObject> loadRooms() {
								HashMap<String, GameObject> rooms = new HashMap<String, GameObject>();
								NodeList roomList = doc.getElementsByTagName("room");			
								for(int i = 0; i < roomList.getLength(); i++) {
												Node roomNode = roomList.item(i);
												Element roomElement = (Element) roomList.item(i);
												rooms.put(roomElement.getAttribute("name"), new GameObject(roomNode));
								}
								return rooms;
				}

				public HashMap<String, GameObject> loadObjects() {
								HashMap<String, GameObject> rooms = new HashMap<String, GameObject>();
								NodeList roomList = doc.getElementsByTagName("object");			
								for(int i = 0; i < roomList.getLength(); i++) {
												Node roomNode = roomList.item(i);
												Element roomElement = (Element) roomList.item(i);
												rooms.put(roomElement.getAttribute("name"), new GameObject(roomNode));
								}
								return rooms;
				}

				public HashSet<String> loadActions() {
								HashSet<String> rooms = new HashSet<String>();
								NodeList roomList = doc.getElementsByTagName("action");			
								for(int i = 0; i < roomList.getLength(); i++) {
												Node roomNode = roomList.item(i);
												Element roomElement = (Element) roomList.item(i);
												rooms.add(roomElement.getAttribute("name")); 
								}
								return rooms;
				}

				public GameObject loadPlayer() {
								GameObject player = new GameObject();
								NodeList roomList = doc.getElementsByTagName("player");			
								for(int i = 0; i < roomList.getLength(); i++) {
												Node roomNode = roomList.item(i);
												player = new GameObject(roomNode);
								}
								return player;
				}
	
				public void printAll(NodeList nodeList) {
								for(int i = 0; i < nodeList.getLength(); i++) {
												Element node = (Element) nodeList.item(i);
												System.out.println("Node received " + node.getNodeName() + node.getAttribute("name"));
								}
				}


 public void run() { 

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList roomList = doc.getElementsByTagName("room");

	System.out.println("----------------------------");

	for (int temp = 0; temp < roomList.getLength(); temp++) {

		Node roomNode = roomList.item(temp);

		System.out.println("\nCurrent Element :" + roomNode.getNodeName());

		if (roomNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) roomNode;

			System.out.println("Room name: " + eElement.getAttribute("name"));
			NodeList elementList = eElement.getElementsByTagName("exit");
			String exits = "";
			for(int i = 0; i < elementList.getLength(); i++) {
							exits+= elementList.item(i).getTextContent() + " ";
			}
			System.out.println("First of Exits : " + eElement.getElementsByTagName("exit").item(0).getTextContent());
			System.out.println(exits);
			
		}
	}
  }

}
