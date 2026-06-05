@file:Suppress("UnstableApiUsage")

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
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

	iosX64()
	iosArm64()
	iosSimulatorArm64()

	jvm()

	applyDefaultHierarchyTemplate()

	sourceSets {
		commonMain.dependencies {
			compileOnly(libs.androidx.lifecycle.viewmodel.savedstate)
			compileOnly(libs.kotlinx.coroutines.core)
			compileOnly(libs.vanniktech.blurhash)

			implementation(libs.compose.multiplatform.runtime) // Necessary because of the Compose Compiler plugin
		}

		commonTest.dependencies {
			implementation(libs.kotlin.test)
			implementation(libs.androidx.lifecycle.viewmodel.savedstate)
		}

		androidMain.dependencies {
			compileOnly(libs.androidx.appcompat)
			compileOnly(libs.androidx.exif)

			compileOnly(project.dependencies.platform(libs.compose.bom))
			compileOnly(libs.androidx.activity.compose)

			compileOnly(libs.compose.ui)
			compileOnly(libs.compose.ui.unit)
			compileOnly(libs.compose.foundation)
			compileOnly(libs.compose.material3)
			compileOnly(libs.compose.ui.tooling)
			compileOnly(libs.compose.ui.tooling.preview)
		}

		iosMain.dependencies {}

		jvmMain.dependencies {}
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
