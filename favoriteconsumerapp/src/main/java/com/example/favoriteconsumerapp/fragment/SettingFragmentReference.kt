package com.example.favoriteconsumerapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.favoriteconsumerapp.R
import java.util.*

class SettingFragmentReference: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var language: String

    private lateinit var languagePreference: Preference

    private lateinit var languageSelected: String


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
        init()
        setSummary()

        languagePreference.setOnPreferenceClickListener {
            // open setting language
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }

    }

    private fun init(){
        language = resources.getString(R.string.key_language)

        languageSelected = Locale.getDefault().displayLanguage

        languagePreference = findPreference<Preference>(language) as Preference

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == language){
            languagePreference.summary = sharedPreferences.getString(language,
                this.languageSelected
            )
        }
    }

    private fun setSummary(){
        val sharedPreferences = preferenceManager.sharedPreferences
        languagePreference.summary = sharedPreferences.getString(language, this.languageSelected)
    }

}