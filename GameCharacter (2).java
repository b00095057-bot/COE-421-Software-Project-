import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GameCharacter.java
 * Person 3 — Divyansh
 * COE 421L — System Override: AI Under Audit
 * <p>
 * Represents an NPC (non-player character) in the game.
 * Each character has:
 *   - A default line of dialogue (fallback)
 *   - Scene-specific dialogue (changes based on WHERE the player is)
 *   - Conditional dialogue (changes based on WHAT HAS HAPPENED in the story)
 *   - A mood that other systems can read or set
 * <p>
 * Dialogue priority (highest → lowest):
 *   1. Conditional dialogue  (story flags override everything)
 *   2. Scene dialogue        (location-based lines)
 *   3. Default dialogue      (generic fallback)
 */
public class GameCharacter {

    // ---------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------

    private final String name;
    private final String defaultDialogue;

    /**
     * Key   = scene ID (lowercase), e.g. "bootchamber"
     * Value = what this character says when the player is there
     */
    private final Map<String, String> sceneDialogue;

    /**
     * Key   = story flag name, e.g. "reachedCore"
     * Value = what this character says once that flag is set
     * LinkedHashMap preserves insertion order so earlier entries
     * (higher priority flags) are checked first.
     */
    private final Map<String, String> conditionalDialogue;

    /** Mood tag — can be read by GameEngine or EntityBehaviorThread */
    private String mood;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Creates a new character with a name and a default line.
     *
     * @param name            Display name shown to the player
     * @param defaultDialogue Fallback line when no other condition matches
     */
    public GameCharacter(String name, String defaultDialogue) {
        this.name             = name;
        this.defaultDialogue  = defaultDialogue;
        this.sceneDialogue    = new HashMap<>();
        this.conditionalDialogue = new LinkedHashMap<>(); // order matters!
        this.mood             = "neutral";
    }

    // ---------------------------------------------------------------
    // Builder-style setters (return 'this' so calls can be chained)
    // ---------------------------------------------------------------

    /**
     * Adds a line of dialogue for a specific scene.
     * <p>
     * Example:
     *   character.addSceneDialogue("ethicsGarden", "Be careful here.");
     *
     * @param scene    The scene ID (case-insensitive)
     * @param dialogue What the character says in that scene
     * @return this (for method chaining)
     */
    public GameCharacter addSceneDialogue(String scene, String dialogue) {
        sceneDialogue.put(scene.toLowerCase(), dialogue);
        return this;
    }

    /**
     * Adds a line of dialogue that fires once a story flag is TRUE.
     * Flags are set via GameState.markEvent(String flag).
     * <p>
     * Add higher-priority flags FIRST because LinkedHashMap preserves order.
     *
     * @param flag     The story flag name (must match GameState.markEvent key)
     * @param dialogue What the character says after that event
     * @return this (for method chaining)
     */
    public GameCharacter addConditionalDialogue(String flag, String dialogue) {
        conditionalDialogue.put(flag, dialogue);
        return this;
    }

    // ---------------------------------------------------------------
    // Getters and mood
    // ---------------------------------------------------------------

    /** @return Display name of this character */
    public String getName() { return name; }

    /**
     * Sets the character's mood.
     * Can be called by GameEngine when trust/suspicion thresholds are crossed.
     *
     * @param mood New mood string
     */
    public void setMood(String mood) { this.mood = mood; }

    // ---------------------------------------------------------------
    // Core dialogue method
    // ---------------------------------------------------------------

    /**
     * Returns the most relevant line of dialogue for the current game state.
     * <p>
     * Priority order:
     *   1. Conditional (story flag) dialogue   — checked first, in insertion order
     *   2. Scene dialogue                       — based on current location
     *   3. Default dialogue                     — fallback
     * <p>
     * The returned string is already formatted as:
     *   "[CharacterName] says: "line here""
     *
     * @param state The live GameState so we can check flags and location
     * @return Formatted dialogue string ready to print
     */
    public String getDialogue(GameState state) {

        // 1. Check conditional (story-flag) dialogue first
        for (Map.Entry<String, String> entry : conditionalDialogue.entrySet()) {
            if (state.didHappen(entry.getKey())) {
                return formatLine(entry.getValue());
            }
        }

        // 2. Check scene-specific dialogue
        String currentScene = state.getLocation().toLowerCase();
        if (sceneDialogue.containsKey(currentScene)) {
            return formatLine(sceneDialogue.get(currentScene));
        }

        // 3. Fall back to default
        return formatLine(defaultDialogue);
    }

    // ---------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------

    /**
     * Wraps a dialogue line with the character's name.
     *
     * @param line Raw dialogue string
     * @return Formatted: "Name says: "line""
     */
    private String formatLine(String line) {
        return name + " says: \"" + line + "\"";
    }
}
