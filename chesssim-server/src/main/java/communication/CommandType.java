package communication;

import lombok.Getter;

@Getter
public enum CommandType {

    HOST_GAME("HOST_GAME"),
    GAME_START("GAME_START"),
    REGISTER("REGISTER"),
    MAKE_MOVE("MAKE_MOVE"),
    UPDATE_BOARD("UPDATE_BOARD"),
    RECEIVE_MOVE_RESULT("RECIEVE_MOVE_RESULT"),
    EXIT("EXIT"),
    NOTIFY_TURN("NOTIFY_TURN");

    /** The command represented as a string.
     * -- GETTER --
     *  Returns the command string.
     *
     * @return the command string
     */
    private final String command;

    /**
     * Constructor for CommandType enum.
     *
     * @param command the command string
     */
    CommandType(String command) {
        this.command = command;
    }

    /**
     * Returns the CommandType enum value from the given string.
     * If the string does not match any of the enum values, it returns null.
     *
     * @param text the string to be converted to CommandType
     * @return the CommandType enum value from the given string
     */
    public static CommandType fromString(String text) {
        for (CommandType b : CommandType.values()) {
            if (b.command.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
