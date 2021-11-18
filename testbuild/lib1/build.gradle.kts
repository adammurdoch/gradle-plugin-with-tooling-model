plugins {
    id("test.plugin")
}

model {
    option.set(1)
}

dependencies {
    implementation(project(":lib3"))
}
