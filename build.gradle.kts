import org.gradle.internal.extensions.core.extra

plugins {
	alias(libs.plugins.asciidoctor.convert)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.cloud.contract)
	alias(libs.plugins.spring.dependency.management)
	jacoco
	java
	`maven-publish`
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

extra.apply {
	set("snippetsDir", file("build/generated-snippets"))
}

group = "com.algaworks.algashop"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

val mockitoAgent = configurations.create("mockitoAgent")

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	create("asciidoctorExt")
}

repositories {
	mavenCentral()
	mavenLocal()
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			artifact(tasks.named("bootJar"))
			artifact(tasks.named("verifierStubsJar"))
		}
	}
}

dependencies {

	"asciidoctorExt"(libs.spring.restdocs.asciidoctor)

	annotationProcessor(libs.lombok)
	annotationProcessor(libs.lombok.mapstruct.binding)
	annotationProcessor(libs.mapstruct.processor)

	compileOnly(libs.lombok)

	implementation(libs.commons.lang3)
	implementation(libs.mapstruct)
	implementation(libs.java.uuid.generator)
	implementation(libs.spring.boot.starter.data.mongodb)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.webmvc)

	mockitoAgent(libs.mockito.core) {
		isTransitive = false
	}

	testAnnotationProcessor(libs.lombok)

	testCompileOnly(libs.lombok)

	testImplementation(libs.datafaker)
	testImplementation(libs.rest.assured.spring.mock.mvc)
	implementation(libs.spring.boot.starter.data.mongodb.test)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.boot.starter.validation.test)
	testImplementation(libs.spring.boot.starter.webmvc.test)
	testImplementation(libs.spring.cloud.starter.contract.verifier)
	testImplementation(libs.spring.restdocs.mockmvc)

	testRuntimeOnly(libs.junit.platform.launcher)
}

dependencyManagement {
	imports {
		mavenBom(libs.spring.cloud.dependencies.get().toString())
	}
}

contracts {
	packageWithBaseClasses = "com.algaworks.algashop.product.catalog.contract.base"
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.contractTest {
	outputs.dir(project.property("snippetsDir") as File)
	useJUnitPlatform()
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
	finalizedBy(tasks.jacocoTestReport)
	systemProperty("test.seed", System.getProperty("test.seed") ?: "")
}

tasks.asciidoctor {
	dependsOn(tasks.contractTest)
	val snippetsFile = project.property("snippetsDir") as File
	inputs.dir(snippetsFile)
	attributes(mapOf("snippets" to snippetsFile))
	sources {
		include("index.adoc")
	}
	baseDirFollowsSourceFile()
	setOutputDir(file("build/docs/asciidoc"))
}
