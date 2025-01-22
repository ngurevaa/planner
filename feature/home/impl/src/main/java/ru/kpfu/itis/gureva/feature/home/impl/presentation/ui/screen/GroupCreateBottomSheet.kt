package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kpfu.itis.gureva.core.designsystem.component.BottomSheetDragHandle
import ru.kpfu.itis.gureva.core.designsystem.theme.bodyFontFamily
import ru.kpfu.itis.gureva.core.ui.noRippleClickable
import ru.kpfu.itis.gureva.feature.home.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCreateBottomSheet(
    viewModel: HomeViewModel
) {
    val state by viewModel.collectAsState()
    val dispatch = viewModel::dispatch

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { dispatch(HomeScreenAction.CloseGroupCreateBottomSheet) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = { BottomSheetDragHandle(title = stringResource(id = R.string.new_list)) {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        dispatch(HomeScreenAction.CloseGroupCreateBottomSheet)
                    }
                }
        } }
    ) {
        GroupCreateBottomSheetContent(state, dispatch)
    }

    viewModel.collectSideEffect {
        when (it) {
            is HomeScreenSideEffect.CloseBottomSheet -> {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            dispatch(HomeScreenAction.CloseGroupCreateBottomSheet)
                        }
                    }
            }
            is HomeScreenSideEffect.NavigateToGroup -> TODO()
        }
    }
}

@Composable
fun GroupCreateBottomSheetContent(
    state: HomeScreenState,
    dispatch: (HomeScreenAction) -> Unit,
) {
    Column {
        GroupNameField(state, dispatch)
        SaveGroupButton(state, dispatch)
    }
}

@Composable
fun ColumnScope.GroupNameField(
    state: HomeScreenState,
    dispatch: (HomeScreenAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = state.groupName,
        onValueChange = {
            dispatch(HomeScreenAction.UpdateGroupName(it))
        },
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            .focusRequester(focusRequester),
        textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp, lineHeight = 28.sp),
        minLines = 5,
        maxLines = 5
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Text(
        text = "${state.groupName.length}/30",
        modifier = Modifier
            .align(Alignment.End)
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun SaveGroupButton(
    state: HomeScreenState,
    dispatch: (HomeScreenAction) -> Unit
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
                        dispatch(HomeScreenAction.SaveGroup)
                    }
                },
        ) {
            LaunchedEffect(state.errorId) {
                if (state.errorId != 0) errorVisibility = true
            }
        }

        Crossfade(
            targetState = errorVisibility,
            animationSpec = tween(1000),
            label = ""
        ) { visible ->
            if (visible) {
                ErrorMessage(state.groupNameError ?: "")

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
