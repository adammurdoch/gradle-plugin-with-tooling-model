import java.io.File;
import java.io.Serializable;
import java.util.List;

class DefaultModel implements SomeModel, Serializable {
    private final String projectPath;
    private final int option;
    private final List<File> compileClasspath;
    private final List<File> runtimeClasspath;

    public DefaultModel(String projectPath, int option, List<File> compileClasspath, List<File> runtimeClasspath) {
        this.projectPath = projectPath;
        this.option = option;
        this.compileClasspath = compileClasspath;
        this.runtimeClasspath = runtimeClasspath;
    }

    @Override
    public String getProjectPath() {
        return projectPath;
    }

    @Override
    public String toString() {
        return projectPath;
    }

    @Override
    public int getOption() {
        return option;
    }

    @Override
    public List<File> getCompileClasspath() {
        return compileClasspath;
    }

    @Override
    public List<File> getRuntimeClasspath() {
        return runtimeClasspath;
    }
}
