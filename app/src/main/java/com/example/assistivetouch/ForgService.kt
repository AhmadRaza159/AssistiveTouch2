package com.example.assistivetouch

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import android.bluetooth.BluetoothManager
import android.content.Context
import android.graphics.PixelFormat
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.assistivetouch.databinding.IconTouchBinding
import com.example.assistivetouch.databinding.PopupWindowDashboardBinding


class ForgService: Service() {
    companion object{
        var cntx:Context?=null
    }
    private var mNM: NotificationManager? = null
    private var winManager: WindowManager? = null
    private var winParam: WindowManager.LayoutParams? = null
    private var movingCounter = 0
    private var isMoving = false
    lateinit var bindingTouch:IconTouchBinding
    lateinit var bindingPopup:PopupWindowDashboardBinding
    lateinit var utilObj:Utils
    private var displayMetrics: DisplayMetrics? = null
    private var touchIcon: View? = null
    private var popUpDashBoard: View? = null
    private var popUp: PopupWindow? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        cntx=this


        mNM = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        var notiIntent :PendingIntent?=null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notiIntent =
                PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_MUTABLE)
        } else {
            notiIntent =
                PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT)
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
            Toast.makeText(cntx,"ahahaha",Toast.LENGTH_SHORT).show()
        }
        popUp?.isFocusable = true
        popUp?.isTouchable = true
        touchIcon!!.setOnClickListener {
            Toast.makeText(cntx,"ahahaha",Toast.LENGTH_SHORT).show()
            utilObj= Utils()
            bindingPopup.atouchFavouriteDashboard.visibility=View.GONE
            bindingPopup.atouchSettingDashboard.visibility=View.GONE
            bindingPopup.atouchMainDashboard.visibility=View.VISIBLE

            winParam!!.x = (displayMetrics!!.widthPixels / 2 )
            winParam!!.y = (displayMetrics!!.heightPixels / 2 )
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
        clickMethods()
    }
    private fun utilsCaller(code:String){
        when(code){
            "wifi"->{
                utilObj.wifi()
            }
            "data"->{
                utilObj.data()
            }
            "bluetooth"->{
                utilObj.bluetooth()
            }
            "portrait"->{
                utilObj.portrait()
            }
            "location"->{
                utilObj.location()
            }
            "flash"->{
                utilObj.flash()
            }
            "airplane"->{
                utilObj.airplane()
            }
            "soundup"->{
                utilObj.soundUp()
            }
            "sounddown"->{
                utilObj.soundDown()
            }
            "lock"->{
                utilObj.lock()
            }
            "home"->{
                utilObj.home()
            }
            "boost"->{
                utilObj.boost()
            }
            "favorite"->{
                bindingPopup.atouchFavouriteDashboard.visibility=View.VISIBLE
                bindingPopup.atouchSettingDashboard.visibility=View.GONE
                bindingPopup.atouchMainDashboard.visibility=View.GONE
            }
            "utils"->{
                bindingPopup.atouchFavouriteDashboard.visibility=View.GONE
                bindingPopup.atouchSettingDashboard.visibility=View.VISIBLE
                bindingPopup.atouchMainDashboard.visibility=View.GONE
            }

        }
    }

    private fun clickMethods() {
        bindingPopup.main1.setOnClickListener {

        }
        bindingPopup.main2.setOnClickListener {

        }
        bindingPopup.main3.setOnClickListener {

        }
        bindingPopup.main4.setOnClickListener {

        }
        bindingPopup.main5.setOnClickListener {

        }
        bindingPopup.main6.setOnClickListener {

        }
        bindingPopup.main7.setOnClickListener {

        }
        bindingPopup.main8.setOnClickListener {

        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("mydata", "Reach OnStartCmnd")
        return super.onStartCommand(intent, flags, startId)

    }
    class Utils(){
        fun home(){

        }
        fun lock(){
            Log.d("mydata", "Reach wifi")
        }
        fun wifi(){
            val astvTchIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
            astvTchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            cntx?.startActivity(astvTchIntent)        }
        fun data(){
            Log.d("mydata", "Reach wifi")
        }
        fun portrait(){
            Log.d("mydata", "Reach wifi")
        }
        fun location(){
            Log.d("mydata", "Reach wifi")
        }
        fun airplane(){
            Log.d("mydata", "Reach wifi")
        }
        fun bluetooth(){
            val bluetoothM= cntx?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (bluetoothM.adapter.isEnabled) {
                bluetoothM.adapter.disable()

            } else {
                bluetoothM.adapter.enable()
            }
        }
        fun flash(){
            Log.d("mydata", "Reach wifi")
        }
        fun boost(){
            Log.d("mydata", "Reach wifi")
        }
        fun soundUp(){
            Log.d("mydata", "Reach wifi")
        }
        fun soundDown(){
            Log.d("mydata", "Reach wifi")
        }


    }
}