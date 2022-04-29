package com.example.assistivetouch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.assistivetouch.fragments.NotificationFragment

class PermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        supportFragmentManager.beginTransaction().replace(R.id.permission_activity_id,NotificationFragment.newInstance()).commit()
    }

}