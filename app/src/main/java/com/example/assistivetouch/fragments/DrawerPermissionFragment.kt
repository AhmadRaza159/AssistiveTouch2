package com.example.assistivetouch.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.assistivetouch.MainActivity
import com.example.assistivetouch.R
import com.example.assistivetouch.databinding.FragmentDrawerPermissionBinding


class DrawerPermissionFragment : Fragment() {
    private lateinit var binding: FragmentDrawerPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDrawerPermissionBinding.inflate(inflater)


        binding.button.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().packageName)
            )
            startActivityForResult(intent, 103)
        }

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkStatus(){
        if (Settings.canDrawOverlays(requireContext())){
            startActivity(Intent(requireActivity(),MainActivity::class.java))
            requireActivity().finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==103){
            checkStatus()
        }
    }

    companion object {
        fun newInstance() =
            DrawerPermissionFragment()
    }
}