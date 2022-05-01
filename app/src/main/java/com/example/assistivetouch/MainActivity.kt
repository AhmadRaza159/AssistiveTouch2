package com.example.assistivetouch

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.assistivetouch.databinding.ActivityMainBinding
import com.example.assistivetouch.databinding.ViewWithDrawerBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding2:ViewWithDrawerBinding
    private var code=false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2= ViewWithDrawerBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        startData()
        code=intent.getBooleanExtra("code",false)
        if (code){
            var mDeviceAdminSample= ComponentName(applicationContext, AdminReciver::class.java)

            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(
                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminSample
            )
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Assistive Touch require admin permission to lock your phone"
            )
            startActivity(intent)
        }
        animateNavigationDrawer()
        menuPoping()
        binding2.mainView.switch1.setOnCheckedChangeListener { btn, b ->
            if (b){
                binding2.mainView.txtAct.visibility= View.VISIBLE
                binding2.mainView.txtDct.visibility= View.GONE
                if (!isServiceRunning(ForgService::class.java)){
                    val i = Intent(this, ForgService::class.java)
//                    .putExtra("EXTRA_RESULT_INTENT", attr.data)
                    startForegroundService(i)
                }

            }
            else{
                binding2.mainView.txtAct.visibility= View.GONE
                binding2.mainView.txtDct.visibility= View.VISIBLE
                stopService(Intent(this, ForgService::class.java))
            }
        }

        binding2.mainView.editAppsButton.setOnClickListener {
            startActivity(Intent(this,EditAppsActivity::class.java))
        }
        binding2.mainView.editIconsButton.setOnClickListener {
            startActivity(Intent(this,EditIconActivity::class.java))
        }
        binding2.mainView.settingsButton.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
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
        if (isServiceRunning(ForgService::class.java)){
            binding2.mainView.switch1.isChecked =true
            binding2.mainView.txtAct.visibility= View.VISIBLE
            binding2.mainView.txtDct.visibility= View.GONE

        }
        else{
            binding2.mainView.switch1.isChecked =false
            binding2.mainView.txtAct.visibility= View.GONE
            binding2.mainView.txtDct.visibility= View.VISIBLE
        }


        val dataEntry: SharedPreferences.Editor=spat.edit()
        if (spat.all.isEmpty()){
            dataEntry.putInt("size",30)
            dataEntry.putInt("op",80)

            ///
            dataEntry.putString("fav1","nil")
            dataEntry.putString("fav2","nil")
            dataEntry.putString("fav3","nil")
            dataEntry.putString("fav4","nil")
            dataEntry.putString("fav5","nil")
            dataEntry.putString("fav6","nil")
            dataEntry.putString("fav7","nil")
            dataEntry.putString("fav8","nil")

            ///
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

            ///

            dataEntry.apply()
            dataEntry.commit()
        }
    }
    private fun animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        binding2.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                // Scale the View based on current slide offset
                val diffScaledOffset = slideOffset * (1 - 0.7f)
                val offsetScale = 1 - diffScaledOffset
                binding2.mainView.mainCont!!.scaleX = offsetScale
                binding2.mainView.mainCont!!.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff = binding2.mainView.mainCont!!.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                binding2.mainView.mainCont!!.translationX = xTranslation
            }
        })
    }
    private fun menuPoping() {
        binding2.mainView.mainScreenMenu.setOnClickListener {
//            Toast.makeText(this,"TTT",Toast.LENGTH_SHORT).show()
            if (binding2.drawerLayout.isDrawerVisible(
                    GravityCompat.START
                )
            ) binding2.drawerLayout.closeDrawer(GravityCompat.START) else binding2.drawerLayout.openDrawer(
                GravityCompat.START
            )
            /////////////////////////////////
            binding2.navView.bringToFront()

            binding2.navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        binding2.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.more_apps -> {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/developer?id=Galaxy+studio+apps")
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/developer?id=Galaxy+studio+apps")
                                )
                            )
                        }
                        binding2.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.share -> {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey check out this app at: https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                        )
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                        binding2.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.rate -> {
                        val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                                )
                            )
                        }
                        binding2.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.policy -> {
                        val intent = Intent(this, PrivacyPolicy::class.java)
                        startActivity(intent)
                        binding2.drawerLayout.closeDrawers()
                        true
                    }

                    else -> {
                        false
                    }
                }

            }
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