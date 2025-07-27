plugins {
    id("java")
}

group = "com.kojitadokoro114"
version = "1.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.112.Final")
}
