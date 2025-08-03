pluginManagement {
	repositories {
		gradlePluginPortal()
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		maven {
			name = "Jitpack"
			url = uri("https://jitpack.io")
		}
		maven {
			name = "Ornithe Releases"
			url = uri("https://maven.ornithemc.net/releases")
		}
		maven {
			name = "Ornithe Snapshots"
			url = uri("https://maven.ornithemc.net/snapshots")
		}
		maven {
			name = "SignalumMavenInfrastructure"
			url = uri("https://maven.thesignalumproject.net/infrastructure")
		}
	}
}
