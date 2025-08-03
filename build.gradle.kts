@file:Suppress("UnstableApiUsage", "PropertyName")

plugins {
	id("java")
	id("fabric-loom") version "1.11-SNAPSHOT"
	id("ploceus") version "1.11-SNAPSHOT"
}

val mod_group: String by project
val mod_name: String by project
val mod_version: String by project

val minecraft_version: String by project
val feather_build: String by project
val raven_build: String by project
val sparrow_build: String by project
val nests_build: String by project
val osl_version: String by project
val mod_menu_version: String by project
val legacy_lwjgl_3_version: String by project

val loader_version: String by project

group = mod_group
base.archivesName.set(mod_name)
version = mod_version

loom {
	clientOnlyMinecraftJar()
}
ploceus {
	clientOnlyMappings()
}
repositories {
	maven { url = uri("https://moehreag.duckdns.org/maven/snapshots") }
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "SignalumMavenInfrastructure"
        url = uri("https://maven.thesignalumproject.net/infrastructure")
    }
    maven {
        name = "SignalumMavenReleases"
        url = uri("https://maven.thesignalumproject.net/releases")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
	mappings(ploceus.featherMappings(feather_build))
	exceptions(ploceus.raven(raven_build)) // remove this line if not using exceptions patches
	signatures(ploceus.sparrow(sparrow_build)) // remove this line if not using generics patches
	nests(ploceus.nests(nests_build)) // remove this line if not using inner class patches
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
	modImplementation("com.terraformersmc:modmenu:$mod_menu_version")
	modImplementation("io.github.moehreag:legacy-lwjgl3:$legacy_lwjgl_3_version")
	ploceus.dependOsl(osl_version, "client")

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.compileJava {
    options.release.set(17)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}

configurations.configureEach {

}

tasks.processResources {
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}
