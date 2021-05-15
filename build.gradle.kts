import kr.entree.spigradle.kotlin.papermc

// todo: needs javadoc, kotlindoc, etc
// todo and sources jars as well as javadoc jars

plugins {
    java
    id("com.diffplug.spotless") version "5.8.2"
    id("kr.entree.spigradle") version "2.2.3"
}

group = "com.dumbdogdiner"
version = "1.0.0"

allprojects {
    // Declare global repositories
    repositories {
        jcenter()
        mavenCentral()

        // Add paper repository here, as it's used in both API and Bukkit modules.
        papermc()
    }
}

subprojects {
    group = "com.dumbdogdiner.myawesomeplugin"

    apply(plugin = "java")
    apply(plugin = "kr.entree.spigradle")

    // Spotless configuration
    apply(plugin =  "com.diffplug.spotless")
    spotless {
        ratchetFrom = "origin/master"
    }

    repositories {
        jcenter()
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        targetCompatibility = JavaVersion.VERSION_11.toString()
        sourceCompatibility = JavaVersion.VERSION_11.toString()
    }
}

tasks {
    // Disable root project building spigot description.
    generateSpigotDescription {
        enabled = false
    }
}
