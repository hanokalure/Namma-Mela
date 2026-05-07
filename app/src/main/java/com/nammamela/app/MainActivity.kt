package com.nammamela.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.nammamela.app.data.session.UserSession
import com.nammamela.app.domain.repository.AppRepository
import com.nammamela.app.navigation.NammaMelaNavGraph
import com.nammamela.app.ui.theme.NammaMelaTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val freshStartDone =
                dataStore.data.first()[booleanPreferencesKey(FRESH_START_PREFS_KEY)] == true
            if (!freshStartDone) {
                repository.wipeDatabase()
                userSession.clearSessionAfterDatabaseWipe()
                dataStore.edit { prefs ->
                    prefs[booleanPreferencesKey(FRESH_START_PREFS_KEY)] = true
                }
            } else {
                userSession.restore()
            }
        }

        setContent {
            NammaMelaTheme {
                val navController = androidx.navigation.compose.rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NammaMelaNavGraph(navController = navController)
                }
            }
        }
    }

    private companion object {
        /** One-time full reset; after first run, session restores normally. */
        const val FRESH_START_PREFS_KEY = "nammamela_fresh_db_reset_v1"
    }
}
