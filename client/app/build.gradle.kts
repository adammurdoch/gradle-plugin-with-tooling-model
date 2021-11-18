plugins {
    id("application")
}

repositories {
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

dependencies {
    implementation("test.plugin:model:1.0")
    implementation("org.gradle:gradle-tooling-api:7.2")
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("net.rubygrapefruit:native-platform:0.22-milestone-7")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.10")
}

application {
    mainClass.set("AppMain")
}

tasks.named("run", JavaExec::class.java) {
    workingDir = file("../..")
}