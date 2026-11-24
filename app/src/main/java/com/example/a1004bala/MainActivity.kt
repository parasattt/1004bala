package com.example.a1004bala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a1004bala.data.preferences.ThemeMode
import com.example.a1004bala.ui.AppViewModelProvider
import com.example.a1004bala.ui.MainUiState
import com.example.a1004bala.ui.MainViewModel
import com.example.a1004bala.ui.navigation.BookWormNavGraph
import com.example.a1004bala.ui.theme.BookWormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()

            when (val s = state) {
                MainUiState.Loading -> Unit

                is MainUiState.Ready -> {
                    val darkTheme = when (s.themeMode) {
                        ThemeMode.LIGHT -> false
                        ThemeMode.DARK -> true
                        ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    }
                    BookWormTheme(darkTheme = darkTheme) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val navController = rememberNavController()
                            BookWormNavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}