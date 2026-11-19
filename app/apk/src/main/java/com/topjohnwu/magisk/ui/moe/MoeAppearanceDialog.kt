package com.topjohnwu.magisk.ui.moe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/** 「打扮本小姐」：所有萌化文案都能在这里改，留空则用默认。 */
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
                    "全部留空 = 用默认文案",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                onDismiss()
            }) { Text("好了") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("算了") } },
    )
}
