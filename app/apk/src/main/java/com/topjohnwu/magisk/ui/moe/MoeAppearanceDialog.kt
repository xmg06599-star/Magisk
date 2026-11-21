package com.topjohnwu.magisk.ui.moe

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/** 「打扮本小姐」：文案 / 背景图 / 蒙层 / 勾开关，留空则用默认。 */
@Composable
fun MoeAppearanceDialog(show: Boolean, onDismiss: () -> Unit) {
    if (!show) return
    val ctx = LocalContext.current

    var bannerTitle by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_BANNER_TITLE)) }
    var bannerSubtitle by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_BANNER_SUBTITLE)) }
    var textMagisk by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_MAGISK)) }
    var textApp by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_APP)) }
    var textZygisk by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_ZYGISK)) }
    var textRamdisk by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_RAMDISK)) }
    var textSupport by remember { mutableStateOf(MoePrefs.get(ctx, MoePrefs.KEY_SUPPORT)) }
    var scrim by remember { mutableStateOf((MoePrefs.scrim(ctx) * 100).toInt()) }
    var showCheck by remember { mutableStateOf(MoePrefs.showCheck(ctx)) }
    var hasBg by remember { mutableStateOf(MoePrefs.hasBg(ctx)) }
    // 换图后让首页重组
    var bgTick by remember { mutableIntStateOf(0) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null && MoePrefs.importBg(ctx, uri)) {
            hasBg = true
            bgTick++
        }
    }

    @Composable
    fun field(label: String, value: String, hint: String, onChange: (String) -> Unit) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            placeholder = { Text(hint) },
            modifier = Modifier.fillMaxWidth()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("打扮本小姐") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "文字留空 = 用默认；下面还能换背景、调蒙层",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    "背景立绘",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { picker.launch("image/*") }) {
                        Text(if (hasBg) "换一张" else "选图片")
                    }
                    if (hasBg) {
                        TextButton(onClick = {
                            MoePrefs.clearBg(ctx)
                            hasBg = false
                            bgTick++
                        }) { Text("清掉") }
                    }
                    Text(
                        text = if (hasBg) "已用你的图" else "默认渐变",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    "背景蒙层  $scrim%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Slider(
                    value = scrim.toFloat(),
                    onValueChange = { scrim = it.toInt() },
                    valueRange = 0f..90f,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Magisk 卡右上角显示状态勾",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Switch(checked = showCheck, onCheckedChange = { showCheck = it })
                }

                Text(
                    "文案（留空用默认）",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
                field("首页横幅 · 标题", bannerTitle, "哼，Magisk 才不是给你玩的") { bannerTitle = it }
                field("首页横幅 · 副标题", bannerSubtitle, "本小姐今天也在好好看着你的机器") { bannerSubtitle = it }
                field("Magisk 卡片名", textMagisk, "Magisk") { textMagisk = it }
                field("管理器卡片名", textApp, "Magisk App") { textApp = it }
                field("Zygisk 项名称", textZygisk, "Zygisk") { textZygisk = it }
                field("Ramdisk 项名称", textRamdisk, "Ramdisk") { textRamdisk = it }
                field("支持开发标题", textSupport, "支持开发") { textSupport = it }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                MoePrefs.set(ctx, MoePrefs.KEY_BANNER_TITLE, bannerTitle)
                MoePrefs.set(ctx, MoePrefs.KEY_BANNER_SUBTITLE, bannerSubtitle)
                MoePrefs.set(ctx, MoePrefs.KEY_MAGISK, textMagisk)
                MoePrefs.set(ctx, MoePrefs.KEY_APP, textApp)
                MoePrefs.set(ctx, MoePrefs.KEY_ZYGISK, textZygisk)
                MoePrefs.set(ctx, MoePrefs.KEY_RAMDISK, textRamdisk)
                MoePrefs.set(ctx, MoePrefs.KEY_SUPPORT, textSupport)
                MoePrefs.setScrim(ctx, scrim)
                MoePrefs.setShowCheck(ctx, showCheck)
                onDismiss()
            }) { Text("好了") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("算了") } },
    )
}
