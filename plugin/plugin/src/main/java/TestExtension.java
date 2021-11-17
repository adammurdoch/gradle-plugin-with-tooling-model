import org.gradle.api.provider.Property;

public interface TestExtension {
    Property<Integer> getOption();
}
