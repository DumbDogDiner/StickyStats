import kr.entree.spigradle.kotlin.jitpack
import kr.entree.spigradle.kotlin.paper
import kr.entree.spigradle.kotlin.vault

plugins {
    id("kr.entree.spigradle")
    kotlin("jvm") version "1.4.32"
    // Use kapt for CommandAPI annotations
    kotlin("kapt") version "1.4.32"
    // Use shadow to shade in CommandAPI
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    // Using Jitpack for VaultAPI
    jitpack()
    // Using these repositories for CommandAPI
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/")
}

dependencies {
    compileOnly(paper())
    // Require our API
    implementation(project(":sass-api"))
    // Add vault for rewards
    compileOnly(vault())
    // Add CommandAPI annotations
    compileOnly("dev.jorel:commandapi-annotations:5.11")
    kapt("dev.jorel:commandapi-annotations:5.11")
    // Shade CommandAPI into plugin
    implementation("dev.jorel:commandapi-shade:5.11")
    // Add necessary database libraries
    implementation("org.jetbrains.exposed:exposed-core:0.31.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.31.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    implementation("org.postgresql:postgresql:42.2.20")
    // Add Jackson JSON and CBOR for converting between the two formats
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.12.3")
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
        // Vault is required, as rewards are an essential part of this plugin
        depends = listOf("Vault")
    }
}

kapt {
    includeCompileClasspath = false
}
