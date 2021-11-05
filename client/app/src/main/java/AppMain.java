import org.apache.commons.cli.*;
import org.gradle.tooling.BuildActionExecuter;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option installDirOption = new Option(null, "gradle-installation", true, "Gradle installation directory");
        options.addOption(installDirOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        File installDir = locateInstallDir(installDirOption, commandLine);
        File testBuildDir = locateTestBuild();

        System.out.println("Running tooling API client");
        System.out.println("==========================");

        List<SomeModel> models = fetchModels(installDir, testBuildDir);

        System.out.println("==========================");
        System.out.println("Models:");
        for (SomeModel model : models) {
            System.out.println("  project " + model.getPath() + ", compile classpath has " + model.getCompileClasspath().size() + " entries");
        }
    }

    private static List<SomeModel> fetchModels(File installDir, File testBuildDir) {
        GradleConnector connector = GradleConnector.newConnector();
        if (installDir == null) {
            connector.useGradleVersion("7.4-20211104232430+0000");
        } else {
            connector.useInstallation(installDir);
        }
        connector.forProjectDirectory(testBuildDir);
        try (ProjectConnection connection = connector.connect()) {
            BuildActionExecuter<List<SomeModel>> builder = connection.action(new FetchModels());
            builder.setStandardOutput(System.out);
            builder.setStandardError(System.err);
            return builder.run();
        }
    }

    private static File locateInstallDir(Option installDirOption, CommandLine commandLine) {
        if (commandLine.hasOption(installDirOption)) {
            return new File(commandLine.getOptionValue(installDirOption));
        } else {
            return null;
        }
    }

    private static File locateTestBuild() throws IOException {
        return new File("testbuild").getCanonicalFile();
    }
}
