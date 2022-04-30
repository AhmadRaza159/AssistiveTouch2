package com.example.assistivetouch

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.assistivetouch.databinding.ActivityEditIconBinding

class EditIconActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditIconBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditIconBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickCaller()

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickCaller() {
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        val dataEntry: SharedPreferences.Editor=spat.edit()
        binding.icTouch01.setOnClickListener {
            dataEntry.putString("icon","1")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch02.setOnClickListener {
            dataEntry.putString("icon","2")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch03.setOnClickListener {
            dataEntry.putString("icon","3")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch04.setOnClickListener {
            dataEntry.putString("icon","4")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch05.setOnClickListener {
            dataEntry.putString("icon","5")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch06.setOnClickListener {
            dataEntry.putString("icon","6")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch07.setOnClickListener {
            dataEntry.putString("icon","7")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch08.setOnClickListener {
            dataEntry.putString("icon","8")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch09.setOnClickListener {
            dataEntry.putString("icon","9")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch10.setOnClickListener {
            dataEntry.putString("icon","10")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch11.setOnClickListener {
            dataEntry.putString("icon","11")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch12.setOnClickListener {
            dataEntry.putString("icon","12")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch13.setOnClickListener {
            dataEntry.putString("icon","13")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch14.setOnClickListener {
            dataEntry.putString("icon","14")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch15.setOnClickListener {
            dataEntry.putString("icon","15")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch16.setOnClickListener {
            dataEntry.putString("icon","16")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch17.setOnClickListener {
            dataEntry.putString("icon","17")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch18.setOnClickListener {
            dataEntry.putString("icon","18")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch19.setOnClickListener {
            dataEntry.putString("icon","19")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch20.setOnClickListener {
            dataEntry.putString("icon","20")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch21.setOnClickListener {
            dataEntry.putString("icon","21")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }
        binding.icTouch22.setOnClickListener {
            dataEntry.putString("icon","22")
            dataEntry.apply()
            dataEntry.commit()
            if (isServiceRunning(ForgService::class.java)) {
                startForegroundService(Intent(this, ForgService::class.java))
            }
        }

    }
}