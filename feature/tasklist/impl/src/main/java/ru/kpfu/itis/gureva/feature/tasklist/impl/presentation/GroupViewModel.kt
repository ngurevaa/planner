package ru.kpfu.itis.gureva.feature.tasklist.impl.presentation

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.gureva.feature.tasklist.api.model.Task
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetGroupNameUseCase
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.GetTasksUseCase

data class GroupScreenState(
    val groupName: String = "",
    val tasks: List<Task> = listOf(),
    val isCreateTaskBottomSheetVisible: Boolean = false,
    val task: String = "",
    val taskError: String? = null,
    val errorId: Int = 0
)

sealed interface GroupScreenAction {
    data object OpenTaskCreateBottomSheet : GroupScreenAction
    data object CloseTaskCreateBottomSheet : GroupScreenAction
    data class UpdateTask(val task: String) : GroupScreenAction
    data object SaveTask : GroupScreenAction
}

sealed interface GroupScreenSideEffect {
    data object CloseBottomSheet : GroupScreenSideEffect
}

@HiltViewModel(assistedFactory = GroupViewModel.Factory::class)
class GroupViewModel @AssistedInject constructor(
    @Assisted private val groupId: Int?,
    private val getGroupNameUseCase: GetGroupNameUseCase,
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel(), ContainerHost<GroupScreenState, GroupScreenSideEffect> {

    override val container = container<GroupScreenState, GroupScreenSideEffect>(GroupScreenState())

    init {
        intent {
            val name = getGroupNameUseCase(groupId ?: 1) ?: ""
            val tasks = getTasksUseCase(groupId ?: 1)
            reduce { state.copy(groupName = name, tasks = tasks) }
        }
    }

    fun dispatch(action: GroupScreenAction) {
        when (action) {
            is GroupScreenAction.CloseTaskCreateBottomSheet -> closeTaskCreateBottomSheet()
            is GroupScreenAction.OpenTaskCreateBottomSheet -> openTaskCreateBottomSheet()
            is GroupScreenAction.UpdateTask -> updateTask(action.task)
            is GroupScreenAction.SaveTask -> saveTask()
        }
    }

    private fun saveTask() = intent {
        reduce { state.copy(errorId = state.errorId + 1, taskError = "ploxo") }
    }

    private fun updateTask(task: String) = intent {
        if (task.length == 1 && task[0] == ' ') return@intent
        reduce { state.copy(task = task) }
    }

    private fun openTaskCreateBottomSheet() = intent {
        reduce { state.copy(isCreateTaskBottomSheetVisible = true) }
    }

    private fun closeTaskCreateBottomSheet() = intent {
        reduce { state.copy(isCreateTaskBottomSheetVisible = false, errorId = 0, taskError = null) }
    }

    @AssistedFactory
    interface Factory {
        fun create(groupId: Int?): GroupViewModel
    }
}
