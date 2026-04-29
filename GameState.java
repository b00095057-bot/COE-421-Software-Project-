import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    // Yafa's part

    // stores current player location (which room they are in)
    // private because only the class changes it
    private String location;

    // trust affects endings / how NPCs react
    private int trustLevel;

    // suspicion increases when player does risky actions
    private int suspicionLevel;

    // global alert level of the system/facility
    private int alarmLevel;

    // inventory (the items that the player is carrying)
    // final meaning once set it can't be reassigned but can be changed
    private final List<String> bag;

    // stores important story events or flags like "doorUnlocked", "npcMet"
    // map is a data structure that stores key and value pair
    private final Map<String, Boolean> storyFlags;

    // keeps track of all commands entered by the player
    // final keyword keeps it safe --> make sure that it's not suddenly deleted (reset)
    // you can still add and remove a string at a time
    private final List<String> pastCommands;

    // counts how many turns have passed where each command = 1 turn
    private int turnsPassed;

    // constructor --> initializes default game state
    public GameState() {
        String[] startingRooms = {
                "bootChamber",
                "ethicsGarden",
                "securityMaze"
        };

        this.location = startingRooms[(int)(Math.random() * startingRooms.length)];// starting room
        this.trustLevel = 50;            // neutral trust
        this.suspicionLevel = 0;         // no suspicion at start
        this.alarmLevel = 0;             // no alarm at start
        this.bag = new ArrayList<>();    // empty inventory
        this.storyFlags = new HashMap<>(); // no events yet
        // HashMap<>() --> allowing fast lookup of whether specific story events have occurred
        this.pastCommands = new ArrayList<>(); // empty history
        this.turnsPassed = 0;            // game just started
    }

    // synchronized --> ensures safe access if multiple threads use this class
    // returns trust level
    public synchronized int getTrustLevel() {
        return trustLevel;
    }

    // updates trust and keeps it between 0 and 100
    public synchronized void updateTrust(int change, String reason) {
        trustLevel = Math.clamp(trustLevel + change, 0, 100); // clamp value to be in needed interval
        String sign = change > 0 ? "+" : ""; // add + for positive changes/ negative change will add the sign
        System.out.println("[Trust " + sign + change + "] " + reason);
    }

    public synchronized int getSuspicionLevel() {
        return suspicionLevel;
    }

    // updates suspicion and may trigger alarm if too high
    public synchronized void updateSuspicion(int change, String reason) {
        suspicionLevel = Math.clamp(suspicionLevel + change, 0, 100); // make sure it's in the range
        String sign = change > 0 ? "+" : "";
        System.out.println("[Suspicion " + sign + change + "] " + reason);

        // if suspicion gets higher than the set threshold --> automatically increase alarm by 5
        // reason --> high suspicion
        if (suspicionLevel >= 50) {
            markEvent("highSuspicion");
        }
        if (suspicionLevel > 60) {
            updateAlarm(5, "High suspicion triggered alarm");
        }
    }
    // gives us alarm level
    public synchronized int getAlarmLevel() {
        return alarmLevel;
    }

    // updates alarm level
    public synchronized void updateAlarm(int change, String reason) {
        alarmLevel = Math.clamp(alarmLevel + change, 0, 100); // make sure it's in the range
        String sign = change > 0 ? "+" : "";
        System.out.println("[Alarm " + sign + change + "] " + reason);
        if (alarmLevel >= 30) {
            markEvent("alarmTriggered");
        }
    }

    // marks a story event as completed
    public synchronized void markEvent(String flag) {
        storyFlags.put(flag, true);
    }

    // checks if an event has already happened
    public synchronized boolean didHappen(String flag) {
        return storyFlags.getOrDefault(flag, false); // default being false
    }
    // gives us the players current location
    public synchronized String getLocation() {
        return location;
    }

    // updates player location
    // synchronized --> ensures safe access if multiple threads use this class
    public synchronized void setLocation(String location) {
        this.location = location;
    }

    // returns a copy (via keyword new) of the bag -->  prevents outside code from modifying original list
    public synchronized List<String> getBag() {
        return new ArrayList<>(bag);
    }

    // checks if player has a specific item/ made to be case-insensitive
    public synchronized boolean hasItem(String item) {
        for (String current : bag) { // checks if it's in the inventory (bag)
            if (current.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    // adds item to inventory
    public synchronized void addItem(String item) {
        bag.add(item);
    }

    // removes item ignoring case, returns true if removed
    // goes through list to find and remove items that match case-insensitive version of item
    public synchronized boolean removeItem(String item) {
        return bag.removeIf(i -> i.equalsIgnoreCase(item));
    }

    // stores command and increments turn counter
    public synchronized void recordCommand(String cmd) {
        pastCommands.add(cmd);
        turnsPassed++; // every command = 1 turn
    }

    //returns number of turns (or commands)
    public synchronized int getTurnsPassed() {
        return turnsPassed;
    }

    // returns copy of past commands (useful for debugging)
    // copy for safety / prevent modifications from outside
    public synchronized List<String> getPastCommands() {
        return new ArrayList<>(pastCommands);
    }

    // this class stores all important game data
}