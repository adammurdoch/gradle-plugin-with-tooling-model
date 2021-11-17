plugins {
    id("test.plugin")
}

model {
    option.set(45)
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1.1")
//    implementation(project(":lib3"))
}
