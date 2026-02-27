# Moowe

A modern movie and TV discovery app built with **Compose Multiplatform** and **Kotlin Multiplatform**.

Moowe shares most of its UI and business logic across platforms while keeping platform-specific entry points for Android, iOS, and Desktop.

## What It Does

- Discover movies by genre
- Browse trending media
- Search movies quickly
- View rich detail pages (cast, reviews, related titles, seasons)
- Save favorites and recent searches locally

## Tech Stack

- Kotlin Multiplatform (KMP)
- Compose Multiplatform
- Ktor (networking)
- Koin (dependency injection)
- Room + SQLite (local persistence)
- Paging (infinite scrolling)
- Coil 3 (image loading)

## Platforms

- Android (`androidApp`)
- iOS (`iosApp` + shared framework from `composeApp`)
- Desktop JVM (`composeApp`)

## Project Structure

```text
.
|- androidApp/      # Android launcher app
|- composeApp/      # Shared app module (UI, data, domain, DI)
|- image-carousel/  # Reusable multiplatform image carousel library
`- iosApp/          # iOS Xcode host app
```

## Getting Started

### Prerequisites

- JDK 17+
- Android Studio (latest stable)
- Xcode (for iOS builds)

### Clone and Build

```bash
git clone <your-repo-url>
cd moowe
./gradlew build
```

## Run the App

### Android

```bash
./gradlew :androidApp:installDebug
```

### Desktop

```bash
./gradlew :composeApp:run
```

### iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode.
2. Select a simulator/device.
3. Run the `iosApp` target.

## API Configuration

The app integrates with TMDB APIs.

- API constants are currently located in `composeApp/src/commonMain/kotlin/com/harrrshith/moowe/Constants.kt`.
- For production use, move secrets out of source code and inject them using secure configuration.

## Useful Gradle Tasks

```bash
./gradlew :composeApp:test
./gradlew :androidApp:assembleRelease
./gradlew :composeApp:packageDistributionForCurrentOS
```
