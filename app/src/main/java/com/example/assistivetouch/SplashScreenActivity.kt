package com.example.assistivetouch

import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class SplashScreenActivity : AppCompatActivity() {

    private var drawerFlag=false
    private var notificationFlag=false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_splash)

        supportActionBar?.hide()



        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        if (notificationManager.isNotificationPolicyAccessGranted) {
            notificationFlag=true
        }
        if(Settings.canDrawOverlays(this)){
            drawerFlag=true
        }
        val handler = Handler()
        handler.postDelayed({
            if (drawerFlag&&notificationFlag){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,PermissionActivity::class.java))
            }
            finish()

        }, 1000)

    }
}