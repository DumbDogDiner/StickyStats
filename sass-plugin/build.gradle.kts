import kr.entree.spigradle.kotlin.paper

plugins {
    // Use Kotlin, because Kotlin
    kotlin("jvm") version "1.4.32"
    // Use ktlint for linting and formatting
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    // Use Spigradle for nicer plugin configuration
    id("kr.entree.spigradle")
    // Use kapt for CommandAPI annotations
    kotlin("kapt") version "1.4.32"
    // Use shadow to shade in CommandAPI
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    // Using these repositories for CommandAPI
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/")
    // StickyAPI repo
    maven {
        credentials {
            username = "${property("ghUser")}"
            password = "${property("ghPass")}"
        }
        url = uri("https://maven.pkg.github.com/DumbDogDiner/StickyAPI/")
    }
    // CommandAPI repo
    maven("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/")
    mavenLocal()
}

dependencies {
    // Require Paper, as that's the server fork we use
    compileOnly(paper())
    // Require our API
    implementation(project(":sass-api"))
    // Add CommandAPI annotations
    compileOnly("dev.jorel:commandapi-annotations:5.11")
    kapt("dev.jorel:commandapi-annotations:5.11")
    // Shade CommandAPI into plugin
    implementation("dev.jorel:commandapi-shade:5.11")
    // Add necessary database libraries
    // TODO: need hikaricp
    implementation("org.jetbrains.exposed:exposed-core:0.31.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.31.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    implementation("org.postgresql:postgresql:42.2.20")
    // Add Jackson JSON and CBOR for converting between the two formats
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.12.3")
    // Add StickyAPI for GUI
    implementation("com.dumbdogdiner:stickyapi-bukkit:3.0.2")
    implementation("com.dumbdogdiner:stickyapi-common:3.0.2")

    implementation("org.projectlombok:lombok:1.18.18")
    annotationProcessor("org.projectlombok:lombok:1.18.18")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("")
    }

    spigot {
        // Use StickyWallet to award Miles for completing challenges
        depends = listOf("StickyWallet")
    }
}

kapt {
    includeCompileClasspath = false
}
