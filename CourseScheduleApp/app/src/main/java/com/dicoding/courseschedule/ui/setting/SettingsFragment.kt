package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        val preferenceTheme = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        preferenceTheme?.setOnPreferenceChangeListener { _, newValue ->
            val mode = NightMode.valueOf(newValue.toString().uppercase(Locale.US))
            updateTheme(mode.value)
            Toast.makeText(requireContext(), "Theme has been changed", Toast.LENGTH_SHORT).show()
            true
        }

        val preferenceNotification =
            findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        val dailyReminder = DailyReminder()
        preferenceNotification?.setOnPreferenceChangeListener { _, newValue ->
            val checkValue = newValue as Boolean
            if (checkValue) dailyReminder.setDailyReminder(requireContext()) else dailyReminder.cancelAlarm(requireContext())
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}