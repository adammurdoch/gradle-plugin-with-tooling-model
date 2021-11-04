import java.io.File;
import java.io.Serializable;
import java.util.List;

class DefaultModel implements SomeModel, Serializable {
    private final String projectPath;
    private final List<File> compileClasspath;

    public DefaultModel(String projectPath, List<File> compileClasspath) {
        this.projectPath = projectPath;
        this.compileClasspath = compileClasspath;
    }

    @Override
    public String getPath() {
        return projectPath;
    }

    @Override
    public String toString() {
        return projectPath;
    }

    @Override
    public List<File> getCompileClasspath() {
        return compileClasspath;
    }
}
