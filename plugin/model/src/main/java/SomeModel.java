import java.io.File;
import java.util.List;

public interface SomeModel {
    String getProjectPath();

    int getOption();

    List<File> getCompileClasspath();

    List<File> getRuntimeClasspath();
}
