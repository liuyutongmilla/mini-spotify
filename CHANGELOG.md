# Changelog

## v2.0.0

- Reorganized the repository into explicit `frontend/` and `backend/`
  application boundaries.
- Added the Android client under `frontend/android/`.
- Preserved the V1 backend Gradle project name `spotify_backend`.
- Preserved the V1 backend package `com.example` and entry point
  `com.example.ApplicationKt`.
- Added Compose-based UI with MVVM and `StateFlow`.
- Added Retrofit/OkHttp networking and coroutine-based API calls.
- Added Room persistence for favorite albums.
- Added Hilt dependency injection.
- Added ExoPlayer-based audio playback.
- Split backend responsibilities into configuration, model, repository, and
  route layers.
- Added environment-based backend host/port configuration.
- Added request logging, centralized exception handling, and `/health`.
- Added backend repository and application tests.
- Added full-stack architecture and V1-to-V2 migration documentation.

## v1.0.0

- Initial Ktor backend.
- Added feed and playlist endpoints.
- Added static audio delivery.
- Added JSON-backed catalog resources.
- Established backend project name `spotify_backend`.
- Established backend package namespace `com.example`.
