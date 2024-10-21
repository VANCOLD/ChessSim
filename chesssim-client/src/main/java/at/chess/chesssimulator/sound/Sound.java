package at.chess.chesssimulator.sound;

import lombok.Getter;
import lombok.Setter;

import javafx.scene.media.Media;
import java.net.URL;

@Getter
@Setter
public class Sound {
    private Media sound;

    public Sound(String soundPath) {
        URL resource = getClass().getResource(soundPath);
        if (resource == null) {
            System.out.println("File not found: " + soundPath);
            throw new NullPointerException("File not found: " + soundPath);
        }
        sound = new Media(resource.toExternalForm());
    }
}
