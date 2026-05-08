@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.android.library)
	alias(libs.plugins.vanniktech.publish)
}

kotlin {
	jvmToolchain(21)

	android {
		namespace = "ch.ubique.libs.kmpanion"
		minSdk = 23
		compileSdk {
			version = release(36) {
				minorApiLevel = 1
			}
		}

		optimization {
			consumerKeepRules.apply {
				publish = true
				file("consumer-rules.pro")
			}
		}

		withHostTest {}
	}

	val xcf = XCFramework()
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "UbiqueKmpanion"
			isStatic = true
			xcf.add(this)
		}
	}

	applyDefaultHierarchyTemplate()

	sourceSets {
		commonMain.dependencies {
			implementation(libs.kotlinx.coroutines.core)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
		androidMain.dependencies {
		}
		iosMain.dependencies {
		}
	}

	compilerOptions {
		freeCompilerArgs.add("-Xexpect-actual-classes")
	}
}

tasks.withType(Test::class) {
	testLogging {
		setEvents(listOf("standardOut", "passed", "skipped", "failed"))
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}
}

mavenPublishing {
	coordinates(version = project.version.toString())
	publishToMavenCentral(true)
	signAllPublications()
}
