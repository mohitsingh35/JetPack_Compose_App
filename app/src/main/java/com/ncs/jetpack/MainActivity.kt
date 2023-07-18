package com.ncs.jetpack

import android.app.Activity
import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.MovableContentState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.core.view.WindowCompat
import com.ncs.jetpack.ui.theme.DarkColorScheme
import com.ncs.jetpack.ui.theme.JetPackTheme
import com.ncs.jetpack.ui.theme.LightColorScheme
import com.ncs.jetpack.ui.theme.Typography
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToggleSwitch()
        }
    }
}
@Composable
fun ToggleSwitch() {
    var isChecked by remember { mutableStateOf(false) }
    JetPackTheme(isChecked) {
        Row(modifier = Modifier
            .fillMaxSize()
            .background(
                color = animateColorAsState(
                targetValue = colorScheme.primary,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = {
                        OvershootInterpolator(4f).getInterpolation(it)
                    })
            ).value)
            .padding(top = 300.dp, start = 80.dp)
        ) {
            val transition = updateTransition(targetState = isChecked, label = "toggle")
            val offsetX by transition.animateFloat(
                transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) }, label = ""
            ) { isChecked ->
                if (isChecked) 280f else 0f
            }
            val constraints= ConstraintSet {
                val lightimage=createRefFor("lightImage")
                val toggle=createRefFor("toggle")
                val darkimage=createRefFor("darkImage")

                constrain(lightimage){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                constrain(toggle){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                constrain(darkimage){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            }
            ConstraintLayout(constraints,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) colorResource(id = R.color.light).copy(alpha = 0.9f) else colorResource(
                            id = R.color.dark
                        )
                    )
                    .border(4.dp, colorResource(id = R.color.purple_200), CircleShape)
                    .height(100.dp)
                    .width(200.dp)
                    .toggleable(value = isChecked, onValueChange = { isChecked = it })
            ) {
                AnimatedVisibility(
                    visible = isChecked,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ),
                    modifier = Modifier
                        .animateContentSize()
                        .layoutId("lightImage")
                        .padding(top = 16.dp, start = 25.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.sun),
                        contentDescription = "Image Description",
                        modifier = Modifier
                            .size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .size(100.dp)
                        .clip(CircleShape)
                        .layoutId("toggle")
                        .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                        .background(colorResource(id = R.color.purple_200)),
                    contentAlignment = Alignment.Center,
                ) {
//                    Text(if (isChecked) "ON" else "OFF", color = Color.Black)
                }
                AnimatedVisibility(
                    visible = !isChecked,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ),
                    modifier = Modifier
                        .animateContentSize()
                        .layoutId("darkImage")
                        .padding(top = 20.dp, end = 25.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.moon),
                        contentDescription = "Image Description",
                        modifier = Modifier
                            .size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

