package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.core.designsystem.component.BottomSheetDragHandle
import ru.kpfu.itis.gureva.core.designsystem.theme.bodyFontFamily
import ru.kpfu.itis.gureva.core.ui.noRippleClickable
import ru.kpfu.itis.gureva.feature.home.impl.R

@Composable
fun GroupCreateBottomSheet(
    viewModelSheet: GroupCreateViewModel = hiltViewModel(),
    viewModelScreen: HomeViewModel = hiltViewModel()
) {
    val eventHandlerSheet = viewModelSheet::obtainEvent
    val eventHandlerScreen = viewModelScreen::obtainEvent
    val action by viewModelSheet.action.collectAsStateWithLifecycle()

    GroupCreateBottomSheetContent(action, eventHandlerSheet, eventHandlerScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCreateBottomSheetContent(
    action: GroupCreateBottomSheetAction?,
    eventHandlerSheet: (GroupCreateBottomSheetEvent) -> Unit,
    eventHandlerScreen: (HomeScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { eventHandlerScreen(HomeScreenEvent.OnBottomSheetClose) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = { BottomSheetDragHandle(title = stringResource(id = R.string.new_list)) {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        eventHandlerScreen(HomeScreenEvent.OnBottomSheetClose)
                    }
                }
        } }
    ) {
        val groupName = remember {
            mutableStateOf("")
        }
        GroupNameField(groupName)
        SaveGroupButton(action, eventHandlerSheet, eventHandlerScreen, groupName, scope, sheetState)
    }
}

@Composable
fun ColumnScope.GroupNameField(
    groupName: MutableState<String>
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = groupName.value,
        onValueChange = {
            if (it.length == 1 && it[0] == ' ') return@BasicTextField
            if (it.length <= 30) groupName.value = it
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
        text = "${groupName.value.length}/30",
        modifier = Modifier
            .align(Alignment.End)
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveGroupButton(
    action: GroupCreateBottomSheetAction?,
    eventHandlerSheet: (GroupCreateBottomSheetEvent) -> Unit,
    eventHandlerScreen: (HomeScreenEvent) -> Unit,
    groupName: MutableState<String>,
    scope: CoroutineScope,
    sheetState: SheetState
) {
    var visibility by remember {
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
                    if (!visibility) {
                        eventHandlerSheet(GroupCreateBottomSheetEvent.OnGroupSaveClicked(groupName.value))
                    }
                },
        ) {
            var first by remember {
                mutableStateOf(true)
            }
            LaunchedEffect(key1 = action) {
                when (action) {
                    is GroupCreateBottomSheetAction.ShowError -> {
                        if (action.id != 0) visibility = true
//                        if (!first) visibility = true
//                        else first = false
                    }
                    is GroupCreateBottomSheetAction.CloseBottomSheet -> {
                        scope.launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    eventHandlerScreen(HomeScreenEvent.OnBottomSheetClose)
                                }
                            }
                    }
                    else -> {}
                }
            }

            Crossfade(
                targetState = visibility,
                animationSpec = tween(1000),
                label = ""
            ) { visible ->
                if (visible) {
                    if (action is GroupCreateBottomSheetAction.ShowError) {
                        ErrorMessage((action as? GroupCreateBottomSheetAction.ShowError)?.message.toString())

                        LaunchedEffect(visibility) {
                            if (visibility) {
                                delay(1500)
                                visibility = false
                            }
                        }
                    }
                }
                else {
                    SaveButton()
                }
            }
        }
    }
}

@Composable
fun SaveButton() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Filled.Add,
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
            .fillMaxSize()
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
