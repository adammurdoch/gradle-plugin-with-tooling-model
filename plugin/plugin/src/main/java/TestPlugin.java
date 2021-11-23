import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import javax.inject.Inject;

@SuppressWarnings("unused")
public abstract class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("apply test plugin to " + target);
        target.getPlugins().apply("java-library");
        target.getRepositories().mavenCentral();
        TestExtension extension = target.getExtensions().create("options", TestExtension.class);
        extension.getOption().convention(target.getPath().length());
        getToolingModelBuilderRegistry().register(new ModelBuilderImpl());
        Delay.delay();
    }

    @Inject
    public abstract ToolingModelBuilderRegistry getToolingModelBuilderRegistry();
}
