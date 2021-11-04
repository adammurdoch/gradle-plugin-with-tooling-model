import org.apache.commons.cli.*;
import org.gradle.tooling.*;
import org.gradle.tooling.model.gradle.BasicGradleProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option installDirOption = new Option(null, "gradle-installation", true, "Gradle installation directory");
        options.addOption(installDirOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        File installDir = null;
        if (commandLine.hasOption(installDirOption)) {
            installDir = new File(commandLine.getOptionValue(installDirOption));
        }

        System.out.println("Running tooling API client");

        GradleConnector connector = GradleConnector.newConnector();
        if (installDir == null) {
            connector.useGradleVersion("7.4-20211103232305+0000");
        } else {
            connector.useInstallation(installDir);
        }
        File projectDir = new File("testbuild").getCanonicalFile();
        connector.forProjectDirectory(projectDir);
        try (ProjectConnection connection = connector.connect()) {
            BuildActionExecuter<List<SomeModel>> builder = connection.action(new FetchModels());
            builder.setStandardOutput(System.out);
            builder.setStandardError(System.err);
            builder.addArguments("--no-parallel", "-Dorg.gradle.unsafe.isolated-projects=true");
            List<SomeModel> model = builder.run();
            System.out.println("-> model = " + model);
        }
    }

    private static class FetchModels implements BuildAction<List<SomeModel>> {
        @Override
        public List<SomeModel> execute(BuildController controller) {
            List<BuildAction<SomeModel>> actions = new ArrayList<>();
            for (BasicGradleProject project : controller.getBuildModel().getProjects()) {
                actions.add(new FetchModelForProject(project));
            }
            return controller.run(actions);
        }
    }

    private static class FetchModelForProject implements BuildAction<SomeModel> {
        private final BasicGradleProject project;

        public FetchModelForProject(BasicGradleProject project) {
            this.project = project;
        }

        @Override
        public SomeModel execute(BuildController controller) {
            return controller.findModel(project, SomeModel.class);
        }
    }
}
