import org.gradle.tooling.BuildAction;
import org.gradle.tooling.BuildController;
import org.gradle.tooling.model.gradle.BasicGradleProject;

class FetchModelForProject implements BuildAction<SomeModel> {
    private final BasicGradleProject project;

    public FetchModelForProject(BasicGradleProject project) {
        this.project = project;
    }

    @Override
    public SomeModel execute(BuildController controller) {
        return controller.findModel(project, SomeModel.class);
    }
}
