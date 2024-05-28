plugins {
    kotlin("jvm") version "1.9.23"
}

group = "net.marvk"
version = "1.0-SNAPSHOT"

val jacksonModuleKotlinVersion = "2.17.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonModuleKotlinVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
