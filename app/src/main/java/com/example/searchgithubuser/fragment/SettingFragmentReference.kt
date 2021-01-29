package com.example.searchgithubuser.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.searchgithubuser.AlarmReceiver
import com.example.searchgithubuser.R
import java.util.*

class SettingFragmentReference: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var language: String
    private lateinit var reminder: String

    private lateinit var languagePreference: Preference
    private lateinit var reminderPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver
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

        reminderPreference.setOnPreferenceChangeListener { compoundButton, isChecked ->
            if (isChecked as Boolean) {
                val reminderTime = "09:00"
                val reminderMessage = getString(R.string.msg_reminder)
                // reminder on
                context?.let { alarmReceiver.setRepeatingAlarm(it, reminderTime, reminderMessage) }

                reminderPreference.icon = resources.getDrawable(R.drawable.ic_alarm_on, null)
            } else {
                // reminder cancel
                context?.let { alarmReceiver.cancelAlarm(it) }

                reminderPreference.icon = resources.getDrawable(R.drawable.ic_access_alarm, null)
            }

            true
        }
    }

    private fun init(){
        alarmReceiver = AlarmReceiver()

        language = resources.getString(R.string.key_language)
        reminder = resources.getString(R.string.key_reminder)

        languageSelected = Locale.getDefault().displayLanguage

        languagePreference = findPreference<Preference>(language) as Preference
        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == language){
            languagePreference.summary = sharedPreferences.getString(language,
                this.languageSelected
            )
        }

        if (key == reminder){
            reminderPreference.isChecked = sharedPreferences.getBoolean(reminder, false)
        }
    }

    private fun setSummary(){
        val sharedPreferences = preferenceManager.sharedPreferences
        languagePreference.summary = sharedPreferences.getString(language, this.languageSelected)
        reminderPreference.isChecked = sharedPreferences.getBoolean(reminder, false)
    }

}