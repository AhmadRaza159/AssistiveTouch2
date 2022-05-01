package com.example.assistivetouch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assistivetouch.databinding.ActivityAddNewAppBinding
import com.google.gson.Gson

class AddNewAppActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddNewAppBinding
    private lateinit var adapter: AdapterClass
    private var code:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddNewAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        code=intent.getStringExtra("code") as String

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packages: List<ResolveInfo> = this.packageManager.queryIntentActivities(mainIntent,0)
        binding.recyc.layoutManager= GridLayoutManager(this,4)
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        val dataEntry: SharedPreferences.Editor=spat.edit()

        adapter= AdapterClass(packages,this,object :InterfaceClass{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(model: ResolveInfo) {
                val gson = Gson()
                val json = gson.toJson(model)
                dataEntry.putString(code,json)
                dataEntry.commit()
                dataEntry.apply()

                var intent=Intent(this@AddNewAppActivity, ForgService::class.java)
                intent.putExtra("word","open")
                startForegroundService(intent)
                finish()
            }

        })

        binding.recyc.adapter=adapter

    }
}