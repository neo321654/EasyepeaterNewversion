package com.spotolcom.easyrepeater

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


class MySettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val timeRepeat: EditTextPreference? = findPreference("time_repeat")

        timeRepeat?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Время не установлено"
                } else {
                    "Интервал повторений равен: $text"
                }
            }
        timeRepeat?.setOnBindEditTextListener {
                editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

    }
}