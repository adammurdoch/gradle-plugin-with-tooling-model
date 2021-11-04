import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilder;

import java.util.ArrayList;

class ModelBuilderImpl implements ToolingModelBuilder {
    @Override
    public boolean canBuild(String modelName) {
        return modelName.equals(SomeModel.class.getName());
    }

    @Override
    public Object buildAll(String modelName, Project project) {
        System.out.println("-> build model " + modelName + " for " + project);
        return new DefaultModel(project.getPath(), new ArrayList<>(project.getConfigurations().getByName("compileClasspath").getFiles()));
    }
}
