import org.gradle.tooling.BuildAction;
import org.gradle.tooling.BuildController;
import org.gradle.tooling.model.gradle.BasicGradleProject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FetchModels implements BuildAction<List<SomeModel>> {
    @Override
    public List<SomeModel> execute(BuildController controller) {
        List<BuildAction<SomeModel>> actions = new ArrayList<>();
        for (BasicGradleProject project : controller.getBuildModel().getProjects()) {
            actions.add(new FetchModelForProject(project));
        }
        return controller.run(actions).stream().filter(someModel -> someModel != null).collect(Collectors.toList());
    }
}
