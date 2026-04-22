package com.example.andorid_50jp.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andorid_50jp.data.KanaItem
import com.example.andorid_50jp.ui.theme.Blue100
import com.example.andorid_50jp.ui.theme.Blue200
import com.example.andorid_50jp.ui.theme.Blue50
import com.example.andorid_50jp.ui.theme.Blue500
import com.example.andorid_50jp.ui.theme.Blue600
import com.example.andorid_50jp.ui.theme.Indigo600
import com.example.andorid_50jp.ui.theme.Slate100
import com.example.andorid_50jp.ui.theme.Slate50
import com.example.andorid_50jp.ui.theme.Slate500
import com.example.andorid_50jp.ui.theme.Slate600
import com.example.andorid_50jp.ui.theme.Slate700
import com.example.andorid_50jp.ui.theme.Slate800
import com.example.andorid_50jp.ui.theme.White
import com.example.andorid_50jp.viewmodel.FlashcardUiState
import com.example.andorid_50jp.viewmodel.FlashcardViewModel
import com.example.andorid_50jp.viewmodel.KanaMode
import com.example.andorid_50jp.viewmodel.currentItem
import kotlinx.coroutines.delay

@Composable
fun FlashcardScreen(vm: FlashcardViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeLayout(state = state, vm = vm)
    } else {
        PortraitLayout(state = state, vm = vm)
    }
}

// ── Portrait layout ────────────────────────────────────────────────────────────

@Composable
private fun PortraitLayout(
    state: FlashcardUiState,
    vm: FlashcardViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // Header
        AppHeader(compact = false)

        // Scrollable body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModeToggle(mode = state.mode, onModeChange = vm::setMode, modifier = Modifier.weight(1f))
                AudioButton(enabled = state.audioEnabled, onClick = vm::toggleAudio)
            }

            SwipeableCard(state = state, vm = vm, cardFontSize = 80)

            NavButtons(vm = vm, buttonHeight = 52)

            SettingsPanel(
                showRomaji = state.showRomaji,
                isShuffle = state.isShuffle,
                wordAudioEnabled = state.wordAudioEnabled,
                currentIndex = state.currentIndex,
                total = state.displayOrder.size,
                onToggleRomaji = vm::toggleRomaji,
                onToggleShuffle = vm::toggleShuffle,
                onToggleWordAudio = vm::toggleWordAudio
            )

            TtsWarning(show = state.ttsError)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ── Landscape layout ───────────────────────────────────────────────────────────

@Composable
private fun LandscapeLayout(
    state: FlashcardUiState,
    vm: FlashcardViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // Left: compact header + card
        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
        ) {
            AppHeader(compact = true)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                SwipeableCard(state = state, vm = vm, cardFontSize = 48)
            }
        }

        // Right: controls (scrollable)
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModeToggle(mode = state.mode, onModeChange = vm::setMode, modifier = Modifier.weight(1f))
                AudioButton(enabled = state.audioEnabled, onClick = vm::toggleAudio)
            }

            NavButtons(vm = vm, buttonHeight = 44)

            SettingsPanel(
                showRomaji = state.showRomaji,
                isShuffle = state.isShuffle,
                wordAudioEnabled = state.wordAudioEnabled,
                currentIndex = state.currentIndex,
                total = state.displayOrder.size,
                onToggleRomaji = vm::toggleRomaji,
                onToggleShuffle = vm::toggleShuffle,
                onToggleWordAudio = vm::toggleWordAudio
            )

            TtsWarning(show = state.ttsError)
        }
    }
}

// ── Shared sub-composables ─────────────────────────────────────────────────────

