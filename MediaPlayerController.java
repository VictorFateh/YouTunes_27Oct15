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
 * This is the controller for the rest of the program.  The UI is drawn here and the 
 * components are given functionality in the class.
 */
public class MediaPlayerController extends Application
{
    public static void main(String[] args)
    {
        launch(MediaPlayerController.class, args);
    }

    /**
     * this is where the program is actually instantiated from.
     * main() just redirects to here.
     */
    @Override
    public void start(Stage stage)
    {
        final int WIDTH = 800;
        final int HEIGHT = 450;

        //Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        //create a library to view in the library viewing window
        Library library = new Library();

        //add a ListView object for the library viewing area in bottom pane
        ListView lvList = library.addLibraryView();
        //add a menuBar to the top for menu functionality
        MenuBar menuBar = addMenuBar(stage, library, lvList);
        //assign the menuBar to the top Pane of the border layout
        border.setTop(menuBar);
        //add a horizontal box to the center border pane for buttons
        HBox hbox = addHBox();
        //media controls are in the hBox
        border.setCenter(hbox);

        //library is viewed in the lvList
        border.setBottom(lvList);
        //instantiate the JFRAME
        Scene scene = new Scene(border, WIDTH, HEIGHT);
        //Set the caspian stylesheet
        Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        //populate the scene
        stage.setScene(scene);

        //prevent the window from being resized
        stage.setResizable(false);
        //display the name in the top border
        stage.setTitle("YouTunes Media Player");
        //draw the scene on the display
        stage.show();
    }//method

    /**
     * Create MenuBar item and populate the menu
     */
    private MenuBar addMenuBar(Stage stage, Library library, ListView lvList)
    {

        //adds the "File" drop down menu
        final Menu menu1 = new Menu("File");
        //fileChooser
        final FileChooser fileChooser = new FileChooser();

        //create the menubar
        MenuBar menuBar = new MenuBar();
        //add the 'File' drop down menu to the menubar
        menuBar.getMenus().addAll(menu1);

        //File drop down menu - import new, delete, exit
        //build File menu list items
        MenuItem importMenuItem = new MenuItem("Import media");
        importMenuItem.setOnAction(new EventHandler<ActionEvent>()
            {

                public void handle(ActionEvent t)
                { 
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
                    File selectedFile = fileChooser.showOpenDialog(stage);
                    if (selectedFile != null) 
                    {
                        int zero = 0;
                        //find the path and convert to a useful String
                        String path = selectedFile.getAbsolutePath();
                        path = path.replace("\\", "/");

                        Song newSong = new Song();
                        newSong.createMedia(path);

                        //repaint the library view
                        int lvListSize = library.obListLength();
                        //refresh the listview 
                        //raises a warning flag that my system isn't displaying well, random try catch block //fix
                        try 
                        {
                            if(lvListSize <= 0)
                            {
                                lvList.fireEvent(new ListView.EditEvent<>(lvList, ListView.editCommitEvent(), selectedFile.getName(), lvListSize));
                            }
                        }
                        catch(RuntimeException re){
                            // Handle construction errors
                            System.out.println("Caught Exception: " + re.getMessage());
                        }
                    }
                }
            });//eventhandler

        //exit menu item.  adds event handler to exit and close the media player
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    System.exit(0);
                }
            });//eventhandler
        //populate the file menu observable list
        menu1.getItems().add(importMenuItem);
        menu1.getItems().add(exitMenuItem);
        //returns the menuBar as a complete and populated object
        return menuBar;
    }//method

    

    /**
     * Creates an HBox (horizontal box) with buttons for the center region
     */
    private HBox addHBox()
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
}//class
