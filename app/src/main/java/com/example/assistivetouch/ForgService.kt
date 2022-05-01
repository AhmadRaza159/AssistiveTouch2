package com.example.assistivetouch

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.assistivetouch.databinding.IconTouchBinding
import com.example.assistivetouch.databinding.PopupWindowDashboardBinding
import com.google.gson.Gson


class ForgService : Service() {
    companion object {
        var cntx: Context? = null
        var popUp: PopupWindow? = null
        var isFlashlightOn = false
        var bFlag=false
    }
    private var gson = Gson()
    private var json: String = ""

    var torchCallback: CameraManager.TorchCallback = @RequiresApi(Build.VERSION_CODES.P)
    object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
            isFlashlightOn = enabled
            updateUi()
        }
    }
    private var mNM: NotificationManager? = null
    private var winManager: WindowManager? = null
    private var winParam: WindowManager.LayoutParams? = null
    private var movingCounter = 0

    private var isMoving = false
    lateinit var bindingTouch: IconTouchBinding
    lateinit var bindingPopup: PopupWindowDashboardBinding
    lateinit var utilObj: Utils
    private var displayMetrics: DisplayMetrics? = null
    private var touchIcon: View? = null
    private var popUpDashBoard: View? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
        mNM?.cancel(159)
        winManager?.removeView(touchIcon)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getStringExtra("word")=="open"){
            winParam!!.x = (displayMetrics!!.widthPixels / 2 - touchIcon!!.width/2)
            winParam!!.y = (displayMetrics!!.heightPixels / 2 -touchIcon!!.height/2)
            winManager?.updateViewLayout(touchIcon, winParam)
            popUp?.showAtLocation(touchIcon, Gravity.CENTER, 0, 0)
        }
        checkIcon()
        return super.onStartCommand(intent, flags, startId)

    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        cntx = this


        mNM = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        var notiIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notiIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_MUTABLE
                )
        } else {
            notiIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_ONE_SHOT
                )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                "1",
                "a", NotificationManager.IMPORTANCE_NONE
            )
            mNM?.createNotificationChannel(chan)
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }

        // Set the info for the views that show in the notification panel.

        val notification: Notification = Notification.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle("Assistive Touch") // the label of the entry
            .setContentIntent(notiIntent) // The intent to send when the entry is clicked
            .build()
        startForeground(159, notification)

        ///////////
        displayMetrics = DisplayMetrics()
        touchIcon = LayoutInflater.from(this).inflate(R.layout.icon_touch, null)
        popUpDashBoard = LayoutInflater.from(this).inflate(R.layout.popup_window_dashboard, null)

        winManager = this.getSystemService(WINDOW_SERVICE) as WindowManager
        winParam = WindowManager.LayoutParams()
        winManager?.defaultDisplay?.getMetrics(displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            winParam!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            winParam!!.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        winParam!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        winParam!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        winParam!!.x = displayMetrics!!.widthPixels / 2
        winParam!!.y = displayMetrics!!.widthPixels / 1
        winParam!!.gravity = (Gravity.TOP or Gravity.LEFT)
        winParam!!.format = PixelFormat.RGBA_8888
        winParam!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        winManager?.addView(touchIcon, winParam)
        bindingPopup = PopupWindowDashboardBinding.bind(popUpDashBoard!!)
        bindingTouch = IconTouchBinding.bind(touchIcon!!)


        popUp = (displayMetrics?.widthPixels)?.times(0.75)?.let { it1 ->
            (displayMetrics?.heightPixels)?.times(0.35)?.let { it2 ->
                PopupWindow(
                    popUpDashBoard, it1.toInt(),
                    it2.toInt()
                )
            }
        }
        popUp?.setOnDismissListener {
            Toast.makeText(cntx, "ahahaha", Toast.LENGTH_SHORT).show()
        }
        popUp?.isFocusable = true
        popUp?.isTouchable = true
        touchIcon!!.setOnClickListener {
            val bluetoothMm = cntx?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bFlag = bluetoothMm.adapter.isEnabled
            updateUi()
            utilObj = Utils()
            bindingPopup.atouchFavouriteDashboard.visibility = View.GONE
            bindingPopup.atouchSettingDashboard.visibility = View.GONE
            bindingPopup.atouchMainDashboard.visibility = View.VISIBLE

            winParam!!.x = (displayMetrics!!.widthPixels / 2 - touchIcon!!.width/2)
            winParam!!.y = (displayMetrics!!.heightPixels / 2 -touchIcon!!.height/2)
            winManager?.updateViewLayout(touchIcon, winParam)
            popUp?.showAtLocation(touchIcon, Gravity.CENTER, 0, 0)

        }
        touchIcon?.setOnTouchListener { _, event ->
            movingCounter++

            if (event?.action == MotionEvent.ACTION_DOWN) {
                isMoving = false
                //flagAnimation = false
                Log.e("aServiceKey", "Reached in down")
            }
            if (event?.action == MotionEvent.ACTION_UP) {
                Log.e("aServiceKey", "Reached in up")
                movingCounter = 0
//                if (flagAnimation)
//                    setAssitiveTouchViewAlign()

            }
            if (event?.action == MotionEvent.ACTION_MOVE) {
                if (movingCounter > 10) {
//                    flagAnimation = true
                    Log.e("aServiceKey", "Reached in move")
                    isMoving = true
                    winParam!!.x = (event.getRawX() - touchIcon!!.measuredWidth / 2).toInt()
                    winParam!!.y = (event.getRawY() - touchIcon!!.measuredHeight / 2 - 75).toInt()
                    winManager?.updateViewLayout(touchIcon, winParam)
                }
            }
            return@setOnTouchListener isMoving
        }
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(torchCallback, null)

        clickMethods()
    }
    private fun checkIcon() {
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        when(spat.getString("icon","nil")){
            "1"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_01))
            }
            "2"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_02))
            }
            "3"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_03))
            }
            "4"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_04))
            }
            "5"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_05))
            }
            "6"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_06))
            }
            "7"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_07))
            }
            "8"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_08))
            }
            "9"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_09))
            }
            "10"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_10))
            }
            "11"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_11))
            }
            "12"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_12))
            }
            "13"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_13))
            }
            "14"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_14))
            }
            "15"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_15))
            }
            "16"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_16))
            }
            "17"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_17))
            }
            "18"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_18))
            }
            "19"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_19))
            }
            "20"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_20))
            }
            "21"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_21))
            }
            "22"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_22))
            }
            "nil"->{
                bindingTouch.touchIconTouch.setImageDrawable(this.getDrawable(R.drawable.ic_touch_01))
            }
        }

        if (spat.getInt("op",80)>20){
            bindingTouch.touchIconTouch.alpha=((spat.getInt("op",80)).toFloat()/100)
        }
        else{
            bindingTouch.touchIconTouch.alpha= ((20.0/100).toFloat())
        }

        if (spat.getInt("size", 30)>20){
            val layoutParams = LinearLayout.LayoutParams(
                spat.getInt("size", 30) * 5,
                spat.getInt("size", 30) * 5
            )
            bindingTouch.touchIconTouch.layoutParams = layoutParams
        }
        else{
            val layoutParams = LinearLayout.LayoutParams(
                20 * 5,
                20 * 5
            )
            bindingTouch.touchIconTouch.layoutParams = layoutParams
        }


        /////////

        bindingPopup.aFavFirstText.text = getResolveInfoObjDetails("fav1").first
        bindingPopup.aFavFirstImg.setImageDrawable(getResolveInfoObjDetails("fav1").second)

        bindingPopup.aFavSecondText.text = getResolveInfoObjDetails("fav2").first
        bindingPopup.aFavSecondImg.setImageDrawable(getResolveInfoObjDetails("fav2").second)

        bindingPopup.aFavThirdText.text =getResolveInfoObjDetails("fav3").first
        bindingPopup.aFavThirdImg.setImageDrawable(getResolveInfoObjDetails("fav3").second)

        bindingPopup.aFavFourthText.text = getResolveInfoObjDetails("fav4").first
        bindingPopup.aFavFourthImg.setImageDrawable(getResolveInfoObjDetails("fav4").second)

        bindingPopup.aFavFifthText.text = getResolveInfoObjDetails("fav5").first
        bindingPopup.aFavFifthImg.setImageDrawable(getResolveInfoObjDetails("fav5").second)

        bindingPopup.aFavSixthText.text = getResolveInfoObjDetails("fav6").first
        bindingPopup.aFavSixthImg.setImageDrawable(getResolveInfoObjDetails("fav6").second)

        bindingPopup.aFavSeventhText.text = getResolveInfoObjDetails("fav7").first
        bindingPopup.aFavSeventhImg.setImageDrawable(getResolveInfoObjDetails("fav7").second)

        bindingPopup.aFavEightText.text = getResolveInfoObjDetails("fav8").first
        bindingPopup.aFavEightImg.setImageDrawable(getResolveInfoObjDetails("fav8").second)

    }

    private fun getResolveInfoObjDetails(code: String):Pair<CharSequence,Drawable>{
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        if (spat.getString(code,"nil")!="nil"){
            json=spat.getString(code,"nil")!!
            val obj: ResolveInfo = gson.fromJson(json, ResolveInfo::class.java)
            return Pair(obj.loadLabel(packageManager),obj.loadIcon(packageManager))
        }
        else{
            return Pair("",resources.getDrawable(R.drawable.a_app_box))
        }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun utilsCaller(code: String) {
        when (code) {
            "wifi" -> {
                utilObj.wifi()
            }
            "data" -> {
                utilObj.data()
            }
            "bluetooth" -> {
                utilObj.bluetooth()
            }
            "portrait" -> {
                utilObj.portrait()
            }
            "location" -> {
                utilObj.location()
            }
            "flashlight" -> {
                utilObj.flash()
            }
            "airplane" -> {
                utilObj.airplane()
            }
            "soundup" -> {
                utilObj.soundUp()
            }
            "sounddown" -> {
                utilObj.soundDown()
            }
            "lock" -> {
                utilObj.lock()
            }
            "home" -> {
                utilObj.home()
            }
            "boost" -> {
                utilObj.boost()
            }
            "fav" -> {
                bindingPopup.atouchFavouriteDashboard.visibility = View.VISIBLE
                bindingPopup.atouchSettingDashboard.visibility = View.GONE
                bindingPopup.atouchMainDashboard.visibility = View.GONE
            }
            "setting" -> {
                bindingPopup.atouchFavouriteDashboard.visibility = View.GONE
                bindingPopup.atouchSettingDashboard.visibility = View.VISIBLE
                bindingPopup.atouchMainDashboard.visibility = View.GONE
            }
            "none" -> {

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun clickMethods() {
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )

        bindingPopup.settingBack.setOnClickListener {
            bindingPopup.atouchFavouriteDashboard.visibility = View.GONE
            bindingPopup.atouchSettingDashboard.visibility = View.GONE
            bindingPopup.atouchMainDashboard.visibility = View.VISIBLE
        }
        bindingPopup.aFavBack.setOnClickListener {
            bindingPopup.atouchFavouriteDashboard.visibility = View.GONE
            bindingPopup.atouchSettingDashboard.visibility = View.GONE
            bindingPopup.atouchMainDashboard.visibility = View.VISIBLE
        }

        bindingPopup.main1.setOnClickListener {
            spat.getString("main1", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main2.setOnClickListener {
            spat.getString("main2", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main3.setOnClickListener {
            spat.getString("main3", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main4.setOnClickListener {
            spat.getString("main4", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main5.setOnClickListener {
            spat.getString("main5", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main6.setOnClickListener {
            spat.getString("main6", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main7.setOnClickListener {
            spat.getString("main7", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.main8.setOnClickListener {
            spat.getString("main8", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        //////////
        bindingPopup.setting1.setOnClickListener {
            spat.getString("setting1", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting2.setOnClickListener {
            spat.getString("setting2", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting3.setOnClickListener {
            spat.getString("setting3", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting4.setOnClickListener {
            spat.getString("setting4", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting5.setOnClickListener {
            spat.getString("setting5", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting6.setOnClickListener {
            spat.getString("setting6", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting7.setOnClickListener {
            spat.getString("setting7", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        bindingPopup.setting8.setOnClickListener {
            spat.getString("setting8", "none")?.let { it1 -> utilsCaller(it1) }
            updateUi()
        }
        /////
        bindingPopup.aFavFirst.setOnClickListener {
            if (spat.getString("fav1","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav1","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav1")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavSecond.setOnClickListener {
            if (spat.getString("fav2","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav2","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav2")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavThird.setOnClickListener {
            if (spat.getString("fav3","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav3","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav3")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavFourth.setOnClickListener {
            if (spat.getString("fav4","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav4","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav4")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavFifth.setOnClickListener {
            if (spat.getString("fav5","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav5","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav5")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavSixth.setOnClickListener {
            if (spat.getString("fav6","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav6","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav6")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavSeventh.setOnClickListener {
            if (spat.getString("fav7","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav7","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav7")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        bindingPopup.aFavEight.setOnClickListener {
            if (spat.getString("fav8","nil")!="nil"){
                val obj1: ResolveInfo = gson.fromJson(spat.getString("fav8","nil"), ResolveInfo::class.java)
                val intent = packageManager.getLaunchIntentForPackage(obj1.activityInfo.packageName)
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                val intent = Intent(this, AddNewAppActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("code", "fav8")
                startActivity(intent)
            }
            popUp?.dismiss()

        }

        //////////////

        bindingPopup.aFavFirst.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav1","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavFirstText.text = ""
            bindingPopup.aFavFirstImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavSecond.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav2","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavSecondText.text = ""
            bindingPopup.aFavSecondImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavThird.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav3","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavThirdText.text = ""
            bindingPopup.aFavThirdImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavFourth.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav4","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavFourthText.text = ""
            bindingPopup.aFavFourthImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavFifth.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav5","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavFifthText.text = ""
            bindingPopup.aFavFifthImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavSixth.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav6","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavSixthText.text = ""
            bindingPopup.aFavSixthImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavSeventh.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav7","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavSeventhText.text = ""
            bindingPopup.aFavSeventhImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }
        bindingPopup.aFavEight.setOnLongClickListener {
            val spat = this.getSharedPreferences(
                "spat", Context.MODE_PRIVATE
            )
            val pusher:SharedPreferences.Editor=spat.edit()
            pusher.putString("fav8","nil")
            pusher.commit()
            pusher.apply()

            bindingPopup.aFavEightText.text = ""
            bindingPopup.aFavEightImg.setImageDrawable(this.resources.getDrawable(R.drawable.a_app_box))
            return@setOnLongClickListener true
        }



    }


    @RequiresApi(Build.VERSION_CODES.P)
    public fun updateUi() {
        val spat = this.getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        bindingPopup.main1Img.setImageDrawable(
            spat.getString("main1", "none")?.let { getUtilData(it).first })
        bindingPopup.main1Txt.text = spat.getString("main1", "none")?.let { getUtilData(it).second }
        bindingPopup.main2Img.setImageDrawable(
            spat.getString("main2", "none")?.let { getUtilData(it).first })
        bindingPopup.main2Txt.text = spat.getString("main2", "none")?.let { getUtilData(it).second }
        bindingPopup.main3Img.setImageDrawable(
            spat.getString("main3", "none")?.let { getUtilData(it).first })
        bindingPopup.main3Txt.text = spat.getString("main3", "none")?.let { getUtilData(it).second }
        bindingPopup.main4Img.setImageDrawable(
            spat.getString("main4", "none")?.let { getUtilData(it).first })
        bindingPopup.main4Txt.text = spat.getString("main4", "none")?.let { getUtilData(it).second }
        bindingPopup.main5Img.setImageDrawable(
            spat.getString("main5", "none")?.let { getUtilData(it).first })
        bindingPopup.main5Txt.text = spat.getString("main5", "none")?.let { getUtilData(it).second }
        bindingPopup.main6Img.setImageDrawable(
            spat.getString("main6", "none")?.let { getUtilData(it).first })
        bindingPopup.main6Txt.text = spat.getString("main6", "none")?.let { getUtilData(it).second }
        bindingPopup.main7Img.setImageDrawable(
            spat.getString("main7", "none")?.let { getUtilData(it).first })
        bindingPopup.main7Txt.text = spat.getString("main7", "none")?.let { getUtilData(it).second }
        bindingPopup.main8Img.setImageDrawable(
            spat.getString("main8", "none")?.let { getUtilData(it).first })
        bindingPopup.main8Txt.text = spat.getString("main8", "none")?.let { getUtilData(it).second }
        //
        bindingPopup.setting1Img.setImageDrawable(
            spat.getString("setting1", "none")?.let { getUtilData(it).first })
        bindingPopup.setting1Txt.text =
            spat.getString("setting1", "none")?.let { getUtilData(it).second }
        bindingPopup.setting2Img.setImageDrawable(
            spat.getString("setting2", "none")?.let { getUtilData(it).first })
        bindingPopup.setting2Txt.text =
            spat.getString("setting2", "none")?.let { getUtilData(it).second }
        bindingPopup.setting3Img.setImageDrawable(
            spat.getString("setting3", "none")?.let { getUtilData(it).first })
        bindingPopup.setting3Txt.text =
            spat.getString("setting3", "none")?.let { getUtilData(it).second }
        bindingPopup.setting4Img.setImageDrawable(
            spat.getString("setting4", "none")?.let { getUtilData(it).first })
        bindingPopup.setting4Txt.text =
            spat.getString("setting4", "none")?.let { getUtilData(it).second }
        bindingPopup.setting5Img.setImageDrawable(
            spat.getString("setting5", "none")?.let { getUtilData(it).first })
        bindingPopup.setting5Txt.text =
            spat.getString("setting5", "none")?.let { getUtilData(it).second }
        bindingPopup.setting6Img.setImageDrawable(
            spat.getString("setting6", "none")?.let { getUtilData(it).first })
        bindingPopup.setting6Txt.text =
            spat.getString("setting6", "none")?.let { getUtilData(it).second }
        bindingPopup.setting7Img.setImageDrawable(
            spat.getString("setting7", "none")?.let { getUtilData(it).first })
        bindingPopup.setting7Txt.text =
            spat.getString("setting7", "none")?.let { getUtilData(it).second }
        bindingPopup.setting8Img.setImageDrawable(
            spat.getString("setting8", "none")?.let { getUtilData(it).first })
        bindingPopup.setting8Txt.text =
            spat.getString("setting8", "none")?.let { getUtilData(it).second }


    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getUtilData(code: String): Pair<Drawable, String> {
        when (code) {
            "wifi" -> {
                val wifiManager = cntx?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (wifiManager.isWifiEnabled) {
                    return Pair(resources.getDrawable(R.drawable.wifi), "WiFi")
                } else {
                    return Pair(resources.getDrawable(R.drawable.wifi_off), "WiFi")
                }
            }
            "bluetooth" -> {

                if (bFlag) {
                    return Pair(resources.getDrawable(R.drawable.bluetooth), "Bluetooth")

                } else {
                    return Pair(resources.getDrawable(R.drawable.bluetooth_off), "Bluetooth")

                }
            }
            "data" -> {
                val networkManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                var returnValue=Pair(resources.getDrawable(R.drawable.data_off), "Data")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    if (networkManager.isDataEnabled) {
                        returnValue= Pair(resources.getDrawable(R.drawable.data), "Data")
                    } else {
                        returnValue= Pair(resources.getDrawable(R.drawable.data_off), "Data")
                    }
                return returnValue
            }
            "portrait" -> {
                if (Settings.System.getInt(
                        contentResolver,
                        Settings.System.ACCELEROMETER_ROTATION,
                        0
                    ) == 1
                ) {
                    return Pair(resources.getDrawable(R.drawable.portrait), "Portrait")

                } else {
                    return Pair(resources.getDrawable(R.drawable.portrait_off), "Portrait")

                }
            }
            "location" -> {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isLocationEnabled) {
                    return Pair(resources.getDrawable(R.drawable.location), "Location")
                } else {
                    return Pair(resources.getDrawable(R.drawable.location_off), "Location")
                }
            }
            "soundup" -> {
                return Pair(resources.getDrawable(R.drawable.sound_up), "Sound Up")
            }
            "airplane" -> {
                if (Settings.Global.getInt(
                        contentResolver,
                        Settings.Global.AIRPLANE_MODE_ON, 0
                    ) == 1
                ) {
                    return Pair(resources.getDrawable(R.drawable.airplane), "Airplane")
                } else {
                    return Pair(resources.getDrawable(R.drawable.airplane_off), "Airplane")
                }
            }
            "flashlight" -> {
                    if (!isFlashlightOn) {
                        return Pair(resources.getDrawable(R.drawable.flash_off), "Flashlight")
                    } else {
                        return Pair(resources.getDrawable(R.drawable.flash), "Flashlight")
                    }




            }
            "sounddown" -> {
                return Pair(resources.getDrawable(R.drawable.sound_down), "Sound Down")
            }
            "boost" -> {
                return Pair(resources.getDrawable(R.drawable.boost), "Boost")
            }
            "home" -> {
                return Pair(resources.getDrawable(R.drawable.home), "Home")
            }
            "lock" -> {
                return Pair(resources.getDrawable(R.drawable.lock), "Lock")
            }
            "fav" -> {
                return Pair(resources.getDrawable(R.drawable.heart), "Favorite")
            }
            "setting" -> {
                return Pair(resources.getDrawable(R.drawable.setting), "Settings")
            }
            "none" -> {
                return Pair(resources.getDrawable(R.drawable.none), "")
            }
            else -> {
                return Pair(resources.getDrawable(R.drawable.none), "")
            }
        }
    }



    class Utils() {
        private var isFlashlightOn = false

        private val torchCallback: CameraManager.TorchCallback = @RequiresApi(Build.VERSION_CODES.M)
        object : CameraManager.TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                super.onTorchModeChanged(cameraId, enabled)
                isFlashlightOn = enabled
            }
        }

        fun home() {
            popUp?.dismiss()
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            cntx?.startActivity(intent)
        }

        fun lock() {
            popUp?.dismiss()
            var dpm: DevicePolicyManager= cntx?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            var mDeviceAdminSample= ComponentName(cntx!!, AdminReciver::class.java)

            if (dpm.isAdminActive(mDeviceAdminSample)){
                var deviceManger =
                    cntx?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                deviceManger.lockNow()
            }
            else{
             //   Runtime.getRuntime().exec("dpm set-device-admin --user 0 com.mydeviceadmin/.AdminReciver")
                val intent = Intent(cntx,MainActivity::class.java)
                intent.putExtra("code",true)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                cntx?.startActivity(intent)
            }

        }

        fun wifi() {
            popUp?.dismiss()

            val astvTchIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
            astvTchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            cntx?.startActivity(astvTchIntent)
        }

        fun data() {
            popUp?.dismiss()
            try {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$DataUsageSummaryActivity"
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                cntx?.startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                cntx?.startActivity(intent)
            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun portrait() {
            if (Settings.System.canWrite(cntx)) {
                if (Settings.System.getInt(
                        cntx?.contentResolver,
                        Settings.System.ACCELEROMETER_ROTATION,
                        0
                    ) == 1
                ) {
                    Settings.System.putInt(
                        cntx?.contentResolver,
                        Settings.System.ACCELEROMETER_ROTATION,
                        0
                    )
                } else {
                    Settings.System.putInt(
                        cntx?.contentResolver,
                        Settings.System.ACCELEROMETER_ROTATION,
                        1
                    )
                }
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                cntx?.startActivity(intent)
            }
        }

        fun location() {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            cntx?.startActivity(intent)
            popUp?.dismiss()
        }

        fun airplane() {
            val panelIntent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            panelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            cntx?.startActivity(panelIntent)
            popUp?.dismiss()
        }

        fun bluetooth() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (cntx?.let {
                        ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.BLUETOOTH_CONNECT
                        )
                    } == PackageManager.PERMISSION_GRANTED){
                    val bluetoothM = cntx?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                    if (bluetoothM.adapter.isEnabled) {
                        bFlag=false
                        bluetoothM.adapter.disable()
                    } else {
                        bFlag=true
                        bluetoothM.adapter.enable()
                    }
                }
                else{
                    Toast.makeText(cntx,"Bluetooth nearby permission required",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val bluetoothM = cntx?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                if (bluetoothM.adapter.isEnabled) {
                    bFlag=false
                    bluetoothM.adapter.disable()

                } else {
                    bFlag=true
                    bluetoothM.adapter.enable()
                }

            }

        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun flash() {
            val cameraManager = cntx?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager.registerTorchCallback(torchCallback, null)
            if (cntx?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == true) {
                if (!isFlashlightOn) {
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                } else {
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
                }
            }
        }

        fun boost() {
        }

        fun soundUp() {
            val audioManager = cntx?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1, 1
            )

        }

        fun soundDown() {
            val audioManager = cntx?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1, 1
            )

        }


    }
}