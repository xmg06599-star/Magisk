package com.topjohnwu.magisk.ui.moe

import android.content.Context

/**
 * 萌化文案的本地存储。留空表示用内置默认文案。
 */
object MoePrefs {
    private const val FILE = "moe_theme"

    const val KEY_BANNER_TITLE = "banner_title"
    const val KEY_BANNER_SUBTITLE = "banner_subtitle"
    const val KEY_MAGISK = "text_magisk"
    const val KEY_APP = "text_app"
    const val KEY_ZYGISK = "text_zygisk"
    const val KEY_RAMDISK = "text_ramdisk"
    const val KEY_SUPPORT = "text_support"

    private fun prefs(ctx: Context) =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun get(ctx: Context, key: String): String =
        prefs(ctx).getString(key, "") ?: ""

    fun set(ctx: Context, key: String, value: String) {
        prefs(ctx).edit().putString(key, value.trim()).apply()
    }
}
