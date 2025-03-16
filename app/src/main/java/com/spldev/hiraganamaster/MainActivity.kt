package com.spldev.hiraganamaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hiraganamaster.ui.HiraganaPracticeScreen
import com.google.mlkit.vision.digitalink.Ink
import com.spldev.hiraganamaster.viewmodel.HiraganaViewModel
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = HiraganaViewModel()
        viewModel.setupDigitalInkRecognition(this)

        setContent {
            HiraganaPracticeScreen(
                viewModel = viewModel,
                onVerifyDrawing = {
                    val inkBuilder = Ink.builder()
                    viewModel.strokes.forEach { stroke ->
                        val strokeBuilder = Ink.Stroke.builder()
                        stroke.forEach { point ->
                            strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
                        }
                        inkBuilder.addStroke(strokeBuilder.build())
                    }
                    val ink = inkBuilder.build()

                    viewModel.verifyDrawing(this, ink)
                },
                onNextCharacter = {
                    viewModel.changeCharacter()
                },
                onClearCanvas = {
                    viewModel.clearCanvas()
                }
            )
        }
    }
}
