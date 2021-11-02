import org.gradle.api.Plugin;

public class TestPlugin implements Plugin {
    @Override
    public void apply(Object target) {
        System.out.println("-> apply test plugin");
    }

    private static class ModelImpl implements SomeModel {
    }
}
