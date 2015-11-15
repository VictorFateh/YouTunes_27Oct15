import javafx.stage.*;
import java.io.File;

/**
 *Exists to open a directory chooser to find the path where the music is
 */
public class FindMedia
{
    @SuppressWarnings("unchecked")
    public String findMediaPath(Stage stage)
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Please Select Media Folder");
        File selectedDirectory = chooser.showDialog(stage);
        {
            String path = selectedDirectory.getAbsolutePath();
            path = path.replace("\\", "/");
  
            return path;
        }
    }
}
