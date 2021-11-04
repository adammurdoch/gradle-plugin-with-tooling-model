plugins {
    id("test.plugin")
}

dependencies {
    implementation(project(":lib1"))
    implementation(project(":lib2"))
}