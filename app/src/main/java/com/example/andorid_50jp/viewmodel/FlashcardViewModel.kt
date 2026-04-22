package com.example.andorid_50jp.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.andorid_50jp.data.KanaItem
import com.example.andorid_50jp.data.kanaList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

enum class KanaMode { HIRAGANA, KATAKANA }

data class FlashcardUiState(
    val currentIndex: Int = 0,
    val mode: KanaMode = KanaMode.HIRAGANA,
    val displayOrder: List<Int> = (kanaList.indices).toList(),
    val isShuffle: Boolean = false,
    val showRomaji: Boolean = true,
    val audioEnabled: Boolean = true,
    val autoPlayEnabled: Boolean = false,
    val navDirection: Int = 1,  // 1 = forward, -1 = backward
    val ttsAvailable: Boolean = false,
    val ttsError: Boolean = false
)

val FlashcardUiState.currentItem: KanaItem
    get() = kanaList[displayOrder[currentIndex]]

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    private var tts: TextToSpeech? = null

    init {
        initTts(application)
    }

    private fun initTts(application: Application) {
        tts = TextToSpeech(application) { status ->
            viewModelScope.launch {
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.JAPANESE)
                    val available = result != TextToSpeech.LANG_MISSING_DATA &&
                            result != TextToSpeech.LANG_NOT_SUPPORTED
                    _uiState.update { it.copy(ttsAvailable = available, ttsError = !available) }
                } else {
                    _uiState.update { it.copy(ttsAvailable = false, ttsError = true) }
                }
            }
        }
    }

    fun nextCard() {
        _uiState.update { state ->
            val next = (state.currentIndex + 1) % kanaList.size
            state.copy(currentIndex = next, navDirection = 1)
        }
        playCurrentCardAudio()
    }

    fun prevCard() {
        _uiState.update { state ->
            val prev = (state.currentIndex - 1 + kanaList.size) % kanaList.size
            state.copy(currentIndex = prev, navDirection = -1)
        }
        playCurrentCardAudio()
    }

    fun setMode(mode: KanaMode) {
        _uiState.update { it.copy(mode = mode) }
        playCurrentCardAudio()
    }

    fun toggleShuffle() {
        _uiState.update { state ->
            val newShuffle = !state.isShuffle
            val newOrder = if (newShuffle) {
                (kanaList.indices).toMutableList().also { it.shuffle() }
            } else {
                (kanaList.indices).toList()
            }
            state.copy(isShuffle = newShuffle, displayOrder = newOrder, currentIndex = 0)
        }
        playCurrentCardAudio()
    }

    fun toggleRomaji() {
        _uiState.update { it.copy(showRomaji = !it.showRomaji) }
    }

    fun toggleAudio() {
        _uiState.update { state ->
            val newEnabled = !state.audioEnabled
            if (!newEnabled) tts?.stop()
            state.copy(audioEnabled = newEnabled)
        }
    }

    fun toggleAutoPlay() {
        _uiState.update { it.copy(autoPlayEnabled = !it.autoPlayEnabled) }
    }

    fun playCurrentCardAudio(force: Boolean = false) {
        val state = _uiState.value
        if (!state.audioEnabled || !state.ttsAvailable) return
        if (!force && !state.autoPlayEnabled) return
        val item = state.currentItem
        val char = if (state.mode == KanaMode.HIRAGANA) item.hira else item.kata
        speak(char, flush = true)
        speak(item.word, flush = false)
    }

    private fun speak(text: String, flush: Boolean) {
        val mode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts?.speak(text, mode, null, UUID.randomUUID().toString())
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onCleared()
    }
}