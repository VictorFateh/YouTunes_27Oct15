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
    private int currentTrack = 0;

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

        // allow the user to go back a track.
        backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    //final MediaPlayer curPlayer = mediaView.getMediaPlayer();
                    //curPlayer.currentTimeProperty().removeListener(progressChangeListener);
                    //curPlayer.getMedia().getMetadata().removeListener(metadataChangeListener);
                    playList.get(currentTrack).stop();
                    //go back one track in the playlist, go to end if at beginning of playlist
                    if (currentTrack == 0)
                    {
                        currentTrack = (playListLength - 1);
                    }
                    else
                    {
                        currentTrack -= 1;
                    }
                    playList.get(currentTrack).play();
                }
            });

        return backButton;
    }//method

    //create play button
    private Button playButton()
    {

        Button playButton = new Button("Play");

        // play an audio file
        playButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {

                    Status status = playList.get(currentTrack).getStatus();
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
                        playList.get(currentTrack).play();
                        playButton.setText("Pause");
                    } else {
                        playList.get(currentTrack).pause();
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
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    //final MediaPlayer curPlayer = mediaView.getMediaPlayer();
                    //curPlayer.currentTimeProperty().removeListener(progressChangeListener);
                    //curPlayer.getMedia().getMetadata().removeListener(metadataChangeListener);
                    playList.get(currentTrack).stop();
                    //advance one track in the playlist, go to beginning if at end of playlist
                    if (currentTrack == (playListLength - 1))
                    {
                        currentTrack = 0;
                    }
                    else
                    {
                        currentTrack += 1;
                    }
                    playList.get(currentTrack).play();
                }
            });
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
                        playList.get(currentTrack).seek(duration.multiply(timeSlider.getValue() / 100.0));
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

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (volumeSlider.isValueChanging()) {
                        playList.get(currentTrack).setVolume(volumeSlider.getValue() / 100.0);
                    }
                }
            });

        return volumeSlider;
    }//method

}
