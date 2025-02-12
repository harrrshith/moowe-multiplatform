package com.harrrshith.moowe

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay


@Composable
fun OnBoardingRoute() {
    WonderfulGradientBackground()
    OnBoardingScreen()
}

@Composable
private fun OnBoardingScreen(
    modifier: Modifier = Modifier
){
    var startAnimation by remember { mutableStateOf(false) }
    var startAlpha by remember { mutableStateOf(false) }
    var startButtonAlpha by remember { mutableStateOf(false) }
    val animatedOffsetY by  animateFloatAsState(
        targetValue = if (startAnimation) 0f else 1500f,
        label = "offsetY"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (startAlpha) 1f else .5f,
        label = "alpha"
    )
    val animatedButtonAlpha by animateFloatAsState(
        targetValue = if (startButtonAlpha) 1f else 0f,
        label = "alpha"
    )
    val animatedButtonWidth by animateFloatAsState(
        targetValue = if (startButtonAlpha) 180f else 0f,
        label = "width"
    )
    val animatedButtonHeight by animateFloatAsState(
        targetValue = if (startButtonAlpha) 50f else 0f,
        label = "width"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Moowe",
            textAlign = TextAlign.Center,
            modifier = modifier
                .offset { IntOffset(x = 0, y = animatedOffsetY.toInt()) }
                .alpha(animatedAlpha)
        )
        Button(
            onClick = {  },
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .alpha(animatedButtonAlpha)
                .height(animatedButtonHeight.dp)
                .width(animatedButtonWidth.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1000)
        startAnimation = !startAnimation
        delay(200)
        startAlpha = !startAlpha
        delay(200)
        startButtonAlpha = !startButtonAlpha
    }
}

@Composable
fun GradientBackground(
    modifier: Modifier,
    content:  @Composable () -> Unit
){
    val brush1 = Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer))
    val brush2 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer))
    val brush3 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.tertiaryContainer))
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

    val circle1TranslationDuration = 24000
    val circle1XAxis = infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle1TranslationDuration
                .5f at (circle1TranslationDuration * .25).toInt() using LinearEasing
                1.0f at (circle1TranslationDuration * .5).toInt() using LinearEasing
                .5f at (circle1TranslationDuration * .75).toInt() using LinearEasing
                .25f at circle1TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1XAxis"
    )
    val circle1YAxis = infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle1TranslationDuration
                .5f at (circle1TranslationDuration * .25).toInt() using LinearEasing
                .5f at (circle1TranslationDuration * .5).toInt() using LinearEasing
                1.0f at (circle1TranslationDuration * .75).toInt() using LinearEasing
                .25f at circle1TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1YAxis"
    )

    val circle1ScaleDuration = 13456
    val circle1Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle1ScaleDuration
                0.8f at (circle1ScaleDuration * .33).toInt() using LinearEasing
                1.2f at (circle1ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle1ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1Scale"
    )

    val circle2TranslationDuration = 15000
    val circle2YAxis = infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle2TranslationDuration
                0f at (circle2TranslationDuration * .25).toInt() using LinearEasing
                .5f at (circle2TranslationDuration * .5).toInt() using LinearEasing
                1.0f at (circle2TranslationDuration * .75).toInt() using LinearEasing
                .5f at circle2TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2YAxis"
    )
    val circle2ScaleDuration = 16235
    val circle2Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle2ScaleDuration
                1.2f at (circle2ScaleDuration * .33).toInt() using LinearEasing
                1.1f at (circle2ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle2ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2Scale"
    )

    val circle3TranslationDuration = 10000
    val circle3XAxis = infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle3TranslationDuration
                0f at (circle3TranslationDuration * 0.25).toInt() using LinearEasing
                1.0f at circle3TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "circle3XAxis"
    )
    val circle3YAxis = infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle3TranslationDuration
                0f at (circle3TranslationDuration * 0.5).toInt() using LinearEasing
                1.0f at circle3TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3YAxis"
    )
    val circle3ScaleDuration = 9875
    val circle3Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle3ScaleDuration
                1.1f at (circle3ScaleDuration * .33).toInt() using LinearEasing
                0.85f at (circle3ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle3ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3Scale"
    )

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .blur(radius = 100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .drawBehind {
                val circle1Offset = Offset(
                    size.width * circle1XAxis.value,
                    size.height * circle1YAxis.value,
                )
                drawCircle(
                    brush = brush1,
                    radius = size.minDimension * .70f * circle1Scale.value,
                    center = circle1Offset,
                    alpha = 0.75f
                )
                val circle2Offset = Offset(
                    size.width * .5f,
                    size.height * circle2YAxis.value
                )
                drawCircle(
                    brush = brush2,
                    radius = size.minDimension * .725f * circle2Scale.value,
                    center = circle2Offset,
                    alpha = 0.75f
                )

                val circle3Offset = Offset(
                    size.width * circle3XAxis.value,
                    size.height * circle3YAxis.value,
                )
                drawCircle(
                    brush = brush3,
                    radius = size.minDimension * .75f * circle3Scale.value,
                    center = circle3Offset,
                    alpha = 0.75f
                )
            }.zIndex(0f)
    ) {
        content()
    }
}

