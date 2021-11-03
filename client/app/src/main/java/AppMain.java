import org.gradle.tooling.*;
import org.gradle.tooling.model.gradle.BasicGradleProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException {
        System.out.println("Running tooling API client");
        GradleConnector connector = GradleConnector.newConnector();
        connector.useGradleVersion("7.4-20211102232306+0000");
        File projectDir = new File("../../testbuild").getCanonicalFile();
        connector.forProjectDirectory(projectDir);
        try (ProjectConnection connection = connector.connect()) {
            BuildActionExecuter<List<SomeModel>> builder = connection.action(new FetchModels());
            builder.setStandardOutput(System.out);
            builder.setStandardError(System.err);
//            builder.addArguments("--no-parallel", "-Dorg.gradle.unsafe.isolated-projects=true");
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
}
