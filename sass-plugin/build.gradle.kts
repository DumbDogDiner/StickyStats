import kr.entree.spigradle.kotlin.jitpack
import kr.entree.spigradle.kotlin.paper
import kr.entree.spigradle.kotlin.vault

plugins {
    id("java-library")
    id("kr.entree.spigradle")
    kotlin("jvm") version "1.4.32"
    // Use kapt for CommandAPI annotations
    kotlin("kapt") version "1.4.32"
    id("idea")
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
    implementation("org.projectlombok:lombok:1.18.18")
    annotationProcessor("org.projectlombok:lombok:1.18.18")
    implementation("org.jetbrains:annotations:16.0.2")
    compileOnly(paper())
    // Require our API
    // Add vault for rewards
    compileOnly(vault())
    // Add CommandAPI annotations
    compileOnly("dev.jorel:commandapi-annotations:5.11")
    kapt("dev.jorel:commandapi-annotations:5.11")
    // Shade CommandAPI into plugin
    implementation("dev.jorel:commandapi-shade:5.11")
}
apply(from = "sourcesets.gradle")
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
