package com.example.assistivetouch

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.assistivetouch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startData()


        binding.switch1.setOnCheckedChangeListener { btn, b ->
            if (b){
                if (!isServiceRunning(ForgService::class.java)){
                    val i = Intent(this, ForgService::class.java)
//                    .putExtra("EXTRA_RESULT_INTENT", attr.data)
                    startForegroundService(i)
                }

            }
            else{
                stopService(Intent(this, ForgService::class.java))
            }
        }

        binding.editAppsButton.setOnClickListener {
            startActivity(Intent(this,EditAppsActivity::class.java))
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun startData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            var bluetoothPermission = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT
            )
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, bluetoothPermission,11)
            }
        }
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        val dataEntry: SharedPreferences.Editor=spat.edit()
        if (spat.all.isEmpty()){
            dataEntry.putString("main1","none")
            dataEntry.putString("main2","lock")
            dataEntry.putString("main3","none")
            dataEntry.putString("main4","fav")
            dataEntry.putString("main5","setting")
            dataEntry.putString("main6","none")
            dataEntry.putString("main7","home")
            dataEntry.putString("main8","data")
            ///
            dataEntry.putString("setting1","wifi")
            dataEntry.putString("setting2","bluetooth")
            dataEntry.putString("setting3","portrait")
            dataEntry.putString("setting4","location")
            dataEntry.putString("setting5","soundup")
            dataEntry.putString("setting6","airplane")
            dataEntry.putString("setting7","flashlight")
            dataEntry.putString("setting8","sounddown")
            ///
            dataEntry.putString("fav1","none")
            dataEntry.putString("fav2","none")
            dataEntry.putString("fav3","none")
            dataEntry.putString("fav4","none")
            dataEntry.putString("fav5","none")
            dataEntry.putString("fav6","none")
            dataEntry.putString("fav7","none")
            dataEntry.putString("fav8","none")
            ///

            dataEntry.apply()
            dataEntry.commit()
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}