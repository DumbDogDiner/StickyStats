import kr.entree.spigradle.kotlin.paper

plugins {
	id("kr.entree.spigradle")
	kotlin("jvm") version "1.4.32"
	// Use kapt for CommandAPI annotations
	kotlin("kapt") version "1.4.32"
	// Use shadow to shade in CommandAPI
	id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
	// Using these repositories for CommandAPI
	maven("https://repo.codemc.org/repository/maven-public")
	maven("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/")
}

dependencies {
	compileOnly(paper())
	implementation(project(":reward-api"))
	// Add CommandAPI annotations
	compileOnly("dev.jorel:commandapi-annotations:5.11")
	kapt("dev.jorel:commandapi-annotations:5.11")
	// Shade CommandAPI into plugin
	implementation("dev.jorel:commandapi-shade:5.11")
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
}

kapt {
	includeCompileClasspath = false
}