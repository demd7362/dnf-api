import com.github.gradle.node.npm.task.NpxTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.node-gradle.node") version "7.0.1"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.20" // 모든 entity에 no arg constructor 생성
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

group = "com.taecho"
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
allOpen { // 기본적으로 final로 선언되는 클래스들을 런타임 프록시 생성을 위해 열어주기 위함
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    // default
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // web
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // aop
    implementation("org.springframework:spring-aop")
    implementation("org.springframework:spring-aspects")

    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.1")

    // database
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    testImplementation("org.springframework.security:spring-security-test")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
