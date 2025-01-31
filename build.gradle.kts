import com.github.gradle.node.npm.task.NpxTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.node-gradle.node") version "7.0.1"
}
node {
    download = true
    /**
     * node version
     */
    version = "20.10.0"

    /**
     * npm version
     */
    npmVersion = "10.2.3"
    nodeProjectDir = file("${projectDir}/src/main/resources/static")
}


val tailwindCss = tasks.register<NpxTask>("tailwindcss") {
    command.set("tailwind")
    args.set(listOf("-i", "${projectDir}/src/main/resources/static/css/tailwinds.css", "-o", "${projectDir}/src/main/resources/static/css/tailwind-out.css"))
    dependsOn(tasks.npmInstall)
}
tasks.processResources {
    dependsOn(tailwindCss)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        kotlin {
            srcDirs("src/main/kotlin")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
        kotlin {
            srcDirs("src/test/kotlin")
        }
    }
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework:spring-aop")
    implementation("org.springframework:spring-aspects")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
