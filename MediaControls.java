import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.*;
import javafx.event.*; 
import java.util.List;
import javafx.scene.media.*;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer.Status;
import javafx.application.Platform;

/**
 * Write a description of class mediaControls here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MediaControls
{
    Library trackList;
    private MediaView mediaView;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private List<MediaPlayer> playList;
    private int playListLength;
    private int currentTrack = 0;
    private Label playTime;
    Slider timeSlider = new Slider();
    Slider volumeSlider = new Slider();

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
            timeSlider(), playTime(), volumeLabel, volumeSlider());

        return hbox;
    }//method

    //create back button
    private Button backButton()
    {
        Button backButton = new Button("Back");

        // allow the user to go back a track.
        backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
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
        playList.get(currentTrack).play();
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
                            playList.get(currentTrack).seek(playList.get(currentTrack).getStartTime());
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

        playList.get(currentTrack).currentTimeProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    updateValues();
                }
            });

        playList.get(currentTrack).setOnPlaying(new Runnable() {
                public void run() {
                    if (stopRequested) {
                        playList.get(currentTrack).pause();
                        stopRequested = false;
                    } else {
                        playButton.setText("Pause");
                    }
                }
            });

        playList.get(currentTrack).setOnPaused(new Runnable() {
                public void run() {
                    System.out.println("onPaused");
                    playButton.setText("Play");
                }
            });

        playList.get(currentTrack).setOnReady(new Runnable() {
                public void run() {
                    duration = playList.get(currentTrack).getMedia().getDuration();
                    updateValues();
                }
            });

        playList.get(currentTrack).setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        playList.get(currentTrack).setOnEndOfMedia(new Runnable() {
                public void run() {
                    if (!repeat) {
                        playButton.setText(">");
                        stopRequested = true;
                        atEndOfMedia = true;
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
                    //playList.get(currentTrack).play();
                    playButton();
                }
            });
        return nextButton;
    }//method

    //make a time slider to show progress of the song
    private Slider timeSlider()
    {
        //time slider width
        final int MIN_TSLIDER_WIDTH = 50;

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

    @SuppressWarnings("deprecation")//divide(duration) was deprecated because of a bug in jdk
    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                    public void run() {
                        Duration currentTime = playList.get(currentTrack).getCurrentTime();
                        playTime.setText(formatTime(currentTime, duration));
                        timeSlider.setDisable(duration.isUnknown());
                        if (!timeSlider.isDisabled()
                        && duration.greaterThan(Duration.ZERO)
                        && !timeSlider.isValueChanging()) {
                            timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                        }
                        if (!volumeSlider.isValueChanging()) {
                            volumeSlider.setValue((int) Math.round(playList.get(currentTrack).getVolume()
                                    * 100));
                        }
                    }
                });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
            - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                    elapsedHours, elapsedMinutes, elapsedSeconds,
                    durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                    elapsedMinutes, elapsedSeconds, durationMinutes,
                    durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                    elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                    elapsedSeconds);
            }
        }
    }

    // Add Play label
    private Label playTime()
    {

        playTime = new Label();
        playTime.setPrefWidth(130);
        playTime.setMinWidth(50);

        return playTime;
    }

    //make a volume bar
    private Slider volumeSlider()
    {
        //volume slider widths
        final int PREF_VSLIDER_WIDTH = 70;
        final int MIN_VSLIDER_WIDTH = 30;

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
