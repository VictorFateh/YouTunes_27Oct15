import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.*;
import javafx.event.*;
import java.util.List;
import javafx.scene.media.*;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer.Status;

/**
 * Write a description of class mediaControls here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MediaControls
{
    Library trackList;
    private MediaPlayer mp;
    private MediaView mediaView;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private List<MediaPlayer> playList;
    private int playListLength;
    
    //passing in the library because we need to be able to act on an object
    public MediaControls(Library library)
    {
        trackList = library;
        playList = library.getMediaPlayers();
        playListLength = playList.size();
    }

    /**
     * Creates an HBox (horizontal box) with buttons for the center region
     */
    public HBox addHBox()
    {
        //padding and spacing for buttons
        final int PAD_WIDTH = 15;
        final int PAD_HEIGHT = 12;
        final int SPACING = 10;
        //hbox background color
        final String HBOX_BACKGROUND_COLOR = "-fx-background-color: #bfc2c7;";//grey

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(PAD_WIDTH, PAD_HEIGHT, PAD_WIDTH, PAD_HEIGHT));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle(HBOX_BACKGROUND_COLOR);//sets the hbox to grey

        // Add Time label
        Label timeLabel = new Label("Time: ");

        // Add the volume label
        Label volumeLabel = new Label("Vol: ");

        hbox.getChildren().addAll(backButton(), playButton(), nextButton(), timeLabel,
            timeSlider(), volumeLabel, volumeSlider());

        return hbox;
    }//method

    //create back button
    private Button backButton()
    {
        Button backButton = new Button("Back");
        return backButton;
    }//method

    //create play button
    private Button playButton()
    {

        Button playButton = new Button("Play");

        // play an audio file
        playButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {

                    playList.get(0).play();
                    
                    Status status = playList.get(0).getStatus();
                    if (status == Status.UNKNOWN || status == Status.HALTED) {
                        // don't do anything in these states
                        return;
                    }

                    if (status == Status.PAUSED
                    || status == Status.READY
                    || status == Status.STOPPED) {
                        // rewind the movie if we're sitting at the end
                        if (atEndOfMedia) {
                            mp.seek(mp.getStartTime());
                            atEndOfMedia = false;
                        }
                        playList.get(0).play();
                        playButton.setText("Pause");
                    } else {
                        playList.get(0).pause();
                        playButton.setText("Play");
                    }
                }
            });
        return playButton;
    }//method

    //create next button
    private Button nextButton()
    {

        Button nextButton = new Button("next");

        // allow the user to skip a track.
        /*nextButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
        final MediaPlayer curPlayer = mediaView.getMediaPlayer();
        curPlayer.currentTimeProperty().removeListener(progressChangeListener);
        curPlayer.getMedia().getMetadata().removeListener(metadataChangeListener);
        curPlayer.stop();

        MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
        mediaView.setMediaPlayer(nextPlayer);
        nextPlayer.play();
        }
        });*/
        return nextButton;
    }//method

    //make a time slider to show progress of the song
    private Slider timeSlider()
    {

        //time slider width
        final int MIN_TSLIDER_WIDTH = 50;

        // Add time slider
        Slider timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(MIN_TSLIDER_WIDTH);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (timeSlider.isValueChanging()) {
                        //multiply duration by percentage calculated by slider position
                        //mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                    }
                }
            });//listener

        return timeSlider;
    }//method

    //make a volume bar
    private Slider volumeSlider()
    {
        //volume slider widths
        final int PREF_VSLIDER_WIDTH = 70;
        final int MIN_VSLIDER_WIDTH = 30;

        Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(PREF_VSLIDER_WIDTH);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(MIN_VSLIDER_WIDTH);

        return volumeSlider;
    }//method

}
