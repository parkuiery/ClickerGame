package com.uiel.clickergame

import android.media.SoundPool
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uiel.clickergame.component.ClickToShakeText
import com.uiel.clickergame.component.NicknameTextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val homeViewModel: HomeViewModel = viewModel()
    val uiState by homeViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        //homeViewModel.register()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
    ) {
        var isClick by remember { mutableStateOf(false) }
        val image = if (isClick) R.drawable.mouth_open else R.drawable.mouth_close
        val context = LocalContext.current
        val soundPool = remember {
            SoundPool.Builder()
                .setMaxStreams(1)
                .build()
        }
        val soundId = remember {
            soundPool.load(context, R.raw.clicksound, 1) // res/raw에 sound 파일 추가!
        }
        val coroutineScope = rememberCoroutineScope()
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val scale by infiniteTransition.animateFloat(
            initialValue = if (isClick) 1f else 1f,
            targetValue = if (isClick) 1.2f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(100, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Text(text = stringResource(id = R.string.now_rank) +" : ${uiState.rank}")
        Spacer(modifier = Modifier.height(40.dp))
        NicknameTextField(
            text = uiState.nickName,
            onTextChange = {
                homeViewModel.updateNickName(it)
            },
            initEdit = uiState.isNew,
            onCheckClick = homeViewModel::register
        )
        if (uiState.isNicknameBad) {
            Text(
                text = "해당 닉네임은 금지된 단어를 포함하고 있거나 중복되어있습니다.",
                color = Color.Red
            )
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !uiState.isNew, onClick = { })
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            //view.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)
                            homeViewModel.click()
                            coroutineScope.launch(Dispatchers.IO) {
                                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                            }
                            homeViewModel.updateScore()
                            isClick = true
                            tryAwaitRelease()
                            isClick = false
                        }
                    )
                },
            painter = painterResource(image),
            contentDescription = null,
        )
        ClickToShakeText(
            text = NumberFormat.getNumberInstance(Locale.US).format(uiState.score),
            isShaking = isClick,
        )
    }
}