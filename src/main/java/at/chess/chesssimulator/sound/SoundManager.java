package at.chess.chesssimulator.sound;

import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SoundManager {

    protected static final Logger logger = LoggerFactory.getLogger(SoundManager.class);
    private final HashMap<SoundType, Sound> sounds;

    @Getter
    private static final SoundManager instance = new SoundManager();

    private SoundManager() {

        logger.info("Creating SoundManager");
        sounds = new HashMap<>();

        sounds.put(SoundType.MOVE, new Sound("/sounds/move-self.mp3"));
        sounds.put(SoundType.CAPTURE, new Sound("/sounds/capture.mp3"));
        sounds.put(SoundType.PROMOTE, new Sound("/sounds/promote.mp3"));
        sounds.put(SoundType.CASTLE, new Sound("/sounds/castle.mp3"));
        sounds.put(SoundType.CHECKMATE, new Sound("/sounds/move-check.mp3"));

        logger.info("Successfully loaded the following sounds: {}", String.join(", ", sounds.keySet().stream().map(SoundType::name).toList()));
    }

    public void playSound(SoundType soundType) {
        logger.debug("Playing sound: {}", soundType.name());
        new MediaPlayer(sounds.get(soundType).getSound()).play();
    }

}
