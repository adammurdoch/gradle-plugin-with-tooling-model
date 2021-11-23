plugins {
    id("test.plugin")
}

options {
    option.set(1)
}

dependencies {
    implementation(project(":lib3"))
}
