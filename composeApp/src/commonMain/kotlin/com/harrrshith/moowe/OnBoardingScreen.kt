package com.harrrshith.moowe

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harrrshith.moowe.theme.FontFamily
import kotlinx.coroutines.delay
import moowe.composeapp.generated.resources.Res
import moowe.composeapp.generated.resources.app_name
import moowe.composeapp.generated.resources.next
import org.jetbrains.compose.resources.stringResource


@Composable
fun OnBoardingRoute() {
    WonderfulGradientBackground {
        OnBoardingScreen()
    }
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
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = modifier
                .offset { IntOffset(x = 0, y = animatedOffsetY.toInt()) }
                .alpha(animatedAlpha),
            text = stringResource(Res.string.app_name),
            style = TextStyle(
                fontFamily = FontFamily,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 42.sp
            ),
            textAlign = TextAlign.Center,
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
                text = stringResource(Res.string.next),
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
fun WonderfulGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    //https://github.com/Rahkeen/Arcdroid/blob/main/app/src/main/java/co/rikin/arcdroid/MainActivity.kt
    val brush1 = Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer))
    val brush2 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer))
    val brush3 = Brush.horizontalGradient(colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.tertiaryContainer))
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val circle1XAxis = createCircle1XAxisAnimation(infiniteTransition)
    val circle1YAxis = createCircleYAxisAnimation(infiniteTransition)
    val circle1Scale = createCircle1ScaleAnimation(infiniteTransition)
    val circle2YAxis = createCircle2YAxisAnimation(infiniteTransition)
    val circle2Scale = createCircle2ScaleAnimation(infiniteTransition)
    val circle3XAxis = createCircle3XAxisAnimation(infiniteTransition)
    val circle3YAxis = createCircle3YAxisAnimation(infiniteTransition)
    val circle3Scale = createCircle3ScaleAnimation(infiniteTransition)

    Box {
        Box(
            modifier = modifier
                .fillMaxSize()
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
                }
        )
        content()
    }
}

//AnimationSpecs
const val CIRCLE1_TRANSLATION_DURATION = 24000
const val CIRCLE1_SCALE_DURATION = 13456
const val CIRCLE2_TRANSLATION_DURATION = 15000
const val CIRCLE2_SCALE_DURATION = 16235
const val CIRCLE3_TRANSLATION_DURATION = 10000
const val CIRCLE3_SCALE_DURATION = 9875

val createCircle1XAxisAnimation : @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE1_TRANSLATION_DURATION
                .5f at (CIRCLE1_TRANSLATION_DURATION * .25).toInt() using LinearEasing
                1.0f at (CIRCLE1_TRANSLATION_DURATION * .5).toInt() using LinearEasing
                .5f at (CIRCLE1_TRANSLATION_DURATION * .75).toInt() using LinearEasing
                .25f at CIRCLE1_TRANSLATION_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1XAxis"
    )
}

val createCircleYAxisAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = .25f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE1_TRANSLATION_DURATION
                .5f at (CIRCLE1_TRANSLATION_DURATION * .25).toInt() using LinearEasing
                .5f at (CIRCLE1_TRANSLATION_DURATION * .5).toInt() using LinearEasing
                1.0f at (CIRCLE1_TRANSLATION_DURATION * .75).toInt() using LinearEasing
                .25f at CIRCLE1_TRANSLATION_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1YAxis"
    )
}

val createCircle1ScaleAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = CIRCLE1_SCALE_DURATION
                0.8f at (CIRCLE1_SCALE_DURATION * .33).toInt() using LinearEasing
                1.2f at (CIRCLE1_SCALE_DURATION * .66).toInt() using LinearEasing
                1.0f at CIRCLE1_SCALE_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle1Scale"
    )
}

val createCircle2YAxisAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE2_TRANSLATION_DURATION
                0f at (CIRCLE2_TRANSLATION_DURATION * .25).toInt() using LinearEasing
                .5f at (CIRCLE2_TRANSLATION_DURATION * .5).toInt() using LinearEasing
                1.0f at (CIRCLE2_TRANSLATION_DURATION * .75).toInt() using LinearEasing
                .5f at CIRCLE2_TRANSLATION_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2YAxis"
    )
}

val createCircle2ScaleAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = CIRCLE2_SCALE_DURATION
                1.2f at (CIRCLE2_SCALE_DURATION * .33).toInt() using LinearEasing
                1.1f at (CIRCLE2_SCALE_DURATION * .66).toInt() using LinearEasing
                1.0f at CIRCLE2_SCALE_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle2Scale"
    )
}

val createCircle3XAxisAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE3_TRANSLATION_DURATION
                0f at (CIRCLE3_TRANSLATION_DURATION * 0.25).toInt() using LinearEasing
                1.0f at CIRCLE3_TRANSLATION_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "circle3XAxis"
    )
}

val createCircle3YAxisAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE3_TRANSLATION_DURATION
                0f at (CIRCLE3_TRANSLATION_DURATION * 0.5).toInt() using LinearEasing
                1.0f at CIRCLE3_TRANSLATION_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3YAxis"
    )
}

val createCircle3ScaleAnimation: @Composable (InfiniteTransition) -> State<Float> = { infiniteTransition ->
    infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = CIRCLE3_SCALE_DURATION
                1.1f at (CIRCLE3_SCALE_DURATION * .33).toInt() using LinearEasing
                0.85f at (CIRCLE3_SCALE_DURATION * .66).toInt() using LinearEasing
                1.0f at CIRCLE3_SCALE_DURATION using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "circle3Scale"
    )
}