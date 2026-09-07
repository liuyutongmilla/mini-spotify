package com.example.config

/** Server bind configuration, sourced from the environment with local defaults. */
data class AppConfig(
    val host: String,
    val port: Int
) {
    companion object {
        fun fromEnvironment(): AppConfig = AppConfig(
            host = System.getenv("HOST") ?: "0.0.0.0",
            port = System.getenv("PORT")?.toIntOrNull() ?: 8080
        )
    }
}
