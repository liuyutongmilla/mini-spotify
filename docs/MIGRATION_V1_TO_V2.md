# V1 to V2 Migration

V1 is the existing Ktor backend repository. V2 keeps that backend identity and
expands the repository into a full-stack layout.

## Target layout

```text
mini-spotify/
├── frontend/
│   └── android/
├── backend/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle/
│   ├── gradlew
│   ├── gradlew.bat
│   └── src/
├── docs/
├── README.md
└── CHANGELOG.md
```

## Important Git-history rule

When applying V2 to the existing V1 checkout, move V1 files with `git mv`
instead of deleting and recreating them. This keeps the V1 -> V2 history easy
to review.

Example:

```bash
mkdir -p backend
git mv build.gradle.kts backend/
git mv settings.gradle.kts backend/
git mv gradle backend/
git mv gradlew backend/
git mv gradlew.bat backend/
git mv src backend/
```

Then copy the V2 backend source additions into `backend/`, add
`frontend/android/`, `docs/`, and the updated root documentation.

Do not run `git init` again. The existing repository history and `origin`
remote should remain in place.
