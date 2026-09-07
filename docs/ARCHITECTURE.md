# Architecture

## System overview

```text
Android Client
    |
    | HTTP / JSON
    v
Ktor Backend
    |
    +-- Catalog repository
    |
    +-- Bundled catalog JSON
    |
    +-- Static audio resources
```

The repository is organized as a full-stack application:

```text
mini-spotify/
├── frontend/
│   └── android/
├── backend/
├── docs/
├── README.md
└── CHANGELOG.md
```

## Request flow

```text
User action
  -> Compose screen
  -> ViewModel
  -> Android repository
  -> Retrofit API
  -> Ktor route
  -> MusicRepository
  -> JSON resource
  -> typed response
  -> ViewModel StateFlow
  -> Compose recomposition
```

Favorite persistence is local to the Android application:

```text
Compose
  -> PlaylistViewModel
  -> FavoriteAlbumRepository
  -> AlbumDao
  -> Room
```

Playback flow:

```text
Playlist screen
  -> PlayerViewModel
  -> ExoPlayer
  -> GET /songs/{file}
  -> Ktor static media route
```

## Frontend responsibilities

### Compose UI

Screens render immutable UI state and forward user actions to ViewModels.
Network and database clients are not accessed directly from UI code.

### ViewModels

ViewModels own screen state with `StateFlow`, launch coroutines, and translate
repository results into loading, success, and error states.

### Android repositories

Repositories isolate data-access concerns from the UI layer. Retrofit and Room
are kept behind repository interfaces/classes so transport and persistence
details do not leak into screens.

### Player integration

ExoPlayer is provided through dependency injection and consumed by the
playback ViewModel and persistent player bar.

## Backend responsibilities

### Ktor routes

Routes define HTTP contracts, validate path parameters, and map repository
results to HTTP responses.

### MusicRepository

`MusicRepository` owns catalog loading and typed JSON decoding. The current
resource-backed implementation can later be replaced by a database without
changing the public route contracts.

### Configuration

The backend reads `HOST` and `PORT` from environment variables and defaults to
`0.0.0.0:8080`.

### Error handling and logging

Ktor `CallLogging` records incoming requests. `StatusPages` converts unhandled
server exceptions into controlled JSON error responses.

## API surface

| Method | Endpoint | Responsibility |
|---|---|---|
| GET | `/` | Service metadata |
| GET | `/health` | Health check |
| GET | `/feed` | Home catalog sections |
| GET | `/playlists` | Playlist collection |
| GET | `/playlist/{id}` | Playlist lookup |
| GET | `/songs/{file}` | Audio delivery |

## Package compatibility with V1

The backend intentionally preserves the original V1 identifiers:

```text
Gradle project: spotify_backend
Gradle group:   com.example
Main class:     com.example.ApplicationKt
Package root:   com.example
```

V2 changes the repository layout and expands the application into a full-stack
system, while keeping the backend package contract continuous with V1.

The Android client uses:

```text
com.example.spotify
```

## Future production extensions

The current design leaves clear extension points for:

- PostgreSQL or another persistent backend datastore;
- authentication and user-scoped favorites;
- object storage and CDN-backed media delivery;
- pagination and catalog search;
- Docker and Kubernetes deployment;
- CI/CD;
- metrics, distributed tracing, and centralized logs;
- API versioning.
