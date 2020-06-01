import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
//	id("com.google.cloud.tools.appengine") version "2.1.0" // 追加
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	google()
	jcenter()
}

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	//implementation("org.springframework.social:spring-social-twitter:1.1.2.RELEASE")
//	implementation("org.springframework.social:spring-social-security:1.1.6.RELEASE")
//	implementation("org.springframework.social:spring-social-config:1.1.6.RELEASE")
	//implementation("org.springframework.social:spring-social-core")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	implementation("com.google.cloud:google-cloud-language:1.19.4")
//	implementation("com.googlecode.json-simple:json-simple:1.1.1")
	compile ("com.google.cloud:google-cloud-language:1.100.0")
	//compile("com.google.cloud:google-cloud-language")
	//runtime(group = "com.google.cloud", name = "google-cloud-language")
	implementation(fileTree("libs/twitter4j-core-4.0.7.jar"))
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
