import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import java.util.ArrayList;

//todo: add tableview support for the viewing window


/**
 * Creates a Library that contains the paths for media
 */
public class Library
{
    //Library items;
    ObservableList<String> items;
    ArrayList<String> pathList;
    ArrayList<String> trackNames;
    ListView<String> lvList;

    /**
     * Constructor for objects of class Library
     */
    public Library()
    {
        items = FXCollections.observableArrayList ();
        pathList = new ArrayList<String>();
        trackNames = new ArrayList<String>();
    }//method
    
    
    /**
     * return the size of the item List
     */
    public int obListLength()
    {
        return items.size();
    }//method
        
    
    /**
     * Create the library viewing region
     */
    public ListView addLibraryView()
    {
        final double PREF_WIDTH = 800;

        lvList = new ListView<String>();
        //The empty strings exist to make the list view appear on startup
        ObservableList<String> items = FXCollections.observableArrayList ("");
        
        lvList.setItems(items);
        lvList.setMaxHeight(Control.USE_PREF_SIZE);
        lvList.setPrefWidth(PREF_WIDTH);

        //define a context menu
        ContextMenu contextMenu = addContextMenu(lvList);
        return lvList;
    }//method

    /**
     * Create a context menu for the library viewing area
     */
    public ContextMenu addContextMenu(ListView<String> lvList)
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
    }//method
}//class
