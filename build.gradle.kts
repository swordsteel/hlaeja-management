plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.certificate)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.fasterxml.jackson.module.kotlin)
    implementation(hlaeja.jjwt.api)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.library.hlaeja.jwt)
    implementation(hlaeja.projectreactor.kotlin.reactor.extensions)
    implementation(hlaeja.springboot.starter.actuator)
    implementation(hlaeja.springboot.starter.security)
    implementation(hlaeja.springboot.starter.thymeleaf)
    implementation(hlaeja.springboot.starter.webflux)
    implementation(hlaeja.thymeleaf.spring.security)

    testImplementation(hlaeja.assertj.core)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.mockk)
    testImplementation(hlaeja.projectreactor.reactor.test)
    testImplementation(hlaeja.springboot.starter.test)

    testRuntimeOnly(hlaeja.junit.platform.launcher)
}

group = "ltd.lulz"

tasks {
    named("processResources") {
        dependsOn("copyCertificates")
    }
}
