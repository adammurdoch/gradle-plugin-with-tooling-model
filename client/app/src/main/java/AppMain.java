import net.rubygrapefruit.platform.Native;
import net.rubygrapefruit.platform.NativeIntegrationUnavailableException;
import net.rubygrapefruit.platform.terminal.Terminals;
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
        Option parallelOption = new Option(null, "parallel", false, "Fetch models in parallel, also adds some delay");
        options.addOption(parallelOption);
        Option buildOption = new Option(null, "build", true, "The Gradle build to fetch models for");
        options.addOption(buildOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        File installDir = locateInstallDir(installDirOption, commandLine);
        File testBuildDir = locateTestBuild(buildOption, commandLine);
        boolean parallel = commandLine.hasOption(parallelOption);

        System.out.println("Running tooling API client");
        System.out.println("====== GRADLE OUTPUT ======");

        List<SomeModel> models = fetchModels(installDir, testBuildDir, parallel);

        System.out.println("==== END GRADLE OUTPUT ====");
        System.out.println();
        System.out.println("Models:");
        for (SomeModel model : models) {
            System.out.println("  project " + model.getProjectPath());
            System.out.println("    option: " + model.getOption());
            System.out.println("    compile classpath: " + format(testBuildDir, model.getCompileClasspath()));
            System.out.println("    runtime classpath: " + format(testBuildDir, model.getRuntimeClasspath()));
        }
        System.out.println();
    }

    private static String format(File rootDir, List<File> files) {
        if (files.isEmpty()) {
            return "no files";
        }

        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < files.size() && builder.length() < 60) {
            if (i > 0) {
                builder.append(", ");
            }
            File file = files.get(i);
            if (file.toPath().startsWith(rootDir.toPath())) {
                builder.append(rootDir.toPath().relativize(file.toPath()));
            } else {
                builder.append(file.getName());
            }
            i++;
        }
        if (i != files.size()) {
            builder.append(", ...");
        }
        builder.append(" (");
        builder.append(files.size());
        builder.append(" files total)");
        return builder.toString();
    }

    private static List<SomeModel> fetchModels(File installDir, File testBuildDir, boolean parallel) {
        GradleConnector connector = GradleConnector.newConnector();
        if (installDir == null) {
            connector.useGradleVersion("7.4-20211201142612+0000");
        } else {
            connector.useInstallation(installDir);
        }
        connector.forProjectDirectory(testBuildDir);
        try (ProjectConnection connection = connector.connect()) {
            BuildActionExecuter<List<SomeModel>> builder = connection.action(new FetchModels());
            builder.setStandardOutput(System.out);
            builder.setStandardError(System.err);
            if (parallel) {
                builder.addArguments("--parallel", "-Dtest.delay=2000");
            }
            if (isTerminal()) {
                builder.setColorOutput(true);
            }
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

    private static File locateTestBuild(Option buildOption, CommandLine commandLine) throws IOException {
        if (commandLine.hasOption(buildOption)) {
            return new File(commandLine.getOptionValue(buildOption));
        } else {
            return new File("testbuild").getCanonicalFile();
        }
    }

    private static boolean isTerminal() {
        try {
            Terminals terminals = Native.get(Terminals.class);
            return terminals.isTerminal(Terminals.Output.Stdout) && terminals.isTerminal(Terminals.Output.Stderr);
        } catch (NativeIntegrationUnavailableException e) {
            // Ignore
            return false;
        }
    }
}
