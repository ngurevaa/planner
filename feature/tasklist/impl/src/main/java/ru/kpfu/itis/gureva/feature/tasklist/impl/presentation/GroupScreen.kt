package ru.kpfu.itis.gureva.feature.tasklist.impl.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kpfu.itis.gureva.core.ui.noRippleClickable
import ru.kpfu.itis.gureva.feature.tasklist.impl.R

@Composable
fun GroupScreen(groupId: Long?) {
    val viewModel = hiltViewModel<GroupViewModel, GroupViewModel.Factory>(
        creationCallback = {factory -> factory.create(groupId) }
    )
    val state by viewModel.collectAsState()
    val dispatch = viewModel::dispatch

    if (state.isCreateTaskBottomSheetVisible) {
        TaskCreateBottomSheet(viewModel)
    }
    GroupScreenContent(state, dispatch)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreenContent(
    state: GroupScreenState,
    dispatch: (GroupScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = state.groupName,
                    style = MaterialTheme.typography.headlineMedium
                ) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            CreateTaskButton(dispatch)
        }
    ) { innerPadding ->
    }
}

@Composable
fun CreateTaskButton(
    dispatch: (GroupScreenAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                dispatch(GroupScreenAction.OpenTaskCreateBottomSheet)
            }
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(3.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = stringResource(id = R.string.btn_create_new_task),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
