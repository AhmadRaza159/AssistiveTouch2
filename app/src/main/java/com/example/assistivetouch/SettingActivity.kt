package com.example.assistivetouch

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.example.assistivetouch.ads.AddsClass
import com.example.assistivetouch.databinding.ActivitySettingBinding
import com.google.android.ads.nativetemplates.TemplateView

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateView()
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = spat.edit()
        loadAd()
        binding.radS.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1){
                    editor.putInt("speed",1)
                    editor.apply()
                    editor.commit()
                }
            }

        })
        binding.radN.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1){
                    editor.putInt("speed",2)
                    editor.apply()
                    editor.commit()
                }
            }

        })
        binding.radQ.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1){
                    editor.putInt("speed",3)
                    editor.apply()
                    editor.commit()
                }
            }

        })


        binding.iconSizeSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                editor.putInt("size", p1)
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    editor.putInt("size", p0.progress)
                }
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    editor.putInt("size", p0.progress)
                }
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

        })
        binding.iconOpSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                editor.putInt("op", p1)
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStartTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    editor.putInt("op", p0.progress)
                }
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    editor.putInt("op", p0.progress)
                }
                editor.apply()
                editor.commit()
                if (isServiceRunning(ForgService::class.java)) {
                    startForegroundService(Intent(this@SettingActivity, ForgService::class.java))
                }
                updateView()
            }

        })
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
    private fun updateView() {
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        binding.iconSizeSeekbar.progress=spat.getInt("size",0)
        binding.iconSizeValue.text=(spat.getInt("size",0)).toString()
        binding.iconOpValue.text=(spat.getInt("op",0)).toString()
        binding.iconOpSeekbar.progress=spat.getInt("op",0)
        if (spat.getInt("speed",2)==1){
            binding.radS.isChecked=true
        }
        else if (spat.getInt("speed",2)==2){
            binding.radN.isChecked=true
        }
        else if (spat.getInt("speed",2)==3){
            binding.radQ.isChecked=true
        }
    }
    private fun loadAd(){
        var tmplate: TemplateView =findViewById(R.id.native_ad)
        var adClas: AddsClass = AddsClass(this)
        adClas.load_Native_Ad(tmplate)
    }
}