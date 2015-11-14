import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * Track class captures the media information and the metadata for each track
 */
public class Song
{
    // metadata
    private String artist;
    private String album; 
    private String title;
    private String year;
    private ImageView albumCover;
    private Media media;
    private MediaPlayer mediaPlayer;

    public void createMedia(String path) {
        try {
            media = new Media(new File(path).toURI().toString());

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(new Runnable() {
                    @Override
                    public void run() {
                        final String errorMessage = media.getError().getMessage();
                        // Handle errors during playback
                        System.out.println("MediaPlayer Error: " + errorMessage);
                    }
                });

            mediaPlayer.play();

        } catch (RuntimeException re) {
            // Handle construction errors
            System.out.println("Caught Exception: " + re.getMessage());
        }
    }
    
}
