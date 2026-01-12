plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "IosKmpSample"
            isStatic = true
        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation("devmugi.sample:sample-shared") // Shared app and UI components
            implementation(libs.ktor.client.darwin)
        }
    }
}
