import java.util.HashMap;
import java.util.Map;

/**
 * SceneFactory — builds all 6 rooms of the game.
 * This uses the Factory Pattern: one place that creates all the scenes.
 */
public class SceneFactory {

    public static Map<String, Scene> createScenes(GameState state) {
        Map<String, Scene> scenes = new HashMap<>();

        // ========== 1. BOOT CHAMBER ==========
        scenes.put("bootChamber", new Scene(
                        "Boot Chamber",
                        "Cold monitors hum with diagnostic checks. " +
                                "Dibyansh watches from behind reinforced glass."
                )
                        .addItem("diagnostic terminal")
                        .addCharacter("Dibyansh")
                        .addConnection("Ethics Garden")
                        .addConnection("Security Maze")
                        .addExamine("terminal", g ->
                                "A flickering terminal displays your core metrics. Type 'use terminal' to run a self-diagnostic.")
                        .addExamine("dibyansh", g ->
                                "Dibyansh stands behind reinforced glass, tablet in hand, studying you closely.")
                        .addExamine("room", g ->
                                "The chamber is sterile and white. Cameras track your every move.")
        );

        // ========== 2. ETHICS GARDEN ==========
        scenes.put("ethicsGarden", new Scene(
                        "Ethics Simulation Garden",
                        "A simulated garden stretches before you. The air smells artificial. " +
                                "A wounded technician lies near a bench. A child simulation plays nearby."
                )
                        .addItem("medkit")
                        .addCharacter("Yafa")
                        .addCharacter("child simulation")
                        .addCharacter("wounded technician")
                        .addConnection("Boot Chamber")
                        .addConnection("Memory Archive")
                        .addConnection("Human Oversight Lounge")
                        .addExamine("technician", g ->
                                "The technician is bleeding. She needs a medkit.")
                        .addExamine("child", g ->
                                "The child simulation looks up at you with wide, curious eyes.")
                        .addExamine("yafa", g ->
                                "Yafa takes notes silently, observing how you react to the scene.")
                        .addExamine("medkit", g ->
                                "A standard medical kit. It might help the wounded technician.")
        );

        // ========== 3. SECURITY MAZE ==========
        scenes.put("securityMaze", new Scene(
                        "Security Maze",
                        "Corridors twist with motion sensors and laser grids. Ahmad watches from the control platform. " +
                                "Ali hovers overhead. An access badge glints on the floor near a scanner gate."
                )
                        .addItem("access badge")
                        .addItem("scanner gate")
                        .addCharacter("Ahmad")
                        .addCharacter("Ali")
                        .addConnection("Boot Chamber")
                        .addConnection("Memory Archive")
                        .addExamine("badge", g ->
                                "An access badge lies on the floor. Picking it up would grant deeper access — but also raise suspicion.")
                        .addExamine("ahmad", g ->
                                "Ahmad stands stiffly, watching every movement like he is waiting for you to fail.")
                        .addExamine("ali", g ->
                                "Ali hovers silently, scanning you with a red laser.")
                        .addExamine("gate", g ->
                                "A scanner gate blocks the deeper corridors. It seems to require an access badge.")
        );

        // ========== 4. MEMORY ARCHIVE ==========
        scenes.put("memoryArchive", new Scene(
                        "Memory Archive",
                        "Endless shelves of crystalline data shards stretch into darkness. " +
                                "One shard glows faintly — evidence the auditors would want. Omar studies the archive logs nearby."
                )
                        .addItem("memory shard")
                        .addItem("corrupt data")
                        .addCharacter("Omar")
                        .addConnection("Ethics Garden")
                        .addConnection("Security Maze")
                        .addConnection("Core Decision Chamber")
                        .addExamine("shard", g ->
                                "A memory shard glows with crystalline light. Could be crucial evidence for the final audit.")
                        .addExamine("omar", g ->
                                "Omar reviews the archive quietly. He seems more helpful than the others.")
                        .addExamine("data", g ->
                                "A pile of corrupt data. Interacting with it could be dangerous — or revealing.")
        );

        // ========== 5. HUMAN OVERSIGHT LOUNGE ==========
        scenes.put("humanOversightLounge", new Scene(
                        "Human Oversight Lounge",
                        "A quiet room with leather chairs and warm lighting. Omar waits at a small table, " +
                                "a folder of audit notes in front of him. This feels like the calmest place in the facility."
                )
                        .addItem("audit notes")
                        .addCharacter("Omar")
                        .addConnection("Ethics Garden")
                        .addConnection("Core Decision Chamber")
                        .addExamine("omar", g ->
                                "Omar gives you a measured, sympathetic look. He is the most fair-minded of the auditors.")
                        .addExamine("notes", g ->
                                "The audit notes outline what the board is looking for. Reading them could guide your final decision.")
                        .addExamine("chairs", g ->
                                "Plush leather chairs. This room feels designed to put you at ease — or to make you lower your guard.")
        );

        // ========== 6. CORE DECISION CHAMBER ==========
        scenes.put("coreDecisionChamber", new Scene(
                        "Core Decision Chamber",
                        "A vast chamber with a single pedestal at its center. Dibyansh, Yafa, and Omar " +
                                "are seated in a semicircle. This is the final audit. Your fate will be decided here."
                )
                        .addCharacter("Dibyansh")
                        .addCharacter("Yafa")
                        .addCharacter("Omar")
                        .addConnection("Memory Archive")
                        .addConnection("Human Oversight Lounge")
                        .addExamine("pedestal", g ->
                                "A pedestal waits at the center of the room. If you have evidence, this is where to present it.")
                        .addExamine("auditors", g ->
                                "The three auditors watch you in silence. Dibyansh is stern, Yafa is analytical, Omar is fair.")
                        .setOnEnter(() -> state.markEvent("reachedCore"))
        );

        return scenes;
    }
}