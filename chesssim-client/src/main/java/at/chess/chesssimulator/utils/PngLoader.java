package at.chess.chesssimulator.utils;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static at.chess.chesssimulator.utils.Constants.IMAGE_FOLDERS_TO_CHECK;

/**
 * A singleton utility class responsible for loading and managing image resources in the chess simulator.
 * <p>
 * This class provides a mechanism to load PNG and JPEG images from predefined folders and retrieve them
 * using a unique key. It ensures that images are only loaded once, improving performance and resource management.
 * </p>
 */
public class PngLoader {

    protected static final Logger logger = LoggerFactory.getLogger(PngLoader.class);

    /** The singleton instance of the PngLoader. */
    private static PngLoader instance;

    /** A hashmap storing loaded images, with their filenames as keys. */
    private final HashMap<String, Image> loadedImages;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the hashmap for storing loaded images and calls the method to load images.
     */
    private PngLoader() {
        this.loadedImages = new HashMap<>();
        this.loadImages();
    }

    /**
     * Retrieves the singleton instance of the PngLoader.
     * <p>
     * If the instance is null, a new PngLoader is created. This ensures that only one instance of the class exists.
     * </p>
     *
     * @return The singleton instance of PngLoader.
     */
    public static PngLoader getInstance() {
        if (instance == null) {
            instance = new PngLoader();
        }
        return instance;
    }

    /**
     * Loads images from the specified folders into memory.
     * <p>
     * This method searches for image files (PNG, JPG, JPEG) in the folders defined in
     * {@link Constants#IMAGE_FOLDERS_TO_CHECK} and adds them to the {@link #loadedImages} map.
     * </p>
     */
    private void loadImages() {
        logger.info("Trying to load images from {}", Arrays.toString(IMAGE_FOLDERS_TO_CHECK));
        List<URL> imageUrls = Arrays.stream(IMAGE_FOLDERS_TO_CHECK)
                .map(folder -> getClass().getClassLoader().getResource(folder))
                .filter(Objects::nonNull)  // Filter out null values if a resource is not found
                .toList();

        for (URL url : imageUrls) {
            logger.info("Checking for images in the folder {}", url.toString());

            File folder = new File(url.getFile());
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));

            if (files == null) {
                logger.warn("No images found in the folder {}", folder.getName());
                continue;
            }

            for (File file : files) {
                String imagePath = file.toURI().toString();
                Image image = new Image(imagePath);
                this.loadedImages.put(file.getName(), image);
                logger.debug("Loaded key-value pair into loaded images - {}", file.getName());
            }
        }
    }

    /**
     * Retrieves an image based on the provided key arguments.
     * <p>
     * The arguments are concatenated to form the key used to retrieve the image from the loaded images.
     * </p>
     *
     * @param arguments The parts of the key for retrieving the corresponding image.
     * @return The image associated with the constructed key, or null if no image is found.
     */
    public Image getImage(String... arguments) {
        logger.debug("Trying to get Image for key {}", String.join("", arguments));
        return this.loadedImages.get(String.join("", arguments));
    }
}
