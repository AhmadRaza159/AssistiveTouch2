package com.example.assistivetouch.fragments

import android.app.Activity.RESULT_OK
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.assistivetouch.R
import com.example.assistivetouch.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater)
        checkStatus()
        binding.button.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivityForResult(intent, 102)
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public fun checkStatus() {

        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager

        if (notificationManager.isNotificationPolicyAccessGranted) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.permission_activity_id, DrawerPermissionFragment.newInstance())
                .commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 102) {
            checkStatus()
        }
    }

    companion object {

        fun newInstance() =
            NotificationFragment()
    }
}