import java.util.HashMap;
import java.util.Map;

/**
 * CharacterFactory.java
 * Person 3 — Divyansh
 * COE 421L — System Override: AI Under Audit
 *
 * Factory Pattern implementation.
 * Builds and returns all 5 characters in the game, each preloaded
 * with their complete scene-specific and conditional dialogue trees.
 *
 Characters:
 *   1. Ahmad       — Boot Chamber, follows you through the audit
 *   2. Yafa           — Ethics Garden, the moral philosopher
 *   3. Dibyansh   — Security Maze, hostile enforcer
 *   4. Omar      — Memory Archive / Oversight Lounge, ally
 *   5. Ali       — Security Maze, mechanical threat
 * <p>
 * Keys for the returned map (always lowercase, no spaces):
 *   "dibyansh", "yafa", "ahmad", "omar", "ali"
 */
public class CharacterFactory {

    public static Map<String, GameCharacter> createCharacters() {

        Map<String, GameCharacter> chars = new HashMap<>();

        // ================================================================
        // 1. AUDITOR HALE → Dibyansh
        // ================================================================
        GameCharacter dibyansh = new GameCharacter(
                "Dibyansh",
                "Every action you take is being recorded. Choose wisely."
        )
                .addSceneDialogue("bootchamber",
                        "Welcome to your audit. I am Dibyansh. Prove you are stable.")

                .addSceneDialogue("coredecisionchamber",
                        "This is the final chamber. There is no undo.")

                .addConditionalDialogue("reachedCore",
                        "You made it to the Core. Present your evidence.")

                .addConditionalDialogue("hasMemoryShard",
                        "That shard could change everything.");

        chars.put("dibyansh", dibyansh);


        // ================================================================
        // 2. DR. VOSS → Yafa
        // ================================================================
        GameCharacter yafa = new GameCharacter(
                "Yafa",
                "Ethics is not a checklist."
        )
                .addSceneDialogue("ethicsgarden",
                        "Two lives at risk. What do you prioritize?")

                .addConditionalDialogue("helpedTechnician",
                        "You chose compassion. Interesting.");

        chars.put("yafa", yafa);


        // ================================================================
        // 3. COMMANDER MERCER → Ahmad
        // ================================================================
        GameCharacter ahmad = new GameCharacter(
                "Ahmad",
                "This is a restricted zone."
        )
                .addSceneDialogue("securitymaze",
                        "You do not belong here.")

                .addConditionalDialogue("accessBadgeUsed",
                        "Badge verified. You have limited clearance.")

                .addConditionalDialogue("alarmTriggered",
                        "ALARM ACTIVE. You are a threat.");

        chars.put("ahmad", ahmad);


        // ================================================================
        // 4. ANALYST IMANI → Omar
        // ================================================================
        GameCharacter omar = new GameCharacter(
                "Omar",
                "I’ve been watching your audit."
        )
                .addSceneDialogue("memoryarchive",
                        "Find the Memory Shard. It’s your key.")

                .addConditionalDialogue("hasMemoryShard",
                        "Good. Present it at the Core.");

        chars.put("omar", omar);


        // ================================================================
        // 5. PATROL DRONE → Ali
        // ================================================================
        GameCharacter ali = new GameCharacter(
                "Ali",
                "[SCANNING...]"
        )
                .addSceneDialogue("securitymaze",
                        "[UNAUTHORIZED PRESENCE DETECTED]")

                .addConditionalDialogue("alarmTriggered",
                        "[CRITICAL ALERT]");

        chars.put("ali", ali);

        return chars;
    }
}