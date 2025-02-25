import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.12.1"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("hexlet.code.App")
    applicationName = "app"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // environment
    implementation("io.github.cdimascio:java-dotenv:5.2.2")

    // db
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // web
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("gg.jte:jte:3.1.9")

    // parsing html
    implementation("org.jsoup:jsoup:1.18.3")

    // Mock web
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.konghq:unirest-java-core:4.4.5")

    // logging
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // javalin
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")

    // tests
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

// Настройка Jacoco
jacoco {
    toolVersion = "0.8.11"
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

// Задача генерации отчёта
tasks.withType<JacocoReport> {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("**/*Test.class")
            }
        })
    )
    sourceDirectories.setFrom(files("$projectDir/src/main/java"))
}

// Настройка задачи test
tasks.withType<Test> {
    //environment("env", "test")
    //environment("JDBC_DATABASE_URL", "jdbc:h2:mem:project")

    //environment("DB_USERNAME", "")
    //environment("DB_PASSWORD", "")

    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
        showStandardStreams = true
    }
}
