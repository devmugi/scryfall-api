rootProject.name = "ios-kmp-sample"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

// Include shared module via composite build
includeBuild("../sample-shared") {
    dependencySubstitution {
        substitute(module("devmugi.sample:sample-shared")).using(project(":"))
    }
}
