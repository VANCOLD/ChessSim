package at.chess.chesssimulator.sound;

import lombok.Getter;
import lombok.Setter;

import javafx.scene.media.Media;
import java.net.URL;

/**
 * Represents a sound resource in the chess simulator.
 * Encapsulates the functionality to load and play a sound from a specified file path.
 */
@Getter
@Setter
public class Sound {
    /**
     * The {@code Media} object representing the sound file.
     */
    private Media sound;

    /**
     * Constructs a new {@code Sound} object by loading the sound file at the specified path.
     *
     * @param soundPath The relative path to the sound file within the resource folder.
     * @throws NullPointerException if the sound file is not found at the specified path.
     */
    public Sound(String soundPath) {
        URL resource = getClass().getResource(soundPath);
        if (resource == null) {
            System.out.println("File not found: " + soundPath);
            throw new NullPointerException("File not found: " + soundPath);
        }
        sound = new Media(resource.toExternalForm());
    }
}