@Composable
fun WonderfulGradientBackground(
    modifier: Modifier = Modifier
) {
    //https://github.com/Rahkeen/Arcdroid/blob/main/app/src/main/java/co/rikin/arcdroid/MainActivity.kt
    val brush1 = Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer))
    val brush2 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer))
    val brush3 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.tertiaryContainer))
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

    val circle1TranslationDuration = 24000
    val circle1XAxis = infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle1TranslationDuration
                .5f at (circle1TranslationDuration * .25).toInt() using LinearEasing
                1.0f at (circle1TranslationDuration * .5).toInt() using LinearEasing
                .5f at (circle1TranslationDuration * .75).toInt() using LinearEasing
                .25f at circle1TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1XAxis"
    )
    val circle1YAxis = infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle1TranslationDuration
                .5f at (circle1TranslationDuration * .25).toInt() using LinearEasing
                .5f at (circle1TranslationDuration * .5).toInt() using LinearEasing
                1.0f at (circle1TranslationDuration * .75).toInt() using LinearEasing
                .25f at circle1TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1YAxis"
    )

    val circle1ScaleDuration = 13456
    val circle1Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle1ScaleDuration
                0.8f at (circle1ScaleDuration * .33).toInt() using LinearEasing
                1.2f at (circle1ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle1ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1Scale"
    )

    val circle2TranslationDuration = 15000
    val circle2YAxis = infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle2TranslationDuration
                0f at (circle2TranslationDuration * .25).toInt() using LinearEasing
                .5f at (circle2TranslationDuration * .5).toInt() using LinearEasing
                1.0f at (circle2TranslationDuration * .75).toInt() using LinearEasing
                .5f at circle2TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2YAxis"
    )
    val circle2ScaleDuration = 16235
    val circle2Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle2ScaleDuration
                1.2f at (circle2ScaleDuration * .33).toInt() using LinearEasing
                1.1f at (circle2ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle2ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2Scale"
    )

    val circle3TranslationDuration = 10000
    val circle3XAxis = infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle3TranslationDuration
                0f at (circle3TranslationDuration * 0.25).toInt() using LinearEasing
                1.0f at circle3TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "circle3XAxis"
    )
    val circle3YAxis = infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = circle3TranslationDuration
                0f at (circle3TranslationDuration * 0.5).toInt() using LinearEasing
                1.0f at circle3TranslationDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3YAxis"
    )
    val circle3ScaleDuration = 9875
    val circle3Scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = circle3ScaleDuration
                1.1f at (circle3ScaleDuration * .33).toInt() using LinearEasing
                0.85f at (circle3ScaleDuration * .66).toInt() using LinearEasing
                1.0f at circle3ScaleDuration using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3Scale"
    )

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .blur(radius = 100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .drawBehind {
                val circle1Offset = Offset(
                    size.width * circle1XAxis.value,
                    size.height * circle1YAxis.value,
                )
                drawCircle(
                    brush = brush1,
                    radius = size.minDimension * .70f * circle1Scale.value,
                    center = circle1Offset,
                    alpha = 0.75f
                )
                val circle2Offset = Offset(
                    size.width * .5f,
                    size.height * circle2YAxis.value
                )
                drawCircle(
                    brush = brush2,
                    radius = size.minDimension * .725f * circle2Scale.value,
                    center = circle2Offset,
                    alpha = 0.75f
                )

                val circle3Offset = Offset(
                    size.width * circle3XAxis.value,
                    size.height * circle3YAxis.value,
                )
                drawCircle(
                    brush = brush3,
                    radius = size.minDimension * .75f * circle3Scale.value,
                    center = circle3Offset,
                    alpha = 0.75f
                )
            }.zIndex(0f)
    ){
        OnBoardingScreen(
            modifier = Modifier
                .zIndex(1f)
        )
    }
}