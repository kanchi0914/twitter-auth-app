import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


/**
 *
 * https://radiochemical.hatenablog.com/entry/2019/09/25/190500
 */


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

	/*jackson*/
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.8.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
	implementation("com.fasterxml.jackson.core:jackson-core:2.10.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.10.1")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.10.1")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.1")
	implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.10.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.1")


	testImplementation("org.springframework.security:spring-security-test")
}


tasks.register("npmRunBuild"){
	doFirst(){

		fun String.runCommand0(workingDir: File) {
			ProcessBuilder(*split(" ").toTypedArray())
					.directory(workingDir)
					.redirectOutput(ProcessBuilder.Redirect.INHERIT)
					.redirectError(ProcessBuilder.Redirect.INHERIT)
					.start()
					.waitFor(60, TimeUnit.MINUTES)
		}

		fun String.runCommand(workingDir: File): String? {
			try {
				val parts = this.split("\\s".toRegex())
				val proc = ProcessBuilder(*parts.toTypedArray())
						.directory(workingDir)
						.redirectOutput(ProcessBuilder.Redirect.PIPE)
						.redirectError(ProcessBuilder.Redirect.PIPE)
						.start()

				proc.waitFor(60, TimeUnit.MINUTES)
				return proc.inputStream.bufferedReader().readText()
			} catch(e: java.io.IOException) {
				e.printStackTrace()
				return null
			}
		}
		fun String.runCommand10(workingDir: File = file("./")): String {
			val parts = this.split("\\s".toRegex())
			val proc = ProcessBuilder(*parts.toTypedArray())
					.directory(workingDir)
					.redirectOutput(ProcessBuilder.Redirect.PIPE)
					.redirectError(ProcessBuilder.Redirect.PIPE)
					.start()

			proc.waitFor(1, TimeUnit.MINUTES)
			return proc.inputStream.bufferedReader().readText().trim()
		}

//		fun myprint(){
//			println("aaaaa!!")
//		}


		"npm run build".runCommand(File("./web/twitter-app-web"))
		//println("dsadasda")
		//"npm --prefix ${rootDir}/web run build".runCommand0(File("./out"))

//		println("aaaa")
//		val aa = ""
//		myprint()
		//"ls".runCommand("./")
//		Runtime.getRuntime().exec("echo aaaaa")
//				.outputStream(System.out)


	}


}

//task npmRunBuild() {
//	if (!file("${rootDir}/web/node_modules").exists()) {
//		"npm --prefix ${rootDir}/web install ${rootDir}/web"
//				.execute()
//				.waitForProcessOutput(System.out, System.err)
//	}
//	"npm --prefix ${rootDir}/web run build"
//			.execute()
//			.waitForProcessOutput(System.out, System.err)
//}
//
//processResources {
//	dependsOn npmRunBuild
//}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
