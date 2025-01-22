package ru.kpfu.itis.gureva.feature.group.impl.presentation

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

data class GroupScreenState(
    val groupName: String = "",
    val tasks: List<TaskEntity> = listOf(),
    val isCreateTaskBottomSheetVisible: Boolean = false
)

sealed interface GroupScreenAction {
    data object OpenTaskCreateBottomSheet : GroupScreenAction
    data object CloseTaskCreateBottomSheet : GroupScreenAction
}



@HiltViewModel(assistedFactory = GroupScreenViewModel.Factory::class)
class GroupScreenViewModel @AssistedInject constructor(
    @Assisted val groupId: Int?
) : ViewModel(), ContainerHost<GroupScreenState, Nothing> {

    override val container = container<GroupScreenState, Nothing>(GroupScreenState())

    init {
        intent {

        }
    }

    @AssistedFactory
    interface Factory {
        fun create(groupId: Int?): GroupScreenViewModel
    }
}
