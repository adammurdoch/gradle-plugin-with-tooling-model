plugins {
    id("test.plugin")
}

model {
    option.set(0)
}

dependencies {
    implementation(project(":lib1"))
    implementation(project(":lib2"))
//    implementation(project(":lib4"))
}