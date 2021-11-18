import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

class ModelBuilderImpl implements ToolingModelBuilder {
    @Override
    public boolean canBuild(String modelName) {
        return modelName.equals(SomeModel.class.getName());
    }

    @Override
    public Object buildAll(String modelName, Project project) {
        System.out.println("build model " + modelName + " for " + project);
        TestExtension extension = project.getExtensions().getByType(TestExtension.class);
        Set<File> compileClasspath = project.getConfigurations().getByName("compileClasspath").getFiles();
        Set<File> runtimeClasspath = project.getConfigurations().getByName("runtimeClasspath").getFiles();
        Delay.delay();
        return new DefaultModel(
                project.getPath(),
                extension.getOption().get(),
                new ArrayList<>(compileClasspath),
                new ArrayList<>(runtimeClasspath)
        );
    }
}
