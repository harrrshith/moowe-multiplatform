package com.harrrshith.moowe.ui.onBoarding

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import moowe.composeapp.generated.resources.Res
import moowe.composeapp.generated.resources.app_name
import moowe.composeapp.generated.resources.next
import org.jetbrains.compose.resources.stringResource

val primaryColor = Color(0xFF5D2F9E) //purple
val primaryColorOffShade = Color(0xFF71DFE3) //teal
val secondaryColor = Color(0xFF3BBFC4) //teal dark
val secondaryColorOffShade = Color(0xFF5E62A6)//violet
val tertiaryColor = Color(0xFF4E1A8E) //dark violet
val tertiaryColorOffShade = Color(0xFF3BBFC4) //teal dark
@Composable
fun OnBoardingRoute() {
    WonderfulGradientBackground(
        modifier = Modifier
//            .systemBarsPadding()
            .fillMaxSize()
    ) {
        OnBoardingScreen()
    }
}

@Composable
private fun OnBoardingScreen(
    modifier: Modifier = Modifier
){
    // Define constants for animation values
    val animationDelay = 500L
    val alphaAnimationDelay = 200L
    val initialOffsetY = 1500f
    val targetOffsetY = 0f
    val initialAlpha = 0.5f
    val targetAlpha = 1f
    val initialButtonAlpha = 0f
    val targetButtonAlpha = 1f
    val initialButtonWidth = 0f
    val targetButtonWidth = 180f
    val initialButtonHeight = 0f
    val targetButtonHeight = 50f

    // Combine animation states into a single variable
    var animationProgress by remember { mutableStateOf(0) }

    val animatedOffsetY by animateFloatAsState(
        targetValue = if (animationProgress > 0) targetOffsetY else initialOffsetY,
        animationSpec = spring(
            dampingRatio = .8f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetY"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (animationProgress > 1) targetAlpha else initialAlpha,
        label = "alpha"
    )

    val animatedButtonAlpha by animateFloatAsState(
        targetValue = if (animationProgress > 2) targetButtonAlpha else initialButtonAlpha,
        label = "alpha"
    )

    val animatedButtonWidth by animateFloatAsState(
        targetValue = if (animationProgress > 2) targetButtonWidth else initialButtonWidth,
        label = "width"
    )

    val animatedButtonHeight by animateFloatAsState(
        targetValue = if (animationProgress > 2) targetButtonHeight else initialButtonHeight,
        label = "height"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = animatedOffsetY.toInt()) }
                .alpha(animatedAlpha),
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            fontSize = 42.sp,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .alpha(animatedButtonAlpha)
                .height(animatedButtonHeight.dp)
                .width(animatedButtonWidth.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(Res.string.next),
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(animationDelay)
        animationProgress = 1
        delay(alphaAnimationDelay)
        animationProgress = 2
        delay(alphaAnimationDelay)
        animationProgress = 3
    }
}

@Composable
fun WonderfulGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    //https://github.com/Rahkeen/Arcdroid/blob/main/app/src/main/java/co/rikin/arcdroid/MainActivity.kt
    val brush1 = remember { Brush.verticalGradient(colors = listOf(primaryColor, primaryColorOffShade, secondaryColor, secondaryColorOffShade)) }
    val brush2 = remember { Brush.horizontalGradient(colors = listOf(secondaryColor, secondaryColorOffShade, primaryColor, primaryColorOffShade)) }
    val brush3 = remember { Brush.horizontalGradient(colors = listOf(tertiaryColor, tertiaryColorOffShade)) }
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
                .background(color = MaterialTheme.colorScheme.surface)
                .blur(radius = 120.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
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
const val CIRCLE3_TRANSLATION_DURATION = 8000
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
        initialValue = .1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE1_TRANSLATION_DURATION
                .4f at (CIRCLE1_TRANSLATION_DURATION * .25).toInt() using LinearEasing
//                .5f at (CIRCLE1_TRANSLATION_DURATION * .5).toInt() using LinearEasing
//                1.0f at (CIRCLE1_TRANSLATION_DURATION * .75).toInt() using LinearEasing
                .1f at CIRCLE1_TRANSLATION_DURATION using LinearEasing
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
        initialValue = 0.25f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE2_TRANSLATION_DURATION
                0f at (CIRCLE2_TRANSLATION_DURATION * .25).toInt() using LinearEasing
//                .5f at (CIRCLE2_TRANSLATION_DURATION * .5).toInt() using LinearEasing
//                1.0f at (CIRCLE2_TRANSLATION_DURATION * .75).toInt() using LinearEasing
                .25f at CIRCLE2_TRANSLATION_DURATION using LinearEasing
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
        initialValue = .35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CIRCLE3_TRANSLATION_DURATION
                0f at (CIRCLE3_TRANSLATION_DURATION * 0.35).toInt() using LinearEasing
                .35f at CIRCLE3_TRANSLATION_DURATION using LinearEasing
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