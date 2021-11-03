import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import javax.inject.Inject;
import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("-> apply test plugin");
        getToolingModelBuilderRegistry().register(new ModelBuilderImpl());
    }

    @Inject
    public abstract ToolingModelBuilderRegistry getToolingModelBuilderRegistry();

    private static class ModelBuilderImpl implements ToolingModelBuilder {
        @Override
        public boolean canBuild(String modelName) {
            return modelName.equals(SomeModel.class.getName());
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            System.out.println("-> build model " + modelName + " for " + project);
            return new ModelImpl(project.getPath());
        }
    }

    private static class ModelImpl implements SomeModel, Serializable {
        private final String projectPath;

        public ModelImpl(String projectPath) {
            this.projectPath = projectPath;
        }

        @Override
        public String toString() {
            return projectPath;
        }
    }
}
