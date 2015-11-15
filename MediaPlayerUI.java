import javafx.application.Application;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * This is the controller for the rest of the program.  The UI is drawn here and the 
 * components are given functionality in the class.
 */
public class MediaPlayerUI extends Application
{
    public static void main(String[] args)
    {
        launch(MediaPlayerUI.class, args);
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
        FindMedia findMedia = new FindMedia();
        Library library = new Library(findMedia.findMediaPath(stage));
                
        //add a ListView object for the library viewing area in bottom pane
        ListView lvList = library.addLibraryView();
        //add a menuBar to the top for menu functionality
        MenuBar menuBar = addMenuBar(stage);
        //assign the menuBar to the top Pane of the border layout
        border.setTop(menuBar);
        //add a horizontal box to the center border pane for buttons
        MediaControls mediaControls = new MediaControls();
                
        //media controls are in the hBox
        border.setCenter(mediaControls.addHBox());

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
    } //method

    /**
     * Create MenuBar item and populate the menu
     */
    @SuppressWarnings("unchecked")
    private MenuBar addMenuBar(Stage stage)//, Library library)//, ListView lvList)
    {

        //adds the "File" drop down menu
        final Menu menu1 = new Menu("File");

        //create the menubar
        MenuBar menuBar = new MenuBar();
        //add the 'File' drop down menu to the menubar
        menuBar.getMenus().addAll(menu1);

        //exit menu item.  adds event handler to exit and close the media player
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t){
                    System.exit(0);
                }
            });//eventhandler
        //populate the file menu observable list
        menu1.getItems().add(exitMenuItem);
        //returns the menuBar as a complete and populated object
        return menuBar;
    }//method

}//class
