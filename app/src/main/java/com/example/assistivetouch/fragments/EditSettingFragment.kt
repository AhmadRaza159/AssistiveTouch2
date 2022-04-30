package com.example.assistivetouch.fragments

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assistivetouch.R
import com.example.assistivetouch.databinding.ControlsCstmzPopBinding
import com.example.assistivetouch.databinding.FragmentEditMainBinding
import com.example.assistivetouch.databinding.FragmentEditSettingBinding


class EditSettingFragment : Fragment() {

    private lateinit var binding: FragmentEditSettingBinding
    private lateinit var dBinding: ControlsCstmzPopBinding
    private var chooseActionDialog: Dialog?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentEditSettingBinding.inflate(inflater)
        dBinding= ControlsCstmzPopBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.controls_cstmz_pop,null))
        chooseActionDialog=Dialog(requireContext())
        chooseActionDialog?.setContentView(dBinding.root)
        chooseActionDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initializeView()
        buttonClicker()



        return binding.root
    }

    private fun dialogClicker(code: String){
        val spat = requireActivity().getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        val dataEntry: SharedPreferences.Editor=spat.edit()
        dBinding.btnNone.setOnClickListener {
            dataEntry.putString(code,"none")
            dataEntry.apply()
            dataEntry.commit()
            initializeView()
            chooseActionDialog?.dismiss()
        }
        dBinding.btnHome.setOnClickListener {
            dataEntry.putString(code,"home")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnSetting.setOnClickListener {
            dataEntry.putString(code,"setting")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnLock.setOnClickListener {
            dataEntry.putString(code,"lock")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnFav.setOnClickListener {
            dataEntry.putString(code,"fav")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnLocation.setOnClickListener {
            dataEntry.putString(code,"location")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnWifi.setOnClickListener {
            dataEntry.putString(code,"wifi")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnAirplane.setOnClickListener {
            dataEntry.putString(code,"airplane")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnBluetooth.setOnClickListener {
            dataEntry.putString(code,"bluetooth")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnPortrait.setOnClickListener {
            dataEntry.putString(code,"portrait")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnBoost.setOnClickListener {
            dataEntry.putString(code,"boost")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnFlash.setOnClickListener {
            dataEntry.putString(code,"flashlight")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnVolume.setOnClickListener {
            dataEntry.putString(code,"soundup")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnSound.setOnClickListener {
            dataEntry.putString(code,"sounddown")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        dBinding.btnData.setOnClickListener {
            dataEntry.putString(code,"data")
            dataEntry.apply()
            dataEntry.commit()
            chooseActionDialog?.dismiss()
            initializeView()

        }
        chooseActionDialog?.show()
    }

    private fun initializeView() {
        val spat = requireActivity().getSharedPreferences(
            "spat", Context.MODE_PRIVATE
        )
        binding.main1Img.setImageDrawable(spat.getString("setting1", "none")?.let { getUtilData(it) })
        binding.main2Img.setImageDrawable(spat.getString("setting2", "none")?.let { getUtilData(it) })
        binding.main3Img.setImageDrawable(spat.getString("setting3", "none")?.let { getUtilData(it) })
        binding.main4Img.setImageDrawable(spat.getString("setting4", "none")?.let { getUtilData(it) })
        binding.main5Img.setImageDrawable(spat.getString("setting5", "none")?.let { getUtilData(it) })
        binding.main6Img.setImageDrawable(spat.getString("setting6", "none")?.let { getUtilData(it) })
        binding.main7Img.setImageDrawable(spat.getString("setting7", "none")?.let { getUtilData(it) })
        binding.main8Img.setImageDrawable(spat.getString("setting8", "none")?.let { getUtilData(it) })

    }

    private fun getUtilData(code: String): Drawable {
        when (code) {
            "wifi" -> {
                return resources.getDrawable(R.drawable.wifi)
            }
            "bluetooth" -> {

                return resources.getDrawable(R.drawable.bluetooth)
            }
            "data" -> {
                return resources.getDrawable(R.drawable.data)
            }
            "portrait" -> {
                return resources.getDrawable(R.drawable.portrait)
            }
            "location" -> {

                return resources.getDrawable(R.drawable.location)

            }
            "soundup" -> {
                return resources.getDrawable(R.drawable.sound_up)
            }
            "airplane" -> {
                return resources.getDrawable(R.drawable.airplane)

            }
            "flashlight" -> {

                return resources.getDrawable(R.drawable.flash)
            }
            "sounddown" -> {
                return resources.getDrawable(R.drawable.sound_down)
            }
            "boost" -> {
                return resources.getDrawable(R.drawable.boost)
            }
            "home" -> {
                return resources.getDrawable(R.drawable.home)
            }
            "lock" -> {
                return resources.getDrawable(R.drawable.lock)
            }
            "fav" -> {
                return resources.getDrawable(R.drawable.heart)
            }
            "setting" -> {
                return resources.getDrawable(R.drawable.setting)
            }
            "none" -> {
                return resources.getDrawable(R.drawable.none)
            }
            else -> {
                return resources.getDrawable(R.drawable.none)
            }
        }
    }

    private fun buttonClicker() {
        binding.main1.setOnClickListener {
            dialogClicker("setting1")
        }
        binding.main2.setOnClickListener {
            dialogClicker("setting2")

        }
        binding.main3.setOnClickListener {
            dialogClicker("setting3")

        }
        binding.main4.setOnClickListener {
            dialogClicker("setting4")

        }
        binding.main5.setOnClickListener {
            dialogClicker("setting5")

        }
        binding.main6.setOnClickListener {
            dialogClicker("setting6")

        }
        binding.main7.setOnClickListener {
            dialogClicker("setting7")

        }
        binding.main8.setOnClickListener {
            dialogClicker("setting8")

        }
    }

    companion object {
        fun newInstance() =
            EditSettingFragment()
    }
}