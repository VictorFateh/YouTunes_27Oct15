import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.*;

/**
 * Write a description of class mediaControls here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MediaControls
{

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
        final String HBOX_BACKGROUND_COLOR = "-fx-background-color: #bfc2c7;";

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
        
        // play each audio file in turn.
        //playLibrary();

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
