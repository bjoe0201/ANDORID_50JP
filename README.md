# 五十音練習 (ANDORID_50JP)

![Version](https://img.shields.io/badge/version-1.0-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![Min SDK](https://img.shields.io/badge/minSdk-31-orange)

日語五十音（平假名／片假名）單字聯想抽認卡 Android App。
將網頁版五十音練習工具移植為原生 Android 應用，支援 TTS 日語發音、手勢滑動與離線使用。

---

## 版本紀錄

| 版本 | versionCode | 日期 | 說明 |
|------|-------------|------|------|
| 1.0  | 1           | 2026-04-22 | 初始版本，完整移植網頁版功能 |

---

## 功能

- 平假名 / 片假名切換
- 每張卡片顯示：字、羅馬拼音、Emoji、單字、中文意思
- TTS 日語發音（需安裝 Google 日文語音包）
- 左右滑動切換卡片
- 隨機模式
- 顯示／隱藏羅馬拼音
- 單字發音開關
- 音量鍵 / 藍牙翻頁鍵支援
- 深色模式支援

---

## 系統需求

- Android 12（API 31）以上
- 建議安裝 Google TTS 日文語音包以取得最佳發音品質

---

## 技術堆疊

| 項目 | 使用技術 |
|------|---------|
| 語言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 架構 | MVVM (ViewModel + StateFlow) |
| 發音 | Android TextToSpeech (ja-JP) |
| 手勢 | HorizontalPager |
| Build | Gradle Kotlin DSL |

---

## 授權

[MIT](LICENSE)
