package communication;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a command with a specific type and an associated message.
 * <p>
 * The {@code Command} class is used to encapsulate a command's type (defined by {@link CommandType})
 * and an optional message that provides additional context or data for the command.
 * </p>
 *
 * <p>
 * This class uses Lombok annotations {@code @Getter} and {@code @Setter} to automatically generate
 * getter and setter methods for the fields.
 * </p>
 *
 * @see CommandType
 */
@Getter
@Setter
public class Command {

    /**
     * The type of the command. It defines the category or action the command represents.
     */
    private CommandType commandType;

    /**
     * The message associated with the command. This could contain additional data or instructions
     * related to the command.
     */
    private String message;

    /**
     * Constructs a new {@code Command} with the specified type and message.
     *
     * @param commandType the type of the command, which determines its action or category.
     * @param message the message associated with the command, which provides context or data.
     */
    public Command(CommandType commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }
}
