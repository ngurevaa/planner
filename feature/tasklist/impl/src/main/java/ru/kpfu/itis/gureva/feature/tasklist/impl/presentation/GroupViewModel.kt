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
import ru.kpfu.itis.gureva.feature.tasklist.api.usecase.SaveTaskUseCase
import ru.kpfu.itis.gureva.feature.tasklist.impl.exception.EmptyTaskException

data class GroupScreenState(
    val groupName: String = "",
    val tasks: List<Task> = listOf(),
    val isCreateTaskBottomSheetVisible: Boolean = false,
    val taskError: String? = null,
    val errorId: Int = 0,
    val task: Task = Task(null, "", null),
    val isCalendarBottomSheetVisible: Boolean = false
)

sealed interface GroupScreenAction {
    data object OpenTaskCreateBottomSheet : GroupScreenAction
    data object CloseTaskCreateBottomSheet : GroupScreenAction
    data class UpdateTask(val task: String) : GroupScreenAction
    data object SaveTask : GroupScreenAction
    data object OpenCalendarBottomSheet : GroupScreenAction
}

sealed interface GroupScreenSideEffect {
    data object CloseTaskCreateBottomSheet : GroupScreenSideEffect
}

@HiltViewModel(assistedFactory = GroupViewModel.Factory::class)
class GroupViewModel @AssistedInject constructor(
    @Assisted private val groupId: Long?,
    private val getGroupNameUseCase: GetGroupNameUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
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
            is GroupScreenAction.OpenCalendarBottomSheet -> TODO()
        }
    }

    private fun saveTask() = intent {
        runCatching { saveTaskUseCase(state.task.copy(name = state.task.name.trim())) }
            .onSuccess {
                reduce { state.copy(taskError = null, errorId = 0) }
                postSideEffect(GroupScreenSideEffect.CloseTaskCreateBottomSheet)
            }
            .onFailure { ex ->
                when (ex) {
                    is EmptyTaskException -> {
                        reduce { state.copy(taskError = ex.message, errorId = state.errorId + 1) }
                    }
                    else -> throw ex
                }
            }
    }

    private fun updateTask(task: String) = intent {
        if (task.length == 1 && task[0] == ' ') return@intent
        reduce { state.copy(task = state.task.copy(name = task)) }
    }

    private fun openTaskCreateBottomSheet() = intent {
        reduce { state.copy(isCreateTaskBottomSheetVisible = true) }
    }

    private fun closeTaskCreateBottomSheet() = intent {
        reduce { state.copy(isCreateTaskBottomSheetVisible = false, errorId = 0, taskError = null, task = Task(null, "", null)) }
    }

    @AssistedFactory
    interface Factory {
        fun create(groupId: Long?): GroupViewModel
    }
}