@Composable
private fun AppHeader(compact: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Blue500, Indigo600)))
            .padding(
                horizontal = 24.dp,
                vertical = if (compact) 8.dp else 20.dp
            )
    ) {
        if (compact) {
            Text(
                text = "🎌 五十音聯想練習",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        } else {
            Column {
                Text(
                    text = "🎌 五十音聯想練習",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Text(
                    text = "透過單字建立記憶掛鉤",
                    fontSize = 13.sp,
                    color = Blue100,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun SwipeableCard(
    state: FlashcardUiState,
    vm: FlashcardViewModel,
    cardFontSize: Int
) {
    var totalDrag by remember { mutableFloatStateOf(0f) }
    AnimatedContent(
        targetState = state.currentIndex to state.navDirection,
        transitionSpec = {
            val dir = targetState.second
            slideInHorizontally(initialOffsetX = { it * dir }, animationSpec = tween(250)) togetherWith
                slideOutHorizontally(targetOffsetX = { -it * dir }, animationSpec = tween(250))
        },
        label = "cardTransition"
    ) { (_, _) ->
        FlashcardView(
            item = state.currentItem,
            mode = state.mode,
            showRomaji = state.showRomaji,
            charFontSize = cardFontSize,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { totalDrag = 0f },
                        onHorizontalDrag = { _, delta -> totalDrag += delta },
                        onDragEnd = {
                            when {
                                totalDrag < -80 -> vm.nextCard()
                                totalDrag > 80  -> vm.prevCard()
                            }
                            totalDrag = 0f
                        }
                    )
                },
            onTap = { vm.playCurrentCardAudio() }
        )
    }
}

@Composable
private fun NavButtons(vm: FlashcardViewModel, buttonHeight: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = vm::prevCard,
            modifier = Modifier.weight(1f).height(buttonHeight.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Slate100, contentColor = Slate600)
        ) {
            Text("← 上一個", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        Button(
            onClick = vm::nextCard,
            modifier = Modifier.weight(1f).height(buttonHeight.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue600, contentColor = White)
        ) {
            Text("下一個 →", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
private fun TtsWarning(show: Boolean) {
    if (show) {
        Text(
            text = "⚠️ 提示：若無聲音，請至系統設定 → 語言與輸入 → 文字轉語音，安裝「日文」語音包。",
            fontSize = 11.sp,
            color = Color(0xFF92400E),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF3C7), RoundedCornerShape(12.dp))
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

// ── Flashcard ─────────────────────────────────────────────────────────────────

@Composable
private fun FlashcardView(
    item: KanaItem,
    mode: KanaMode,
    showRomaji: Boolean,
    charFontSize: Int = 80,
    modifier: Modifier = Modifier,
    onTap: () -> Unit
) {
    var isFlashing by remember { mutableStateOf(false) }
    val borderColor = if (isFlashing) Blue500 else Blue200

    LaunchedEffect(isFlashing) {
        if (isFlashing) {
            delay(300)
            isFlashing = false
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Blue50)
            .border(2.dp, borderColor, RoundedCornerShape(28.dp))
            .clickable {
                isFlashing = true
                onTap()
            }
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Big character
            val displayChar = if (mode == KanaMode.HIRAGANA) item.hira else item.kata
            Text(
                text = displayChar,
                fontSize = charFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800,
                textAlign = TextAlign.Center
            )

            // Romaji
            Text(
                text = item.romaji.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Blue500.copy(alpha = if (showRomaji) 1f else 0f),
                letterSpacing = 3.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 16.dp),
                color = Blue200.copy(alpha = 0.6f)
            )

            // Emoji + word + meaning
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = item.emoji, fontSize = 44.sp)
                Column {
                    Text(
                        text = item.word,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700,
                        letterSpacing = 1.sp
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.meaning,
                            fontSize = 14.sp,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (showRomaji) "(${item.wordRomaji})" else "",
                            fontSize = 12.sp,
                            color = Blue500,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "點擊卡片發音",
                fontSize = 10.sp,
                color = Slate500,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

// ── Mode Toggle ───────────────────────────────────────────────────────────────

@Composable
private fun ModeToggle(
    mode: KanaMode,
    onModeChange: (KanaMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Slate100)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ModeTab(
            label = "平假名",
            selected = mode == KanaMode.HIRAGANA,
            onClick = { onModeChange(KanaMode.HIRAGANA) },
            modifier = Modifier.weight(1f)
        )
        ModeTab(
            label = "片假名",
            selected = mode == KanaMode.KATAKANA,
            onClick = { onModeChange(KanaMode.KATAKANA) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ModeTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (selected) Blue600 else Slate500
        )
    }
}

// ── Audio Button ──────────────────────────────────────────────────────────────

@Composable
private fun AudioButton(enabled: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) Color(0xFFDCFCE7) else Slate100)
    ) {
        Text(
            text = if (enabled) "🔊" else "🔇",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ── Settings Panel ────────────────────────────────────────────────────────────

@Composable
private fun SettingsPanel(
    showRomaji: Boolean,
    isShuffle: Boolean,
    wordAudioEnabled: Boolean,
    currentIndex: Int,
    total: Int,
    onToggleRomaji: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleWordAudio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Slate50)
            .border(1.dp, Slate100, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SettingsRow("顯示羅馬拼音", showRomaji, onToggleRomaji)
        SettingsRow("隨機模式",     isShuffle,  onToggleShuffle)
        SettingsRow("單字發音",     wordAudioEnabled, onToggleWordAudio)

        HorizontalDivider(
            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp),
            color = Slate100
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PROGRESS",
                fontSize = 10.sp,
                color = Slate500,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "${currentIndex + 1} / $total",
                fontSize = 13.sp,
                color = Blue600,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            )
        }
    }
}

@Composable
private fun SettingsRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Slate600, fontWeight = FontWeight.Medium)
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = Blue500,
                uncheckedThumbColor = White,
                uncheckedTrackColor = Slate100
            )
        )
    }
}
