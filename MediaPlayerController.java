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
        //add a menuBar to the top for menu functionality
        MenuBar menuBar = addMenuBar(stage);
        //assign the menuBar to the top Pane of the border layout
        border.setTop(menuBar);
        //add a horizontal box to the center border pane for buttons
        HBox hbox = addHBox();
        //media controls are in the hBox
        border.setCenter(hbox);
        //add a ListView object for the library viewing area in bottom pane
        ListView lvList = addLibraryView();
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
    }

    /**
     * Create MenuBar item and populate the menu
     */
    private MenuBar addMenuBar(Stage stage)
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
                        //openFile(selectedFile);
                    }
                }
            });

        //exit menu item.  adds event handler to exit and close the media player
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    System.exit(0);
                }
            });
        //populate the file menu observable list
        menu1.getItems().add(importMenuItem);
        menu1.getItems().add(exitMenuItem);
        //returns the menuBar as a complete and populated object
        return menuBar;
    }

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
            });

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
    }

    /**
     * Create the library viewing region
     */
    private ListView addLibraryView()
    {
        final double PREF_WIDTH = 800;

        ListView<String> lvList = new ListView<String>();
        //Currently hard coded strings for mockup purposes
        ObservableList<String> items = FXCollections.observableArrayList (
                "Goes Like This", "For This", "Skit",
                "Ya Mean", "Chicken salad");
        lvList.setItems(items);
        lvList.setMaxHeight(Control.USE_PREF_SIZE);
        lvList.setPrefWidth(PREF_WIDTH);

        //define a context menu
        ContextMenu contextMenu = addContextMenu(lvList);
        return lvList;
    }
    
    /**
     * Create a context menu for the library viewing area
     */
    private ContextMenu addContextMenu(ListView<String> lvList)
    {
        final ContextMenu rowMenu = new ContextMenu();
        //create menu items for context menu
        MenuItem removeItem = new MenuItem("Remove Media");
        MenuItem exitItem = new MenuItem("Exit");
        //Event Handler for 'Remove Item'
        removeItem.setOnAction(new EventHandler<ActionEvent>(){
                //create a modal confirmation dialog box
                @Override
                public void handle(ActionEvent t) { 
                    String titleTxt = "Remove Media";
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle(titleTxt);
                    String s = "Confirm to remove track from library!";
                    alert.setContentText(s);
                    Optional<ButtonType> result = alert.showAndWait();
                    //if ok is pressed, remove media 
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        //remove track from library
                    }

                }
            });

        //Event Handler for 'Exit'
        exitItem.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    System.exit(0);
                }
            });   

        //add menu items to context menu
        rowMenu.getItems().add(removeItem);
        rowMenu.getItems().add(exitItem);
        //make context menu visible in the library view window
        lvList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
            {
                @Override public void handle(MouseEvent e)
                {
                    if (e.getButton() == MouseButton.SECONDARY)
                    {
                        rowMenu.show(lvList, e.getScreenX(), e.getScreenY());
                    }
                }
            });
        return rowMenu;
    }
}
