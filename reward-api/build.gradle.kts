import kr.entree.spigradle.kotlin.paper

plugins {
    java
    id("kr.entree.spigradle")
}

dependencies {
    compileOnly(paper())
    implementation(project(":stats-api")) // TODO merge stats and rewards
    implementation("org.jetbrains:annotations:16.0.2")
}

spotless {
    java {
        importOrder()
        prettier(
            mapOf(
                "prettier" to "2.2.1",
                "prettier-plugin-java" to "1.0.2"
            )
        ).config(
            mapOf(
                "parser" to "java",
                "tabWidth" to 4
            )
        )
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    }
}

tasks {
    generateSpigotDescription {
        enabled = false
    }
}
