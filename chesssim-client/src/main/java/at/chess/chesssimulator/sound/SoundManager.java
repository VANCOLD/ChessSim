package at.chess.chesssimulator.sound;

import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Manages the loading and playback of sound effects in the chess simulator.
 * Implements a singleton pattern to ensure a single instance manages all sounds.
 */
public class SoundManager {

    /**
     * Logger instance for the {@code SoundManager}.
     */
    protected static final Logger logger = LoggerFactory.getLogger(SoundManager.class);

    /**
     * A map storing the association between {@link SoundType} and their corresponding {@link Sound} objects.
     */
    private final HashMap<SoundType, Sound> sounds;

    /**
     * The singleton instance of {@code SoundManager}.
     */
    @Getter
    private static final SoundManager instance = new SoundManager();

    /**
     * Private constructor to initialize the {@code SoundManager}.
     * Loads and maps all predefined sound effects to their corresponding {@link SoundType}.
     */
    private SoundManager() {
        logger.info("Creating SoundManager");
        sounds = new HashMap<>();

        sounds.put(SoundType.GAME_START, new Sound("/sounds/game-start.mp3"));
        sounds.put(SoundType.MOVE, new Sound("/sounds/move-self.mp3"));
        sounds.put(SoundType.CAPTURE, new Sound("/sounds/capture.mp3"));
        sounds.put(SoundType.PROMOTE, new Sound("/sounds/promote.mp3"));
        sounds.put(SoundType.CASTLE, new Sound("/sounds/castle.mp3"));
        sounds.put(SoundType.CHECKMATE, new Sound("/sounds/checkmate.mp3"));
        sounds.put(SoundType.CHECK, new Sound("/sounds/move-check.mp3"));

        logger.info("Successfully loaded the following sounds: {}",
                String.join(", ", sounds.keySet().stream().map(SoundType::name).toList()));
    }

    /**
     * Plays the sound effect associated with the given {@link SoundType}.
     *
     * @param soundType The type of sound to play.
     * @throws IllegalArgumentException if the specified {@code soundType} is not found in the sound map.
     */
    public void playSound(SoundType soundType) {
        if (sounds.get(soundType) == null) {
            logger.error("Sound not found: {}", soundType.name());
            return;
        }

        logger.debug("Playing sound: {}", soundType.name());
        new MediaPlayer(sounds.get(soundType).getSound()).play();
    }
}