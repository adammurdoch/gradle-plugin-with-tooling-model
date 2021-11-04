# Gradle plugin with tooling model

A test app that fetches a simple tooling model. The model contains some basic information about the project, including
its compile classpath.

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
- Change the build script for a project and run the application. The modified project should be configured and its model created.

Some things to be aware of with the current implementation for project isolation:

- When a project changes, the settings script is run even if it hasn't changed.
- When a project changes, it's parent project is configured even if it hasn't changed. The parent project's model is not recreated.
- When a project that has project dependencies changes, then the targets of the project dependencies are also configured even if they haven't changed (to allow dependency resolution to happen). Their models are not recreated.
- When the settings file changes, then all cached state is discarded and all project configured and models created.
- When a project changes, then some fingerprint entries are discarded. It's best to modify only a single project. You can delete the `testbuild/.gradle/configuration-cache` directory to reset the state if Gradle gets confused.