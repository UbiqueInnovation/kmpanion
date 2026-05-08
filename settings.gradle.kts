enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ub-kmpanion"

pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
	}
}

dependencyResolutionManagement {
	repositories {
		google()
		mavenCentral()
	}
}

include(":kmpanion")
project(":kmpanion").projectDir = file("lib")
