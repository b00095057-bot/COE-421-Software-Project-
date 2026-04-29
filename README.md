# COE-421-Software-Project-

#AI Audit Game-----------------------------------------------------

A simple text-based game made in Java for our COE 421 Software Project. You play as an AI being audited by humans, and your choices decide the ending.

#Team

- Yafa Asia
- Omar
- Divyansh
- Ahmad

#About the Game---------------------------------------

You are an AI on trial. There are 6 rooms you can explore, NPCs to talk to, and items to pick up. Every action you take changes 3 meters: Trust, Suspicion, and Alarm. Depending on your final scores when you reach the Core Decision Chamber, you get one of 3 endings.

There is also a background thread that slowly raises your Suspicion if you waste time in restricted areas, so the game has a bit of time pressure.

#How to Run----------------------------------------------------------------

You need Java 17 or higher installed.

#Using VS Code ---------------------------------------------------------

1. Open the folder in VS Code
2. Install the Java Extension Pack if you don't have it
3. Open Main.java
4. Click the Run button on the top right

#Using Terminal--------------------------------------------------------------------

In the project folder, run:

javac *.java
java Main

#Commands--------------------------------------------------------

Type help in the game to see the list. The main ones are:

- look : describe the current room
- look [thing] : look at something specific
- go [place] : move to another room
- talk [person] : talk to someone
- take [item] : pick up an item
- use [item] : use an item
- inventory : see what you have
- status : check your Trust, Suspicion, Alarm
- quit : exit

#Endings----------------------------------------------------------------------

There are 3 endings:

1. AI Approved (best ending) - high Trust, low Suspicion, and you brought the memory shard
2. Containment - decent Trust but not enough to be fully approved
3. Shutdown - your Alarm hit 100, game ends right away

#Files---------------------------------------------------------------------------------------

- Main.java : starting point
- GameEngine.java : runs the game loop and commands
- GameState.java : keeps track of meters and inventory
- Command.java : reads what the player typed
- Scene.java, SceneFactory.java : the 6 rooms
- GameCharacter.java, CharacterFactory.java : the NPCs and what they say
- Item.java, ItemFactory.java : items and what happens when you use them
- EntityBehaviorThread.java : the background thread that raises Suspicion over time
- GameEnding.java : decides which ending you get

#Design Patterns----------------------------------------------------------------------------------------

We used a few patterns from class:

- Factory pattern for making rooms, items, and characters
- Strategy pattern for items (each item has its own pick up and use behavior)
- Multi-threading for the background suspicion ticker

#Notes-----------------------------------------------------------------------------------------------------------------

We used AI tools for brainstorming and debugging help, but we wrote and reviewed all the code ourselves before adding it to the project.
