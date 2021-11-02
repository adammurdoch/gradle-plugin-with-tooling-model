plugins {
    id("application")
}

dependencies {
    implementation("test.plugin:model:1.0")
}

application {
    mainClass.set("AppMain")
}
