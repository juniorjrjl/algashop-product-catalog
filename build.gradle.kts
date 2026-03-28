plugins {
	java
	id("maven-publish")
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.springframework.cloud.contract") version "5.0.0"
}

group = "com.algaworks.algashop"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

val mockitoAgent: Configuration = configurations.create("mockitoAgent")
configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
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

extra["springCloudVersion"] = "2025.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")

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
	useJUnitPlatform()

}
