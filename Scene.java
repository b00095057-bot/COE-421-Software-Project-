import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Scene {
    private final String name;
    private final String description;
    private final List<String> items;
    private final List<String> characters;
    private final List<String> connections;
    private final Map<String, Function<GameState, String>> examineActions;
    private Runnable onEnterAction;

    public Scene(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.characters = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.examineActions = new HashMap<>();
    }
    // --- Builder methods (chainable) ---

    public Scene addItem(String item) {
        items.add(item);
        return this;
    }

    public Scene addCharacter(String character) {
        characters.add(character);
        return this;
    }

    public Scene addConnection(String connection) {
        connections.add(connection);
        return this;
    }

    public Scene addExamine(String target, Function<GameState, String> action) {
        examineActions.put(target.toLowerCase(), action);
        return this;
    }

    public Scene setOnEnter(Runnable action) {
        this.onEnterAction = action;
        return this;
    }
    // --- Getter methods ---

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getItems() {
        return items;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public List<String> getConnections() {
        return connections;
    }
    // --- Smart examine: supports short AND full names ---
    // e.g. player types "terminal" and it matches "diagnostic terminal"
    public Function<GameState, String> getExamineAction(String target) {
        String key = target.toLowerCase();
        if (examineActions.containsKey(key)) {
            return examineActions.get(key);
        }
        // Try partial matching (e.g. "terminal" matches "diagnostic terminal")
        for (Map.Entry<String, Function<GameState, String>> entry : examineActions.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    // --- Run the onEnter action if one was set ---
    public void onEnter() {
        if (onEnterAction != null) {
            onEnterAction.run();
        }
    }

    // --- Remove an item from the scene (e.g. when player picks it up) ---
    public boolean removeItem(String item) {
        return items.removeIf(i -> i.equalsIgnoreCase(item));
    }
}