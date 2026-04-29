import java.util.Map;
import java.util.Scanner;

/**
 * Main.java — the entry point for System Override: AI Under Audit.
 *
 * This file assembles all the pieces built by the team:
 *  - GameState (Yafa)
 *  - Scenes (Ahmad Yehia)
 *  - Characters (Divyansh)
 *  - Items (Omar)
 *
 * It also launches the EntityBehaviorThread for multi-threaded background behavior.
 */
public class Main {

    public static void main(String[] args) {
        // ===== 1. Build the game world =====
        GameState state = new GameState();
        Map<String, Scene> scenes = SceneFactory.createScenes(state);
        Map<String, GameCharacter> characters = CharacterFactory.createCharacters();
        Map<String, Item> items = ItemFactory.createItems();

        GameEngine engine = new GameEngine(state, scenes, characters, items);

        // ===== 2. Start background thread (multi-threading requirement) =====
        EntityBehaviorThread behavior = new EntityBehaviorThread(state);
        Thread bgThread = new Thread(behavior);
        bgThread.setDaemon(true); // dies automatically when main thread ends
        bgThread.start();

        // ===== 3. Welcome screen =====
        System.out.println("============================================");
        System.out.println("       SYSTEM OVERRIDE: AI UNDER AUDIT       ");
        System.out.println("============================================");
        System.out.println();
        System.out.println("You are an AI being audited by humans.");
        System.out.println("Your fate depends on your choices.");
        System.out.println();
        System.out.println("Type 'help' for a list of commands.");
        System.out.println("Type 'quit' to exit.");
        System.out.println();

        // ===== 4. Main game loop =====
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue; // skip blank lines
            }

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting audit. Goodbye.");
                running = false;
                continue;
            }

            // Send command to the engine and print the result
            String result = engine.processCommand(input);
            System.out.println(result);

            // Check if the game has ended (win/loss condition)
            GameEnding ending = engine.checkForEnding();
            if (ending.hasEnded()) {
                System.out.println();
                System.out.println("============================================");
                System.out.println("GAME OVER — Ending: " + ending.getEndingType());
                System.out.println(ending.getMessage());
                System.out.println("============================================");
                running = false;
            }
        }

        // ===== 5. Cleanup =====
        behavior.stop();
        scanner.close();
    }
}