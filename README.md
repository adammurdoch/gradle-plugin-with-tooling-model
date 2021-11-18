# Gradle plugin with tooling model

This repository contains a test app that fetches a simple tooling model.
It is intended to be used to test the behaviour of [project isolation](https://gradle.github.io/configuration-cache/) in Gradle.

The model contains some basic information about the project, including its compile classpath and a value taken from an
extension added by the custom plugin.

### Repository contents

The source is arranged as follows:

- `plugin` contains a custom plugin implementation that applies the `java-library` plugin and registers a builder for the tooling model.
  - `model` contains the tooling model API.
  - `plugin` contains the plugin implementation.
- `testbuild` contains a small multi-project build that uses the plugin. This build has project isolation enabled.
- `client/app` contains the tooling API client that queries the tooling model.

### Usage

From the root directory of this repository, run:

```
> ./gradlew :client:app:installDist && ./client/app/build/install/app/bin/app
```

The app will show the Gradle output and then log the details from the model. The test plugin logs some output when it
is applied and the model builder also logs some output when a model is built for a project. Using this logging, you
can see which project are configured and which models are built.

To try out the tooling model caching:

- Run the application and note that all projects are configured and all models are created.
- Run the application again without making any changes and note that no projects are configured or models created.
- Change the build script for a project and run the application. The modified project should be configured and its model created. Other projects should not be configured (subject to the caveats below) and no other models created.

The test build has the following dependency graph:

```
  app +--> lib1 ---> lib3
      |
      +--> lib2
      
  lib4
```

Here are some example changes to try:

- Try changing the build script in `lib1/build.gradle.kts`. The project `lib1` will be configured and its model created. The `app` project is also configured because it has a dependency on `lib1`. The `lib3` project is not configured because, although `lib1` depends on it, the cached model and dependency metadata for `lib3` can be reused.
- Try changing the build script in `app/build.gradle.kts`. The `app` project is configured and its model created. The other projects are not configured because their cached models and dependency metadata can be reused.
- Try changing the build script in `lib3/build.gradle.kts`. The `app`, `lib1` and `lib3` projects are configured and their models created.

Some things to be aware of with the current implementation of project isolation:

- When any project changes, the settings script is always run.
- When any project changes, `buildSrc` and plugins in included builds are rebuilt. A later milestone will fix this.
- When any project changes, its parent project is always configured. However, the parent project's model is not recreated.
- When any project changes, then any other projects that depend on the changed project are also configured and their models created. A later milestone will change this to happen only when the dependency graph of the changed project actually changes.
- When the settings file changes, then all project configured and all models created.
- Parallel configuration is disabled. It is still occasionally flaky. A later milestones will fix this.

You can test out parallel configuration and model building by using the `--parallel` option with the test app.
Note that this also adds a 2 second delay to the test plugin and the model builder, so you can see the effect of parallel configuration.
