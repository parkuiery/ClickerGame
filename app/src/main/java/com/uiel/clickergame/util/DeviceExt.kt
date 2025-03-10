package com.uiel.clickergame.util

import android.provider.Settings
import com.uiel.clickergame.CkApplication


fun getDeviceUuid(): String? {
    return Settings.Secure.getString(
        CkApplication.applicationContext().contentResolver,
        Settings.Secure.ANDROID_ID
    )
}