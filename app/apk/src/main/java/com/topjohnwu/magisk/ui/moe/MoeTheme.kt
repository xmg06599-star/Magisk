package com.topjohnwu.magisk.ui.moe

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 强调粉 */
val MoePink = Color(0xFFF0629A)

/** 玻璃卡片圆角 / 形状 */
val MoeGlassShape = RoundedCornerShape(20.dp)
val MoeHeroShape = RoundedCornerShape(24.dp)

/** 玻璃卡片底色（浅色模式半透白，深色模式半点白） */
@Composable
fun moeGlass(): Color = if (isSystemInDarkTheme()) {
    Color.White.copy(alpha = 0.10f)
} else {
    Color.White.copy(alpha = 0.62f)
}

/** 玻璃卡片描边 */
@Composable
fun moeGlassBorder(): Color = if (isSystemInDarkTheme()) {
    Color.White.copy(alpha = 0.14f)
} else {
    Color.White.copy(alpha = 0.75f)
}

/** 所有玻璃卡片的 colors */
@Composable
fun moeCardColors() = CardDefaults.cardColors(containerColor = moeGlass())

/** 大卡渐变 */
@Composable
fun moeHeroBrush(): Brush = if (isSystemInDarkTheme()) {
    Brush.linearGradient(listOf(Color(0xFF352941), Color(0xFF2A2438)))
} else {
    Brush.linearGradient(listOf(Color(0xFFFFE4F0), Color(0xFFF4E8FF)))
}

/** 大卡右下角那个勾 */
@Composable
fun MoeCheckMark(modifier: Modifier = Modifier) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .size(110.dp)
            .clip(CircleShape)
            .border(
                width = 9.dp,
                color = Color(0xFF7AC48C).copy(alpha = if (dark) 0.35f else 0.55f),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "✓",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6ABA80).copy(alpha = if (dark) 0.5f else 0.75f),
        )
    }
}

/** 读背景图（缩一半，省内存） */
@Composable
fun rememberMoeBg(): ImageBitmap? {
    val ctx = LocalContext.current
    return remember(ctx) {
        val f = MoePrefs.bgFile(ctx)
        if (!f.exists()) return@remember null
        runCatching {
            val opts = BitmapFactory.Options().apply { inSampleSize = 2 }
            BitmapFactory.decodeFile(f.absolutePath, opts)?.asImageBitmap()
        }.getOrNull()
    }
}

/** 花瓣 + 光斑 */
@Composable
private fun MoeDecor() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        fun glow(cx: Float, cy: Float, r: Float, a: Float) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = a), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = r,
                ),
                radius = r,
                center = Offset(cx, cy),
            )
        }
        glow(w * 0.80f, h * 0.08f, w * 0.55f, 0.55f)
        glow(w * 0.08f, h * 0.40f, w * 0.42f, 0.40f)
        glow(w * 0.65f, h * 0.82f, w * 0.50f, 0.35f)

        val petals = listOf(
            Triple(0.88f, 0.06f, 22f),
            Triple(0.74f, 0.19f, -28f),
            Triple(0.94f, 0.34f, 45f),
            Triple(0.10f, 0.12f, 12f),
            Triple(0.20f, 0.62f, -18f),
            Triple(0.84f, 0.52f, 34f),
        )
        for ((fx, fy, deg) in petals) {
            val pw = w * 0.026f
            val ph = w * 0.046f
            rotate(deg, pivot = Offset(w * fx, h * fy)) {
                drawOval(
                    color = Color(0xFFFFC8DE).copy(alpha = 0.50f),
                    topLeft = Offset(w * fx - pw / 2, h * fy - ph / 2),
                    size = Size(pw, ph),
                )
            }
        }
    }
}

/**
 * 整页背景：你选的立绘（或默认渐变）+ 花瓣光斑 + 可调白色蒙层。
 */
@Composable
fun MoePageBackground(content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val bg = rememberMoeBg()
    val scrim = MoePrefs.scrim(ctx)
    Box(modifier = Modifier.fillMaxSize()) {
        if (bg != null) {
            Image(
                bitmap = bg,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFFFF0F7),
                                Color(0xFFF1EBFF),
                                Color(0xFFE6F6FF),
                            )
                        )
                    )
            )
            MoeDecor()
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = scrim))
        )
        content()
    }
}
