# Mini Spotify

Mini Spotify is a full-stack music streaming application built with Kotlin.
V2 expands the original Ktor backend into a complete Android + backend system
while preserving the backend package and Gradle identity established in V1.

## V2 architecture

```text
Mini Spotify
├── frontend/
│   └── android/
│       ├── Jetpack Compose
│       ├── MVVM + StateFlow
│       ├── Retrofit + OkHttp
│       ├── Room
│       ├── Hilt
│       └── ExoPlayer
│
└── backend/
    ├── Ktor
    ├── REST API
    ├── kotlinx.serialization
    ├── Repository layer
    ├── centralized error handling
    ├── request logging
    └── static media delivery
```

The Android application requests catalog data from the Ktor API and streams
audio through the backend's media endpoints. Favorite albums are persisted
locally with Room.

## Repository layout

```text
.
├── frontend/
│   └── android/          Android client
├── backend/              Ktor REST API and media server
├── docs/                 Architecture and migration notes
├── CHANGELOG.md
└── README.md
```

## V1 continuity

The backend keeps the same identifiers used by V1:

```text
rootProject.name = "spotify_backend"
group = "com.example"
mainClass = "com.example.ApplicationKt"
```

Backend Kotlin code therefore remains under `com.example`. The Android client
uses `com.example.spotify`.

## Backend API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | Service metadata |
| GET | `/health` | Health check |
| GET | `/feed` | Home catalog sections |
| GET | `/playlists` | All playlists |
| GET | `/playlist/{id}` | Playlist by id |
| GET | `/songs/{file}` | Audio delivery |

## Run the backend

From the repository root:

```bash
cd backend
./gradlew run
```

The service uses `0.0.0.0:8080` by default.

Environment overrides:

```bash
HOST=127.0.0.1 PORT=8081 ./gradlew run
```

Run backend tests:

```bash
./gradlew test
```

## Run the Android client

1. Start the backend on port `8080`.
2. Open `frontend/android/` in Android Studio.
3. Start an Android emulator.
4. Run the `app` configuration.

The emulator API base URL defaults to:

```text
http://10.0.2.2:8080/
```

`10.0.2.2` is the Android emulator alias for the development machine.

## Engineering boundaries

The frontend follows UI -> ViewModel -> Repository -> Retrofit/Room.

The backend follows Route -> Repository -> typed resource data.

This separation keeps UI, transport, persistence, HTTP routing, and catalog
loading responsibilities isolated and gives the project clear extension
points for a database, authentication, cloud media storage, CI/CD, and
containerized deployment.

See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the complete request
flow and responsibility boundaries.

For upgrading the existing Git repository from V1, follow
[`docs/MIGRATION_V1_TO_V2.md`](docs/MIGRATION_V1_TO_V2.md).

## Media

Only publish audio and artwork that you own or are licensed to distribute.
