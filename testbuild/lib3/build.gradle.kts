plugins {
    id("test.plugin")
}

options {
    option.set(3)
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1.1")
}
