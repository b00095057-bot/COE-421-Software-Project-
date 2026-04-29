import java.util.function.Function;

public class Item {
    private final String name;
    private final String description;
    private final boolean canPickUp;
    private final String scene; // where it's found
    private final Function<GameState, String> onPickUp;
    private final Function<GameState, String> onUse;

    public Item(String name, String description,
                boolean canPickUp, String scene,
                Function<GameState, String> onPickUp,
                Function<GameState, String> onUse) {
        this.name = name;
        this.description = description;
        this.canPickUp = canPickUp;
        this.scene = scene;
        this.onPickUp = onPickUp;
        this.onUse = onUse;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean canPickUp() { return canPickUp; }
    public String getScene() { return scene; }

    public String pickUp(GameState state) {
        if (onPickUp != null) {
            return onPickUp.apply(state);
        }
        return "You picked up: " + name;
    }

    public String use(GameState state) {
        if (onUse != null) {
            return onUse.apply(state);
        }
        return "You can't use " + name + " here.";
    }
}