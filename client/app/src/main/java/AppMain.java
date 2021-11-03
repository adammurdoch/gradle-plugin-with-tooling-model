import org.gradle.tooling.*;
import org.gradle.tooling.model.gradle.BasicGradleProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException {
        System.out.println("-> app!");
        GradleConnector connector = GradleConnector.newConnector();
        connector.useGradleVersion("7.4-20211102232306+0000");
        File projectDir = new File("../../testbuild").getCanonicalFile();
        connector.forProjectDirectory(projectDir);
        try (ProjectConnection connection = connector.connect()) {
            BuildActionExecuter<List<SomeModel>> builder = connection.action(new FetchModels());
            builder.setStandardOutput(System.out);
            builder.setStandardError(System.err);
            List<SomeModel> model = builder.run();
            System.out.println("-> model = " + model);
        }
    }

    private static class FetchModels implements BuildAction<List<SomeModel>> {
        @Override
        public List<SomeModel> execute(BuildController controller) {
            List<SomeModel> result = new ArrayList<>();
            for (BasicGradleProject project : controller.getBuildModel().getProjects()) {
                System.out.println("-> load from project: " + project);
                SomeModel model = controller.findModel(project, SomeModel.class);
                if (model != null) {
                    result.add(model);
                }
            }
            return result;
        }
    }
}
