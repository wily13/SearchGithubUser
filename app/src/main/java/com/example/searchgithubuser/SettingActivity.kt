package com.example.searchgithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.searchgithubuser.fragment.SettingFragmentReference

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        supportActionBar?.title = getString(R.string.title_activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.holder_setting,
            SettingFragmentReference()
        ).commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}