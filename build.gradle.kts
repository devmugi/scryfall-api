import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.JavadocJar

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
}

group = "io.github.devmugi"
version = "1.0.1"

kotlin {
    jvmToolchain(21)

    jvm()

    androidLibrary {
        namespace = "devmugi.mtgcards.scryfall.api"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "scryfallApiKit"
        }
    }

    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        nodejs()

        // Use ES modules for modern bundler compatibility (Vite, webpack, etc.)
        useEsModules()

        // Produce distributable JS library for npm
        binaries.library()

        // Enable TypeScript declarations for npm consumers
        generateTypeScriptDefinitions()

        // Configure package.json for npm
        compilations["main"].packageJson {
            customField("name", "@devmugi/scryfall-api")
            customField("author", "devmugi")
            customField("version", "1.0.0")
            customField("description", "Kotlin Multiplatform library for Scryfall MTG API")
            customField(
                "keywords",
                listOf("magic", "scryfall", "mtg", "cards", "api", "kotlin", "multiplatform")
            )
            customField("license", "MIT")
            customField(
                "repository",
                mapOf(
                    "type" to "git",
                    "url" to "https://github.com/devmugi/scryfall-api"
                )
            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Kotlin
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)

            // Kotlin serialization
            implementation(libs.kotlinx.serialization.json)

            // Ktor - network
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        // JVM test dependencies (for unit tests)
        jvmTest.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}

// Detekt configuration
detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$projectDir/detekt.yml"))

    // Analyze all Kotlin source files including tests
    source.setFrom(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin",
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin",
        "src/jsMain/kotlin",
    )

    // Fail build on issues to enforce code quality
    // Set to true locally if needed: ./gradlew detekt -PdetektIgnoreFailures=true
    ignoreFailures = project.hasProperty("detektIgnoreFailures")
}

// Configure JVM target for all Detekt tasks
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "21"
}

// Configure JVM target for baseline tasks as well
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "21"
}

// Create separate detekt tasks
tasks.register("detektAll") {
    group = "verification"
    description = "Run detekt analysis on all source sets"
    dependsOn(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>())
}

// Ktlint configuration
ktlint {
    version.set(libs.versions.ktlintVersion)

    // Enable Android-specific rules
    // Code style is set via .editorconfig (ktlint_code_style = intellij_idea)
    android.set(true)

    // Output format
    outputToConsole.set(true)
    coloredOutput.set(true)

    // Experimental rules: disabled for stability
    // Rule configuration is in .editorconfig
    enableExperimentalRules.set(true)

    // Configure source sets to lint
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        include("**/kotlin/**")
    }

    // Fail build on issues to enforce code quality
    // Bypass locally: ./gradlew ktlintCheck -PktlintIgnoreFailures=true
    ignoreFailures.set(project.hasProperty("ktlintIgnoreFailures"))
}

// Create separate ktlint tasks
tasks.register("ktlintCheckAll") {
    group = "verification"
    description = "Run ktlint checks on all source sets"
    dependsOn(tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask>())
}

tasks.register("ktlintFormatAll") {
    group = "formatting"
    description = "Run ktlint formatting on all source sets"
    dependsOn(tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask>())
}

// Maven Central publishing configuration
mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "scryfall-api", version.toString())

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
            sourcesJar = true,  // include sources
        )
    )

    pom {
        name.set("Scryfall API")
        description.set("Kotlin Multiplatform library for Scryfall MTG API")
        inceptionYear.set("2025")
        url.set("https://github.com/devmugi/scryfall-api")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("devmugi")
                name.set("devmugi")
                url.set("https://github.com/devmugi")
            }
        }

        scm {
            url.set("https://github.com/devmugi/scryfall-api")
            connection.set("scm:git:git://github.com/devmugi/scryfall-api.git")
            developerConnection.set("scm:git:ssh://github.com/devmugi/scryfall-api.git")
        }
    }
}

// npm publishing configuration
tasks.register<Copy>("copyNpmReadme") {
    group = "devmugi"
    description = "Copy npm README to distribution"
    from("docs/npm/README.md")
    into(layout.buildDirectory.dir("dist/js/productionLibrary"))
}

tasks.named("jsNodeProductionLibraryDistribution") {
    finalizedBy("copyNpmReadme")
}

tasks.named("jsBrowserProductionLibraryDistribution") {
    finalizedBy("copyNpmReadme")
}

tasks.register("prepareNpmPublish") {
    group = "devmugi"
    description = "Kotlin Multiplatform library for Scryfall MTG API"
    dependsOn("jsNodeProductionLibraryDistribution")

    doLast {
        val distDir = layout.buildDirectory.dir("dist/js/productionLibrary").get().asFile
        println("npm package ready at: ${distDir.absolutePath}")
        println("To publish locally: cd ${distDir.absolutePath} && npm link")
        println("To publish to npm: cd ${distDir.absolutePath} && npm publish")
    }
}
