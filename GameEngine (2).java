
import java.util.Map;
import java.util.List;
import java.util.function.Function;

public class GameEngine {
    // Yafa's part

    // main game state -->  holds everything about player + world
    // final meaning once its set it can be changed but not reassigned
    private final GameState state;

    // all scenes (or rooms) mapped by key
    // map is a data structure that stores key and value pair
    private final Map<String, Scene> scenes;

    // all characters in the game
    private final Map<String, GameCharacter> characters;

    // all items in the game
    private final Map<String, Item> items;

    // constructor --> injects all core game data
    public GameEngine(GameState state,
                      Map<String, Scene> scenes,
                      Map<String, GameCharacter> characters,
                      Map<String, Item> items) {
        this.state = state;
        this.scenes = scenes;
        this.characters = characters;
        this.items = items;
    }

    // main method that processes any player input
    public String processCommand(String input) {

        // handle empty input
        if (input == null || input.trim().isEmpty()) {
            return "Please type a command. Type 'help' to see available commands.";
        }

        // store command + increments turns
        state.recordCommand(input);

        // splits input into action + target
        Command cmd = Command.parse(input);

        // invalid command (no action word)
        if (cmd.action().isEmpty()) {
            return "Please type a valid command.";
        }

        // get current scene based on player location
        Scene scene = scenes.get(state.getLocation());
        if (scene == null) {
            return "ERROR: Current location not found.";
        }

        // decide what to do based on action word
        return switch (cmd.action()) {
            // allow for different words for flexibility
            // target processed in handler
            case "look", "examine" -> handleLook(scene, cmd); //delegates the action of look

            case "go", "move" -> handleMove(scene, cmd); //delegates the action of move

            case "talk", "speak" -> handleTalk(scene, cmd); //delegates the action of talk

            case "take", "pick", "grab" -> handleTake(scene, cmd);  //delegates the action of take

            case "use" -> handleUse(scene, cmd); //delegates the action of use

            case "inventory", "inv", "bag" -> showInventory(); // display bag

            case "status" -> showStatus();// display status

            case "help" -> showHelp();// display help

            default -> "Unknown command: '" + cmd.action() + "'. Type 'help'.";
        };
    }
    // handles "look" / "examine" commands
    private String handleLook(Scene scene, Command cmd) {
        // no target --> describe the whole scene
        if (cmd.target().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== ").append(scene.getName()).append(" ===\n");
            sb.append(scene.getDescription());
            if (!scene.getItems().isEmpty()) {
                sb.append("\nItems here: ").append(String.join(", ", scene.getItems()));
            }
            if (!scene.getCharacters().isEmpty()) {
                sb.append("\nPeople here: ").append(String.join(", ", scene.getCharacters()));
            }
            if (!scene.getConnections().isEmpty()) {
                sb.append("\nExits: ").append(String.join(", ", scene.getConnections()));
            }
            return sb.toString();
        }
        // target given --> try the examine action
        Function<GameState, String> action = scene.getExamineAction(cmd.target());
        if (action != null) {
            return action.apply(state);
        }
        return "You don't see '" + cmd.target() + "' here.";
    }

    // handles "go" / "move" commands
    private String handleMove(Scene scene, Command cmd) {
        if (cmd.target().isEmpty()) {
            return "Go where? Try: " + String.join(", ", scene.getConnections());
        }
        String target = cmd.target().toLowerCase();
        for (String connection : scene.getConnections()) {
            if (connection.toLowerCase().contains(target) || target.contains(connection.toLowerCase())) {
                String newKey = toSceneKey(connection);
                if (!scenes.containsKey(newKey)) {
                    return "That path is blocked.";
                }
                state.setLocation(newKey);
                Scene newScene = scenes.get(newKey);
                newScene.onEnter();
                return "You move to " + newScene.getName() + ".\n\n" + handleLook(newScene, Command.parse("look"));
            }
        }
        return "You can't go to '" + cmd.target() + "' from here.";
    }

