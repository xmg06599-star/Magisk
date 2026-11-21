package com.topjohnwu.magisk.ui.moe

import android.content.Context
import android.net.Uri
import java.io.File

/**
 * 萌化配置：文案、背景图、蒙层强度、状态勾开关。
 * 全部存 App 私有目录，不需要额外权限。
 */
object MoePrefs {
    private const val FILE = "moe_theme"
    private const val DIR = "moe"

    const val KEY_BANNER_TITLE = "banner_title"
    const val KEY_BANNER_SUBTITLE = "banner_subtitle"
    const val KEY_MAGISK = "text_magisk"
    const val KEY_APP = "text_app"
    const val KEY_ZYGISK = "text_zygisk"
    const val KEY_RAMDISK = "text_ramdisk"
    const val KEY_SUPPORT = "text_support"
    const val KEY_SCRIM = "scrim_alpha"
    const val KEY_SHOW_CHECK = "show_check"

    private fun prefs(ctx: Context) =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun get(ctx: Context, key: String): String =
        prefs(ctx).getString(key, "") ?: ""

    fun set(ctx: Context, key: String, value: String) {
        prefs(ctx).edit().putString(key, value.trim()).apply()
    }

    /** 背景蒙层浓度 0~90，默认 35 */
    fun scrim(ctx: Context): Float = prefs(ctx).getInt(KEY_SCRIM, 35) / 100f

    fun setScrim(ctx: Context, percent: Int) {
        prefs(ctx).edit().putInt(KEY_SCRIM, percent.coerceIn(0, 90)).apply()
    }

    fun showCheck(ctx: Context): Boolean = prefs(ctx).getBoolean(KEY_SHOW_CHECK, true)

    fun setShowCheck(ctx: Context, v: Boolean) {
        prefs(ctx).edit().putBoolean(KEY_SHOW_CHECK, v).apply()
    }

    fun bgFile(ctx: Context): File = File(ctx.filesDir, DIR).apply { mkdirs() }.resolve("bg")

    fun hasBg(ctx: Context): Boolean = bgFile(ctx).exists()

    fun clearBg(ctx: Context) { bgFile(ctx).delete() }

    /** 把相册里选的图拷进私有目录 */
    fun importBg(ctx: Context, uri: Uri): Boolean = runCatching {
        val dst = bgFile(ctx)
        ctx.contentResolver.openInputStream(uri)!!.use { input ->
            dst.outputStream().use { out -> input.copyTo(out) }
        }
        dst.exists() && dst.length() > 0
    }.getOrDefault(false)
}
