plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    js(IR) {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation(libs.scryfall.api)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
