import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import java.util.*;//ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.ListView;
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.application.Platform;
import java.io.File;
import java.io.FilenameFilter;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

//todo: add tableview support for the viewing window
/**
 * Creates a Library that contains the paths for media
 */
public class Library
{

    public static final List<String> SUPPORTED_FILE_EXTENSIONS = Arrays.asList(".mp3", ".m4a");
    public static final int FILE_EXTENSION_LEN = 3;

    private ChangeListener<Duration> progressChangeListener;
    private MapChangeListener<String, Object> metadataChangeListener;

    //Library items;
    ObservableList<String> items;
    ArrayList<String> pathList;
    ArrayList<String> trackNames = new ArrayList<String>();
    ListView<String> lvList;
    MediaView mediaView = new MediaView();
    List<MediaPlayer> players;

    /** Constructor for objects of class Library*/

    public Library(String path)
    {
        final File dir =  new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Cannot find audio source directory: " + dir + " please supply a directory as a command line argument");
            Platform.exit();
            return;
        }

        // create some media players
        players = new ArrayList<>();
        for (String file : dir.list(new FilenameFilter() {
                @Override public boolean accept(File dir, String name) {
                    for (String ext: SUPPORTED_FILE_EXTENSIONS) {
                        if (name.endsWith(ext)) {
                            return true;
                        }
                    }

                    return false;
                }
            })) players.add(createPlayer("file:///" + (dir + "\\" + file).replace("\\", "/").replaceAll(" ", "%20")));
        //make a concurrent track listing
        for(int i = 0; i < players.size(); i++)
        {
            String source = players.get(i).getMedia().getSource();
            source = source.substring(0, source.length() - FILE_EXTENSION_LEN);
            source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
            
            trackNames.add(source);
        }
        

        if (players.isEmpty()) {
            System.out.println("No audio found in " + dir);
            Platform.exit();
            return;
        }    


        // play each audio file in turn.
        for (int i = 0; i < players.size(); i++) {
            final MediaPlayer player     = players.get(i);
            final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
            player.setOnEndOfMedia(new Runnable() {
                    @Override public void run() {
                        player.currentTimeProperty().removeListener(progressChangeListener);
                        player.getMedia().getMetadata().removeListener(metadataChangeListener);
                        player.stop();
                        mediaView.setMediaPlayer(nextPlayer);
                        nextPlayer.play();
                    }
                });
        }

    }//constructor(String)

    
    public void playLibrary()
    {

        for (int i = 0; i < players.size(); i++) {
            final MediaPlayer player     = players.get(i);
            final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
            player.setOnEndOfMedia(new Runnable() {
                    @Override public void run() {
                        player.currentTimeProperty().removeListener(progressChangeListener);
                        player.getMedia().getMetadata().removeListener(metadataChangeListener);
                        player.stop();
                        mediaView.setMediaPlayer(nextPlayer);
                        nextPlayer.play();
                    }
                });
        }
    }


    /** @return a MediaPlayer for the given source which will report any errors it encounters */
    private MediaPlayer createPlayer(String mediaSource) {
        final Media media = new Media(mediaSource);
        final MediaPlayer player = new MediaPlayer(media);
        player.setOnError(new Runnable() {
                @Override public void run() {
                    System.out.println("Media error occurred: " + player.getError());
                }
            });
        return player;
    }//method

    /** sets the currently playing label to the label of the new media player and updates the progress monitor. */
    private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
        newPlayer.seek(Duration.ZERO);

        /*progress.setProgress(0);
        progressChangeListener = new ChangeListener<Duration>() {
        @Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
        progress.setProgress(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
        }
        };
        newPlayer.currentTimeProperty().addListener(progressChangeListener);

        String source = newPlayer.getMedia().getSource();
        source = source.substring(0, source.length() - FILE_EXTENSION_LEN);
        source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
        currentlyPlaying.setText("Now Playing: " + source);

        setMetaDataDisplay(newPlayer.getMedia().getMetadata());*/
    }//method

    private void setMetaDataDisplay(ObservableMap<String, Object> metadata) {
        /*metadataTable.getItems().setAll(convertMetadataToTableData(metadata));
        metadataChangeListener = new MapChangeListener<String, Object>() {
        @Override
        public void onChanged(Change<? extends String, ?> change) {
        metadataTable.getItems().setAll(convertMetadataToTableData(metadata));
        }
        };
        metadata.addListener(metadataChangeListener);*/
    }//method

    public ArrayList<String> getTracksArray()
    {
        return trackNames;
    }
    

    /**
     * Create the library viewing region
     */
    public ListView addLibraryView()//String trackList)
    {
        final double PREF_WIDTH = 800;
        
        lvList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (trackNames);
        lvList.setItems(items);
        lvList.setMaxHeight(Control.USE_PREF_SIZE);
        lvList.setPrefWidth(PREF_WIDTH);
        
        return lvList;
    }//method


    
    /**
     * Create a context menu for the library viewing area
     */
    /*public ContextMenu addContextMenu(ListView<String> lvList)
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
    }*///method
}//class
