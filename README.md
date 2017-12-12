# InteractiveFiction
Modular Text Adventure Framework

Run by going to ./interactiveFiction and entering the command java -cp ./target interactiveFiction-1.0-SNAPSHOT.jar doc.infi.App
You can run the jar with an input argument of ./files/"XML FILE NAME" for custom games that you have written or the default.xml that serves as a playing ground.
Running a default with no args will read the game from ./files/story.xml, which serves as the story for our text adventure assignment.

The game loads its state from these xml files, and generates rooms, objects, and their respective attributes from the xml file.
The design is first and foremost modular. I made the framework and tested with default.xml, then wrote the "story" found in story.xml in about 20 minutes, and had it loaded and ready to go with all the interactions you see there.
Boolean flag conditions are not yet generated from xml and I'm thinking of a way to parse those correctly without having access to scripting capability.
In my inital text adventure program I had more powerful parsing with aliases and alternate names, which I aim to incorporate on this at some point, but as of right now, all commands are parsed to two words only.
In this text adventure program I have much more modular capability and design power. I don't consider this a tradeoff, but rather a limitation of the time that I had available to work on it.
I took great inspiration from Colossal Cave Adventure and HitchHikers Guide to the Galaxy, as well as a little easter egg from Zork.

If you'd like to make your own and run through it please let me know, and I'd be more than happy to write detailed rules as to the current implementation, or you can try and reverse engineer it from looking at the game logic (not recommended at this stage) or from playing with the xml files (much more fun to do). 
However, it is late, and I wanted to provide you with a short, but highly interactive game experience (over a small set of commands due to time restraint, with more time I could write many more situations via the xml file).
So instead of no narrative gameplay, now there is a little bit.
Also instead of allowing you to see all the available objects in the room, I commented out the line that would provide you that, so that the interactions between player and objects are highlighted in their descriptive results, instead of the list format. If you want to get that functionality back, uncomment line 225 in GameEngine.java that reads System.out.println(objectString); and repackage with maven
