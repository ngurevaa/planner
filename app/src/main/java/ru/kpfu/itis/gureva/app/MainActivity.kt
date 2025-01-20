package ru.kpfu.itis.gureva.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gureva.core.designsystem.theme.PlannerTheme
import ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen.HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            PlannerTheme {
                HomeScreen(navigateToGroup = {})
            }
        }
    }
}
