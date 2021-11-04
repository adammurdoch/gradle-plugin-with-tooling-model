import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import javax.inject.Inject;

@SuppressWarnings("unused")
public abstract class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("-> configure project " + target.getPath());
        target.getPlugins().apply("java-library");
        target.getRepositories().mavenCentral();
        getToolingModelBuilderRegistry().register(new ModelBuilderImpl());
    }

    @Inject
    public abstract ToolingModelBuilderRegistry getToolingModelBuilderRegistry();
}
