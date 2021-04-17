import kr.entree.spigradle.kotlin.paper

plugins {
	id("kr.entree.spigradle")
	kotlin("jvm") version "1.4.32"
}

dependencies {
	compileOnly(paper())
}

tasks {
	compileKotlin {
		kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
	}
}
