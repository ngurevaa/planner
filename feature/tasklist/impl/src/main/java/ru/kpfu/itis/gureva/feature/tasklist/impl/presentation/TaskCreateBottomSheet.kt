package ru.kpfu.itis.gureva.feature.tasklist.impl.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kpfu.itis.gureva.core.designsystem.component.BottomSheetDragHandle
import ru.kpfu.itis.gureva.core.designsystem.component.SaveButtonWithErrorAnim
import ru.kpfu.itis.gureva.core.designsystem.theme.bodyFontFamily
import ru.kpfu.itis.gureva.core.ui.noRippleClickable
import ru.kpfu.itis.gureva.feature.tasklist.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateBottomSheet(viewModel: GroupViewModel) {
    val state by viewModel.collectAsState()
    val dispatch = viewModel::dispatch

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { dispatch(GroupScreenAction.CloseTaskCreateBottomSheet) },
        shape = RoundedCornerShape(8.dp),
        dragHandle = { BottomSheetDragHandle(title = stringResource(id = R.string.new_task)) {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        dispatch(GroupScreenAction.CloseTaskCreateBottomSheet)
                    }
                }
        } }
    ) {
        TaskCreateBottomSheetContent(state, dispatch)
    }

    viewModel.collectSideEffect {
        when (it) {
            is GroupScreenSideEffect.CloseBottomSheet -> {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            dispatch(GroupScreenAction.CloseTaskCreateBottomSheet)
                        }
                    }
            }
        }
    }
}

@Composable
fun TaskCreateBottomSheetContent(
    state: GroupScreenState,
    dispatch: (GroupScreenAction) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        TaskField(state, dispatch)
        Spacer(modifier = Modifier.height(24.dp))
//        TaskSettings(state, dispatch)
        Spacer(modifier = Modifier.height(8.dp))
    }

    SaveButtonWithErrorAnim(error = state.taskError, errorId = state.errorId) {
        dispatch(GroupScreenAction.SaveTask)
    }
}

@Composable
fun TaskField(
    state: GroupScreenState,
    dispatch: (GroupScreenAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = state.task,
        onValueChange = {
            dispatch(GroupScreenAction.UpdateTask(it))
        },
        modifier = Modifier.focusRequester(focusRequester),
        textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 18.sp, lineHeight = 28.sp),
        minLines = 5,
        maxLines = 5
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

//@Composable
//fun TaskSettings(
//    state: GroupScreenState,
//    dispatch: (GroupScreenAction) -> Unit
////    calendar: CalendarUtil
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (state.selectedDay == null) {
//            Icon(
//                painter = painterResource(id = R.drawable.calendar_svgrepo_com),
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier
//                    .size(38.dp)
//                    .padding(end = 20.dp)
//                    .noRippleClickable {
//                        sheetEventHandler(CreateTaskBottomSheetEvent.OnCalendarClicked)
//                    }
//            )
//        }
//        else {
//            Text(
//                text = calendar.getFullDate(state.selectedDay),
//                style = MaterialTheme.typography.titleSmall,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier
//                    .padding(end = 20.dp)
//                    .noRippleClickable {
//                        sheetEventHandler(CreateTaskBottomSheetEvent.OnCalendarClicked)
//                    }
//            )
//        }
//
//
//        Icon(
//            painter = painterResource(id = R.drawable.notification_off_bell_alarm_svgrepo_com),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier
//                .size(38.dp)
//                .padding(end = 20.dp)
//        )
//
//        Icon(
//            painter = painterResource(id = R.drawable.repeat_play_svgrepo_com),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier
//                .size(38.dp)
//                .padding(end = 20.dp)
//        )
//
//        Icon(
//            painter = painterResource(id = R.drawable.icons8_google_docs),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier
//                .size(38.dp)
//                .padding(end = 20.dp)
//        )
//    }
//}
