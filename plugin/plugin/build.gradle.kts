plugins {
    id("java-gradle-plugin")
}

group = "test.plugin"

dependencies {
    implementation(project(":model"))
}

gradlePlugin {
    plugins {
        create("test") {
            id = "test.plugin"
            implementationClass = "TestPlugin"
        }
    }
}