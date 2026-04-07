import org.gradle.internal.extensions.core.extra

plugins {
	java
	id("maven-publish")
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.springframework.cloud.contract") version "5.0.0"
	id("org.asciidoctor.jvm.convert") version "4.0.5"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

extra.apply {
	set("springCloudVersion", "2025.1.0")
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
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")

	"asciidoctorExt"("org.springframework.restdocs:spring-restdocs-asciidoctor")


	compileOnly("org.projectlombok:lombok")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	testCompileOnly("org.projectlombok:lombok")

	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.rest-assured:spring-mock-mvc:5.5.7")
	testImplementation("net.datafaker:datafaker:2.5.4")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	mockitoAgent("org.mockito:mockito-core"){
		isTransitive = false
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
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
