package com.zhongpin.mvvm_android.service

import android.content.Intent
import android.util.Log
import com.blankj.utilcode.util.ServiceUtils
import com.blankj.utilcode.util.Utils

object UserNotificationUtil {
    fun  startService() {
        try {
            val intent = Intent(Utils.getApp(), NotificationService::class.java)
            intent.putExtra(NotificationService.SERVICE_ACTION, NotificationService.SERVICE_ACTION_START)
            ServiceUtils.startService(intent)
        } catch (e:Exception) {
            Log.d("NotificationService", "NotificationService startService", e)
        }
    }

    fun stopService() {
        try {
            val intent = Intent(Utils.getApp(), NotificationService::class.java)
            intent.putExtra(
                NotificationService.SERVICE_ACTION,
                NotificationService.SERVICE_ACTION_STOP
            )
            ServiceUtils.stopService(intent)
        }catch (e:Exception) {
                Log.d("NotificationService", "NotificationService stopService", e)
        }
    }
}