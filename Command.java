/**
 * @param action Yafa's part main command words like "go", "take", "look"
 * @param target the rest of the command like "north", "key", etc.
 */
public record Command(String action, String target) {
    // constructor --> initializes all parts of the command
    // full cleaned input after editing it -->lowercase + trimmed version
    // trimming is needed to remove unnecessary spacing
    // for history session and replaying

    // static method --> converts raw user input into a Command object
    public static Command parse(String input) {

        // if input is null or just spaces --> return empty command (prevents crashes)
        if (input == null || input.trim().isEmpty()) {
            return new Command("", "");
        }

        // clean input --> lowercase + remove extra spaces at start/end
        String cleaned = input.toLowerCase().trim();

        // split words and store them in a string array
        String[] words = cleaned.split("\\s+");

        // first word being  action
        String action = words[0];

        // initialize target as empty in case there's only one word
        String target = "";

        // if more than one word exists --> extract everything after action
        if (words.length > 1) {

            // substring used instead of joining words again --> keeps original spacing logic simple
            // substring meaning a part of a larger string/ captures everything after action
            // trim to remove extra spacing
            target = cleaned.substring(action.length()).trim();
        }

        // return new Command object with altered values
        return new Command(action, target);
    }
    // this class serves as a command extractor from user input hence the name.
}
