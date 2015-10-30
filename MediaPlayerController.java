
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

/**
 * I want a border pane with a hBox in the top cell and a observable list in the center cell.
 * in the hbox I want  three panes with elements inside.  in left pane i want back/play/next.
 * center pane I want now playing. left pane volume.
 */
public class MediaPlayerController extends Application
{
    public static void main(String[] args)
    {
        launch(MediaPlayerController.class, args);
    }

    @Override
    public void start(Stage stage)
    {
        final int WIDTH = 800;
        final int HEIGHT = 450;
        
        //Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        //add a horizontal box to the top of the border pane for buttons
        HBox hbox = addHBox();
        //media controls are in the hBox
        border.setCenter(hbox);
        ListView lvList = addLibraryView();
        //library is viewed in the lvList
        border.setBottom(lvList);

        Scene scene = new Scene(border, WIDTH, HEIGHT);
        
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");
        //create the menubar
        MenuBar menuBar = new MenuBar();
        
        menuBar.getMenus().addAll(menu1);
        
        //File menu - import new, delete, exit
        //build File menu list items
        MenuItem importMenuItem = new MenuItem("Import media");
        MenuItem deleteMenuItem = new MenuItem("Delete media");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t){
                System.exit(0);
            }
        });
        //populate the file menu observable list
        menu1.getItems().add(importMenuItem);
        menu1.getItems().add(deleteMenuItem);
        menu1.getItems().add(exitMenuItem);
        border.setTop(menuBar);

        stage.setScene(scene);

        stage.setTitle("YouTunes Media Player");
        stage.show();
    }

    /**
     * Creates an HBox with buttons for the center region
     */
    private HBox addHBox()
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #bfc2c7;");//sets the hbox to blue
        //back button
        Button buttonBack = new Button("Back");
        buttonBack.setPrefSize(75, 20);
        //play button
        Button buttonCurrent = new Button("Play");
        buttonCurrent.setPrefSize(75, 20);
        //next button
        Button buttonNext = new Button("Next");
        buttonNext.setPrefSize(75, 20);
        
        // Add Time label
        Label timeLabel = new Label("Time: ");

        // Add time slider
        Slider timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                   // mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });
        
        // Add the volume label
        Label volumeLabel = new Label("Vol: ");
        // Add Volume slider
        Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);

        hbox.getChildren().addAll(buttonBack, buttonCurrent, buttonNext, timeLabel,
            timeSlider, volumeLabel, volumeSlider);

        return hbox;
    }

    /**
     * Create the center pane format
     */
    private ListView addLibraryView()
    {
        //BorderPane border = new BorderPane();
        //border.setPadding(new Insets(20, 0, 20, 20));

        ListView<String> lvList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (
                "Goes Like This", "For This", "Skit",
                "Ya Mean", "Chicken salad");
        lvList.setItems(items);
        lvList.setMaxHeight(Control.USE_PREF_SIZE);
        lvList.setPrefWidth(800.0);

        return lvList;

    }
}
