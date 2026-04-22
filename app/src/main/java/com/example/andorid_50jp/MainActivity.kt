package com.example.andorid_50jp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import com.example.andorid_50jp.ui.FlashcardScreen
import com.example.andorid_50jp.ui.theme.JP50Theme
import com.example.andorid_50jp.viewmodel.FlashcardViewModel

class MainActivity : ComponentActivity() {

    private val vm: FlashcardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JP50Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    FlashcardScreen(vm = vm)
                }
            }
        }
    }


}
