import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.beans.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.*;
import javafx.event.EventType;
import javafx.event.*;

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
        //button dimensions
        final int BUTTON_WIDTH = 50;
        final int BUTTON_HEIGHT = 20;
        final int CURRENT_BUTTON_HEIGHT = 30;
        //volume slider widths
        final int PREF_VSLIDER_WIDTH = 70;
        final int MIN_VSLIDER_WIDTH = 30;
        //time slider width
        final int MIN_TSLIDER_WIDTH = 50;
        //hbox background color
        final String HBOX_BACKGROUND_COLOR = "-fx-background-color: #bfc2c7;";

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(PAD_WIDTH, PAD_HEIGHT, PAD_WIDTH, PAD_HEIGHT));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle(HBOX_BACKGROUND_COLOR);//sets the hbox to grey
        //back button
        Button buttonBack = new Button("Back");
        buttonBack.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        //play button
        Button buttonCurrent = new Button("Play");
        buttonCurrent.setPrefSize(BUTTON_WIDTH, CURRENT_BUTTON_HEIGHT);
        //next button
        Button buttonNext = new Button("Next");
        buttonNext.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        // Add Time label
        Label timeLabel = new Label("Time: ");

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

        // Add the volume label
        Label volumeLabel = new Label("Vol: ");
        // Add Volume slider
        Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(PREF_VSLIDER_WIDTH);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(MIN_VSLIDER_WIDTH);

        hbox.getChildren().addAll(buttonBack, buttonCurrent, buttonNext, timeLabel,
            timeSlider, volumeLabel, volumeSlider);

        return hbox;
    }//method

    
}
