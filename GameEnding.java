public class GameEnding {
    // Yafa's part
    // flag to check if the game has finished or not
    // final --> value is set once and cannot change after initialization
    private final boolean ended;

    // type of ending --> could be "approved", "containment", "shutdown"
    // helps distinguish how the game ended
    private final String endingType;

    // message to display to the player when game ends
    private final String message;

    // private constructor --> prevents creating objects directly using "new" from outside the class
    // this forces all object creation to go through the static methods (continueGame / endWith)
    // ensures only valid game states are created (avoids missing type/message bugs)
    // also makes the code more readable and controlled (kind of like a "safe builder")
    private GameEnding(boolean ended, String endingType, String message) {
        this.ended = ended;
        this.endingType = endingType;
        this.message = message;
    }

    // used when the game is still running
    // static method --> belong to class not object / can be there with no object
    public static GameEnding continueGame() {
        // ended = false  --> game not finished yet
        // no type/message needed
        return new GameEnding(false, null, null);
    }

    // static method --> used when the game ends
    public static GameEnding endWith(String type, String msg) {
        // ended = true --> game finished
        // store ending type and message for later use
        return new GameEnding(true, type, msg);
    }

    // checks if the game has ended
    public boolean hasEnded() {
        return ended;
    }

    // returns the type of ending (can be used for logic or display)
    public String getEndingType() {
        return endingType;
    }

    // returns the message shown to the player (for verification)
    public String getMessage() {
        return message;
    }

    // this class is for terminating the game purpose.
}