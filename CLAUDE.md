# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

This is a standard Android Gradle project. Use Android Studio or the Gradle wrapper:

```bash
# Debug build
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run a single unit test class
./gradlew test --tests "com.example.andorid_50jp.ExampleUnitTest"

# Lint
./gradlew lint
```

Install to connected device:
```bash
./gradlew installDebug
```

## Architecture

**MVVM** with Jetpack Compose UI. Single-module app (`:app`).

```
viewmodel/FlashcardViewModel.kt   — AndroidViewModel, holds all UI state via StateFlow
data/KanaData.kt                  — Static data: KanaItem data class + kanaList (46 entries)
ui/FlashcardScreen.kt             — All Compose UI; orientation-aware layout
ui/theme/                         — Color.kt, Theme.kt, Type.kt
MainActivity.kt                   — Entry point; passes shared ViewModel to FlashcardScreen
```

### State (`FlashcardUiState`)

All state lives in one `data class` inside the ViewModel:

| Field | Purpose |
|---|---|
| `currentIndex` | Index into `displayOrder` |
| `displayOrder` | List of kanaList indices (shuffled or sequential) |
| `mode` | `HIRAGANA` / `KATAKANA` |
| `autoPlayEnabled` | Auto-play TTS on card navigation |
| `audioEnabled` | Master mute (🔊 button) |
| `showRomaji` | Show/hide romaji overlay |
| `isShuffle` | Shuffle mode |
| `navDirection` | `1` (forward) / `-1` (backward) for slide animation |

Extension property `FlashcardUiState.currentItem` resolves the current `KanaItem`.

### TTS Audio logic

- `playCurrentCardAudio(force = false)`: auto-play on navigation — only fires when `autoPlayEnabled && audioEnabled && ttsAvailable`
- `playCurrentCardAudio(force = true)`: called on card tap — always plays (char + word) if `audioEnabled && ttsAvailable`
- TTS is initialized in `ViewModel.init` using `AndroidViewModel` application context; language set to `Locale.JAPANESE`

### Orientation layout

`FlashcardScreen` detects orientation via `LocalConfiguration.current.orientation`:
- **Portrait**: single-column, full header, 80sp kana character
- **Landscape**: two-column Row (card left 55%, controls right 45%), compact header, 48sp kana character

### Key design decisions

- All 46 kana items are hardcoded static data in `KanaData.kt` — no database
- Colours are defined as named constants in `ui/theme/Color.kt` (Blue500, Slate600, etc.)
- Swipe gesture uses `detectHorizontalDragGestures` with a 80px threshold
- Slide animation uses `AnimatedContent` keyed on `currentIndex to navDirection`
