import kr.entree.spigradle.kotlin.paper

plugins {
    java
    id("kr.entree.spigradle")
}

dependencies {
    // runtime dependencies
    compileOnly(paper())
    // utility dependencies
    implementation("org.projectlombok:lombok:1.18.18")
    annotationProcessor("org.projectlombok:lombok:1.18.18")
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
