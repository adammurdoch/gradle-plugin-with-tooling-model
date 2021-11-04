import java.io.File;
import java.util.List;

public interface SomeModel {
    String getPath();

    List<File> getCompileClasspath();
}
