plugins {
    id("test.plugin").apply(false)
}

subprojects {
    plugins.apply(TestPlugin::class.java)
}
