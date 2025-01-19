package communication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Command {

    private CommandType commandType;
    private String message;

    public Command(CommandType commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }
}
