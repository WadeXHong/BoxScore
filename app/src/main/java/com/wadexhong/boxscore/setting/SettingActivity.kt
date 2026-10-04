package com.wadexhong.boxscore.setting

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.database.BoxScoreDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingActivity : ComponentActivity() {
    
    private lateinit var viewModel: SettingsViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = SettingsRepository(dataStore)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(repository, BoxScoreDatabase.getDatabase(applicationContext)) as T
            }
        })[SettingsViewModel::class.java]
        
        val isShowLogout = intent.getBooleanExtra(Constants.ExtraNames.SETTING_BOOLEAN_IS_SHOW_LOGOUT, false)
        
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                val brightness = state.brightness
                BoxScore.sBrightness = brightness
                val layoutParams = window.attributes
                layoutParams.screenBrightness = brightness
                window.attributes = layoutParams
            }
        }
        
        setContent {
            MaterialTheme {
                SettingsScreen(
                    viewModel = viewModel,
                    isShowLogout = isShowLogout,
                    onFinish = {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}
