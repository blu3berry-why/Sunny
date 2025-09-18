plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.sonarqube) // Important: do not use apply false!
    alias(libs.plugins.kover) apply false
}

sonarqube {
    properties {
        val koverReport = allprojects.mapNotNull { project ->
            val reportPath = "${project.projectDir}/build/reports/kover/report.xml"
            if (File(reportPath).exists()) reportPath else null
        }.joinToString(",")
        property("sonar.coverage.jacoco.xmlReportPaths", koverReport)
    }
}
tasks.named("sonar") {
    dependsOn(subprojects.map { it.tasks.named("koverXmlReport") })
}