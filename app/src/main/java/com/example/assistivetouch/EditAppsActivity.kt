package com.example.assistivetouch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.assistivetouch.ads.AddsClass
import com.example.assistivetouch.databinding.ActivityEditAppsBinding
import com.example.assistivetouch.fragments.EditMainFragment
import com.example.assistivetouch.fragments.EditSettingFragment
import com.google.android.ads.nativetemplates.TemplateView

class EditAppsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditAppsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAd()
        val authenticationAdapter: AuthenticationAdapter =
            AuthenticationAdapter(
                supportFragmentManager
            )
        binding.vpager.setAdapter(authenticationAdapter)

    }

    class AuthenticationAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    EditMainFragment()
                }
                1 -> {
                    EditSettingFragment()
                }
                else -> {
                    EditMainFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
    private fun loadAd(){
        var tmplate: TemplateView =findViewById(R.id.native_ad)
        var adClas: AddsClass = AddsClass(this)
        adClas.load_Native_Ad(tmplate)
    }
}