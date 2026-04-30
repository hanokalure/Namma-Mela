package com.nammamela.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nammamela.app.navigation.NammaMelaNavGraph
import com.nammamela.app.ui.theme.NammaMelaTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @javax.inject.Inject
    lateinit var repository: com.nammamela.app.domain.repository.AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        
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
}