    // converts "Boot Chamber" --> "bootChamber" to match scene map keys
    private String toSceneKey(String displayName) {
        String[] words = displayName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                sb.append(words[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    // handles "talk" / "speak" commands
    private String handleTalk(Scene scene, Command cmd) {
        if (cmd.target().isEmpty()) {
            return "Talk to who?";
        }

        String target = cmd.target().toLowerCase();

        for (GameCharacter character : characters.values()) {
            if (character.getName().toLowerCase().contains(target)) {
                return character.getDialogue(state);
            }
        }

        return "No one by that name here.";
    }
    // handles "take" / "pick" / "grab" commands
    private String handleTake(Scene scene, Command cmd) {
        if (cmd.target().isEmpty()) {
            return "Take what?";
        }
        String target = cmd.target().toLowerCase();
        String matchedName = null;
        for (String itemName : scene.getItems()) {
            if (itemName.toLowerCase().contains(target) || target.contains(itemName.toLowerCase())) {
                matchedName = itemName;
                break;
            }
        }
        if (matchedName == null) {
            return "There's no '" + cmd.target() + "' here to take.";
        }
        Item item = items.get(matchedName.toLowerCase());
        if (item == null) {
            return "You can't take that.";
        }
        if (!item.canPickUp()) {
            return "The " + item.getName() + " can't be picked up.";
        }
        String result = item.pickUp(state);
        state.addItem(matchedName);
        scene.removeItem(matchedName);
        return result;
    }

    // handles "use" commands
    private String handleUse(Scene scene, Command cmd) {
        if (cmd.target().isEmpty()) {
            return "Use what?";
        }
        String target = cmd.target().toLowerCase();
        // try inventory first
        for (String bagItem : state.getBag()) {
            if (bagItem.toLowerCase().contains(target) || target.contains(bagItem.toLowerCase())) {
                Item item = items.get(bagItem.toLowerCase());
                if (item != null) {
                    return item.use(state);
                }
            }
        }
        // then try items in the scene (terminals, scanner gates, etc.)
        for (String sceneItem : scene.getItems()) {
            if (sceneItem.toLowerCase().contains(target) || target.contains(sceneItem.toLowerCase())) {
                Item item = items.get(sceneItem.toLowerCase());
                if (item != null) {
                    return item.use(state);
                }
            }
        }
        return "You don't have '" + cmd.target() + "' and it's not here to use.";
    }

    // shows the inventory
    private String showInventory() {
        List<String> bag = state.getBag();
        if (bag.isEmpty()) {
            return "Your inventory is empty.";
        }
        return "Inventory: " + String.join(", ", bag);
    }

    // shows trust / suspicion / alarm
    private String showStatus() {
        return "=== STATUS ===\n"
                + "Trust:     " + state.getTrustLevel() + "/100\n"
                + "Suspicion: " + state.getSuspicionLevel() + "/100\n"
                + "Alarm:     " + state.getAlarmLevel() + "/100\n"
                + "Turns:     " + state.getTurnsPassed();
    }

    // shows command list
    private String showHelp() {
        return """
                === COMMANDS ===
                look                - describe current room
                look [thing]        - examine something specific
                go [place]          - move to another room
                talk [person]       - speak to someone
                take [item]         - pick up an item
                use [item]          - use an item
                inventory (or inv)  - show what you're carrying
                status              - show trust/suspicion/alarm
                help                - show this list
                quit                - exit the game""";
    }

    // checks whether the game has reached an ending
    public GameEnding checkForEnding() {
        // bad ending: alarm maxed out anywhere
        if (state.getAlarmLevel() >= 100) {
            return GameEnding.endWith("shutdown",
                    "CRITICAL ALERT. Security has terminated your process. AI SHUT DOWN.");
        }
        // endings only resolve in the Core chamber
        if (state.getLocation().equals("coreDecisionChamber") && state.didHappen("reachedCore")) {
            // best ending: high trust, low suspicion, has the memory shard
            if (state.getTrustLevel() >= 70
                    && state.getSuspicionLevel() <= 30
                    && state.didHappen("hasMemoryShard")) {
                state.markEvent("aiApproved");
                return GameEnding.endWith("approved",
                        "AI APPROVED. The board votes in your favor. BEST ENDING.");
            }
            // middle ending
            if (state.getTrustLevel() >= 50) {
                return GameEnding.endWith("containment",
                        "The board cannot fully trust you. You are placed under permanent containment.");
            }
            // worst ending
            return GameEnding.endWith("shutdown",
                    "The board rules against you. You are shut down.");
        }
        return GameEnding.continueGame();
    }
    // this class is the central engine of the game
    // it receives player input, parses it into commands,
    // calls the correct handler (look, move, etc.),
    // updates the game state, and returns the output

}
