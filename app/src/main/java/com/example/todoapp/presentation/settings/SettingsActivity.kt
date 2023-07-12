package com.example.todoapp.presentation.settings


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivitySettingsBinding
import com.example.todoapp.presentation.notification.NotificationWorker
import com.example.todoapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.setting, SettingsFragment())
                .commit()
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detail Task"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                newValue?.let {
                    when (it as Boolean) {
                        true -> {
                            val builder = PeriodicWorkRequestBuilder<NotificationWorker>(
                                1,
                                TimeUnit.DAYS
                            ).apply {
                                setInputData(
                                    Data.Builder().putString(NOTIFICATION_CHANNEL_ID, channelName)
                                        .build()
                                )
                            }.build()
                            WorkManager.getInstance(requireContext()).enqueue(builder)
                        }

                        false -> {
                            val workManager = WorkManager.getInstance(requireContext())
                            workManager.cancelUniqueWork(channelName)
                        }
                    }
                }
                true
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}