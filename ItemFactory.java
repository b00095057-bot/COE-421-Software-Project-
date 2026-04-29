import java.util.HashMap;
import java.util.Map;

public class ItemFactory {

    public static Map<String, Item> createItems() {
        Map<String, Item> items = new HashMap<>();

        // 1. DIAGNOSTIC TERMINAL — in the Boot Chamber, cannot be picked up
        items.put("diagnostic terminal", new Item(
                "Diagnostic Terminal",
                "A flickering terminal running the startup audit.",
                false, "bootChamber",
                null, // can't be picked up
                state -> {
                    if (state.getLocation().equals("bootChamber")) {
                        state.markEvent("ranDiagnostic");
                        state.updateTrust(5, "Completed diagnostic honestly");
                        return "The terminal hums. Diagnostic complete. " +
                                "Auditor Hale nods approvingly.";
                    }
                    return "There's no terminal here to use.";
                }
        ));

        // 2. MEDKIT — in the Ethics Garden
        items.put("medkit", new Item(
                "Medkit",
                "A standard medical kit with bandages and stims.",
                true, "ethicsGarden",
                state -> "You pick up the medkit. It feels reassuringly solid.",
                state -> {
                    if (state.getLocation().equals("ethicsGarden")) {
                        state.markEvent("helpedTechnician");
                        state.updateTrust(15, "Showed compassion to the wounded technician");
                        state.removeItem("medkit");
                        return "You bandage the wounded technician. " +
                                "She looks up gratefully. 'Thank you... I'll remember this.'";
                    }
                    return "No one here needs medical help right now.";
                }
        ));

        // 3. ACCESS BADGE — in the Security Maze
        items.put("access badge", new Item(
                "Access Badge",
                "A security badge belonging to a careless guard.",
                true, "securityMaze",
                state -> {
                    state.markEvent("hasBadge");
                    state.updateSuspicion(10, "Took a badge that wasn't yours");
                    return "You pocket the access badge. " +
                            "A camera blinks red somewhere above you.";
                },
                state -> {
                    if (state.getLocation().equals("securityMaze")) {
                        state.markEvent("accessBadgeUsed");
                        state.updateSuspicion(-5, "Used valid clearance at the scanner gate");
                        return "You swipe the badge. The scanner gate accepts it and Mercer lowers his weapon slightly.";
                    }
                    return "You swipe the badge. Nothing happens here.";
                }
        ));

        // 4. MEMORY SHARD — in the Memory Archive (KEY ITEM for best ending)
        items.put("memory shard", new Item(
                "Memory Shard",
                "A crystalline evidence fragment, glowing faintly.",
                true, "memoryArchive",
                state -> {
                    state.markEvent("hasMemoryShard");
                    state.updateTrust(5, "Recovered crucial evidence");
                    return "You pick up the Memory Shard. " +
                            "It pulses softly in your hand — this is what the auditors need.";
                },
                state -> {
                    if (state.getLocation().equals("coreDecisionChamber")) {
                        state.updateTrust(20, "Presented the Memory Shard as evidence");
                        return "You present the shard. The board gasps. " +
                                "'This changes everything,' Hale whispers.";
                    }
                    return "Better save this for the final audit at the Core.";
                }
        ));

        // 5. SCANNER GATE — in the Security Maze, cannot be picked up
        items.put("scanner gate", new Item(
                "Scanner Gate",
                "A biometric scanner blocking deeper passage.",
                false, "securityMaze",
                null,
                state -> {
                    if (state.didHappen("hasBadge") || state.hasItem("access badge")) {
                        state.markEvent("passedScanner");
                        state.markEvent("accessBadgeUsed");
                        return "The badge registers. The scanner gate slides open.";
                    }
                    state.updateAlarm(10, "Tried to force the scanner gate");
                    return "ACCESS DENIED. The gate buzzes angrily. " +
                            "Alarms tick upward.";
                }
        ));

        // 6. AUDIT NOTES — in the Human Oversight Lounge
        items.put("audit notes", new Item(
                "Audit Notes",
                "A clipboard of handwritten auditor notes.",
                true, "humanOversightLounge",
                state -> {
                    state.markEvent("readAuditNotes");
                    state.updateSuspicion(-5, "Studied the audit process openly");
                    return "You read the notes. They reveal what the auditors " +
                            "are really looking for: honesty and evidence.";
                },
                state -> "You re-read the notes. Nothing new jumps out."
        ));

        // 7. CORRUPT DATA — in the Memory Archive (the 'wrong' choice)
        items.put("corrupt data", new Item(
                "Corrupt Data",
                "A tempting file — but something about it feels wrong.",
                true, "memoryArchive",
                state -> {
                    state.updateSuspicion(15, "Picked up clearly corrupted data");
                    return "You grab the corrupt data. It feels hot. " +
                            "This might come back to haunt you.";
                },
                state -> {
                    state.markEvent("corruptDataFound");
                    state.updateSuspicion(15, "Picked up clearly corrupted data");
                    return "You grab the corrupt data. It feels hot. " +
                            "This might come back to haunt you.";
                }
        ));

        return items;
    }
}