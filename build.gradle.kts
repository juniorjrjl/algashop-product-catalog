import org.gradle.internal.extensions.core.extra

plugins {
	java
	`maven-publish`
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.spring.cloud.contract)
	alias(libs.plugins.asciidoctor.convert)
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

val mockitoAgent: Configuration = configurations.create("mockitoAgent")

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
	// IMPLEMENTATION
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.webmvc)

	// ASCIIDOCTOR EXTENSION
	"asciidoctorExt"(libs.spring.restdocs.asciidoctor)

	// COMPILE ONLY
	compileOnly(libs.lombok)

	// ANNOTATION PROCESSOR
	annotationProcessor(libs.lombok)
	annotationProcessor(libs.lombok.mapstruct.binding)

	// TEST COMPILE / ANNOTATION PROCESSOR
	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)

	// TEST IMPLEMENTATION
	testImplementation(libs.spring.boot.starter.validation.test)
	testImplementation(libs.spring.boot.starter.webmvc.test)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.cloud.starter.contract.verifier)
	testImplementation(libs.rest.assured.spring.mock.mvc)
	testImplementation(libs.datafaker)
	testImplementation(libs.spring.restdocs.mockmvc)

	// MOCKITO AGENT & RUNTIME
	mockitoAgent(libs.mockito.core) {
		isTransitive = false
	}
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
	jvmArgs(
		"-javaagent:${configurations.getByName("mockitoAgent").asPath}"
	)
}

tasks.contractTest {
	outputs.dir(project.property("snippetsDir") as File)
	useJUnitPlatform()

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
