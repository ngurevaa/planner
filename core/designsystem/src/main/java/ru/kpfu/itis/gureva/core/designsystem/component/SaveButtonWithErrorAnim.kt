package ru.kpfu.itis.gureva.core.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.kpfu.itis.gureva.core.ui.noRippleClickable

@Composable
fun SaveButtonWithErrorAnim(
    error: String?,
    errorId: Int,
    dispatch: () -> Unit
) {
    var errorVisibility by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .noRippleClickable {
                    if (!errorVisibility) {
                        dispatch()
                    }
                },
        ) {
            LaunchedEffect(errorId) {
                if (errorId != 0) errorVisibility = true
            }
        }

        Crossfade(
            targetState = errorVisibility,
            animationSpec = tween(1000),
            label = ""
        ) { visible ->
            if (visible) {
                ErrorMessage(error ?: "")

                LaunchedEffect(errorVisibility) {
                    if (errorVisibility) {
                        delay(1500)
                        errorVisibility = false
                    }
                }
            }
            else {
                SaveButton()
            }
        }
    }
}

@Composable
fun SaveButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null
        )
    }
}

@Composable
fun ErrorMessage(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}
