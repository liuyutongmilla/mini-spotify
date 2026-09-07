# Music Streaming Platform Backend

Backend service for the Music Streaming Platform, built with Kotlin and Ktor.

## Tech Stack

- Kotlin 1.8
- Ktor 2.2.4
- Netty
- kotlinx.serialization
- Gradle
- JDK 17

## Architecture

The backend runs as a single Ktor application that handles REST API routing,
playlist and feed data retrieval, JSON serialization, and static audio delivery.

```text
Android Client
      |
      | HTTP / Retrofit
      v
Ktor Application
      |
      +-- REST API
      |
      +-- JSON Resources
      |
      +-- Static Audio Resources
```

## Project Structure

```text
spotify_backend/
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/
│   └── wrapper/
└── src/
    ├── main/
    │   ├── kotlin/com/example/
    │   │   └── Application.kt
    │   └── resources/
    │       ├── feed.json
    │       ├── playlists.json
    │       └── static/
    │           └── songs/
    └── test/
        └── kotlin/com/example/
            └── ApplicationTest.kt
```

## REST API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Returns backend status |
| GET | `/feed` | Returns home feed data |
| GET | `/playlists` | Returns all playlists |
| GET | `/playlist/{id}` | Returns a playlist by ID |
| GET | `/songs/{file}` | Serves audio content |

## Requirements

- JDK 17

Gradle installation is not required because the project includes the Gradle Wrapper.

## Build

```bash
./gradlew clean test build
```

## Run

```bash
./gradlew run
```

The server runs at:

```text
http://localhost:8080
```

## Test

```bash
./gradlew test
```