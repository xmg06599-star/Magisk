package com.topjohnwu.magisk.ui.moe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 首页顶部的萌化横幅：只有设了自定义文案才显示。 */
@Composable
fun MoeBanner(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val title = MoePrefs.get(ctx, MoePrefs.KEY_BANNER_TITLE)
    val subtitle = MoePrefs.get(ctx, MoePrefs.KEY_BANNER_SUBTITLE)
    if (title.isEmpty() && subtitle.isEmpty()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFFD8E6), Color(0xFFEBDCFF), Color(0xFFD4F0FF))
                )
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Column {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A3E6B)
                )
            }
            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF7C6E9E)
                )
            }
        }
    }
}

/** 自定义文案覆盖：有自定义值就用自定义，否则回落到资源字符串。 */
@Composable
fun moeText(key: String, fallback: String): String {
    val ctx = LocalContext.current
    val custom = MoePrefs.get(ctx, key)
    return custom.ifEmpty { fallback }
}
