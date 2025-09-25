/*
 * Copyright (c) 2025 Pham The Minh
 * All rights reserved.
 * Project: TTS implementation
 * File: settings.gradle.kts
 * Last Modified: 25/9/2025 8:55
 */

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TTS implementation"
include(":app")
