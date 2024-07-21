plugins {
	java
	id("org.flywaydb.flyway") version "9.0.1"
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
}

extra["springAiVersion"] = "0.8.1"
group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
	implementation("org.flywaydb:flyway-core:9.0.1")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.7.2")
	implementation ("commons-io:commons-io:2.11.0")
	implementation("org.modelmapper:modelmapper:3.1.0")


	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
