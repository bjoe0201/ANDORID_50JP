# 五十音練習 (ANDORID_50JP)

![Version](https://img.shields.io/badge/version-1.1-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![Min SDK](https://img.shields.io/badge/minSdk-31-orange)

日語五十音（平假名／片假名）單字聯想抽認卡 Android App。
將網頁版五十音練習工具移植為原生 Android 應用，支援 TTS 日語發音、手勢滑動與離線使用。

---

## 版本紀錄

| 版本 | versionCode | 日期 | 說明 |
|------|-------------|------|------|
| 1.1  | 2           | 2026-04-22 | 橫向雙欄版面、發音邏輯調整、移除音量鍵翻頁 |
| 1.0  | 1           | 2026-04-22 | 初始版本，完整移植網頁版功能 |

---

## 功能

- 平假名 / 片假名切換
- 每張卡片顯示：字、羅馬拼音、Emoji、單字、中文意思
- TTS 日語發音（需安裝 Google 日文語音包）
- 左右滑動切換卡片（卡片內提示）
- 點擊卡片發音（字母＋單字，不受自動發音開關影響）
- 自動發音開關（進入新卡片時自動播放字母＋單字）
- 隨機模式
- 顯示／隱藏羅馬拼音
- 橫向自適應雙欄版面
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
| 手勢 | detectHorizontalDragGestures |
| Build | Gradle Kotlin DSL |

---

## FAQ

### 點擊卡片或開啟自動發音後沒有聲音，怎麼辦？

需要安裝 **Google 日文 TTS 語音包**，步驟如下：

**方法一：透過系統設定安裝（推薦）**

1. 開啟手機「**設定**」
2. 進入「**一般管理**」→「**語言與輸入法**」（部分機型路徑略有不同）
3. 點選「**文字轉語音**」（Text-to-Speech）
4. 選擇「**Google 文字轉語音引擎**」，點選旁邊的齒輪圖示 ⚙️
5. 點選「**安裝語音資料**」
6. 找到「**日文（日本）**」，點選下載圖示 ⬇️
7. 下載完成後返回 App，重新點擊卡片即可發音

**方法二：透過 Google Play 安裝**

1. 開啟 Google Play 商店
2. 搜尋「**Google 文字轉語音**」
3. 進入 App 頁面，點選「**管理**」→「**語言**」
4. 新增「**日文**」語音包並下載

> 建議選用「**Neural（神經網路）**」品質的語音包，發音更自然。

---

## 授權

[MIT](LICENSE)
