import java.util.*;
import java.io.File;
import java.io.FilenameFilter;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.util.Duration;

//todo: add tableview support for the viewing window
/**
 * Creates a Library that contains the paths for media
 */
public class Library
{

    public static final List<String> SUPPORTED_FILE_EXTENSIONS = Arrays.asList(".mp3", ".m4a");
    public static final int FILE_EXTENSION_LEN = 3;

    public static final String TAG_COLUMN_NAME = "Tag";
    public static final String VALUE_COLUMN_NAME = "Value";

    private ChangeListener<Duration> progressChangeListener;
    private MapChangeListener<String, Object> metadataChangeListener;

    //Library items;
    ArrayList<String> trackNames = new ArrayList<String>();
    ListView<String> lvList;
    MediaView mediaView = new MediaView();
    List<MediaPlayer> players;
    final Label currentlyPlaying = new Label();
    final ProgressBar progress = new ProgressBar();
    final TableView<Map> metadataTable = new TableView<>();

    /** Constructor for objects of class Library*/

    public Library(String path)
    {
        final File dir =  new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Cannot find audio source directory: " + dir + " please supply a directory as a command line argument");
            Platform.exit();
            return;
        }

        // create an arraylist of media players
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

        //make a concurrent track listing for the library view window
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
        /*for (int i = 0; i < players.size(); i++) {
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
        }*/

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

    public List<MediaPlayer> getMediaPlayers()
    {
        return players;
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

        progress.setProgress(0);
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

        setMetaDataDisplay(newPlayer.getMedia().getMetadata());
    }//method

    private void setMetaDataDisplay(ObservableMap<String, Object> metadata) {
        metadataTable.getItems().setAll(convertMetadataToTableData(metadata));
        metadataChangeListener = new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ?> change) {
                metadataTable.getItems().setAll(convertMetadataToTableData(metadata));
            }
        };
        metadata.addListener(metadataChangeListener);
    }//method

    private ObservableList<Map> convertMetadataToTableData(ObservableMap<String, Object> metadata) {
        ObservableList<Map> allData = FXCollections.observableArrayList();

        for (String key: metadata.keySet()) {
            Map<String, Object> dataRow = new HashMap<>();

            dataRow.put(TAG_COLUMN_NAME,   key);
            dataRow.put(VALUE_COLUMN_NAME, metadata.get(key));

            allData.add(dataRow);
        }//method

        return allData;
    }

    public ArrayList<String> getTracksArray()
    {
        return trackNames;
    }

    /**
     * Create the library viewing region
     */
    public ListView addLibraryView()
    {
        final double PREF_WIDTH = 800;

        lvList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (trackNames);
        lvList.setItems(items);
        lvList.setMaxHeight(Control.USE_PREF_SIZE);
        lvList.setPrefWidth(PREF_WIDTH);

        return lvList;
    }//method
}//class
