package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.gureva.core.utils.CalendarUtil
import ru.kpfu.itis.gureva.core.utils.ResourceManager
import ru.kpfu.itis.gureva.feature.home.api.model.Group
import ru.kpfu.itis.gureva.feature.home.api.usecase.GetAllGroupsUseCase
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import ru.kpfu.itis.gureva.feature.home.impl.exception.EmptyGroupNameException
import ru.kpfu.itis.gureva.feature.home.impl.exception.AlreadyExistingGroupNameException
import javax.inject.Inject

data class HomeScreenState(
    val weekday: String = "",
    val date: String = "",
    val groups: List<Group> = listOf(),
    val searchState: String = "",
    val searchFocused: Boolean = false,
    val isGroupCreateBottomSheetVisible: Boolean = false,
    val groupNameError: String? = null,
    val errorId: Int = 0,
    val groupName: String = ""
)

sealed interface HomeScreenAction {
    data class SearchGroup(val search: String) : HomeScreenAction
    data class FocusOnSearch(val focus: Boolean) : HomeScreenAction
    data object CleanSearch : HomeScreenAction
    data object OpenGroupCreateBottomSheet : HomeScreenAction
    data object CloseGroupCreateBottomSheet : HomeScreenAction
    data object SaveGroup : HomeScreenAction
    data class UpdateGroupName(val name: String): HomeScreenAction
}

sealed interface HomeScreenSideEffect {
    data class NavigateToGroup(val id: Int) : HomeScreenSideEffect
    data object CloseBottomSheet : HomeScreenSideEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    calendar: CalendarUtil,
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val saveGroupUseCase: SaveGroupUseCase,
) : ViewModel(), ContainerHost<HomeScreenState, HomeScreenSideEffect> {

    override val container = container<HomeScreenState, HomeScreenSideEffect>(HomeScreenState())

    init {
        intent {
            val groups = getAllGroupsUseCase()
            reduce { state.copy(
                weekday = calendar.getWeekday().uppercase(),
                date = calendar.getDate(),
                groups = groups
            ) }
        }
    }

    fun dispatch(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.SearchGroup -> searchGroup(action.search)
            is HomeScreenAction.FocusOnSearch -> focusOnSearch(action.focus)
            is HomeScreenAction.CleanSearch -> canselSearch()
            is HomeScreenAction.OpenGroupCreateBottomSheet -> openGroupCreateBottomSheet()
            is HomeScreenAction.CloseGroupCreateBottomSheet -> closeGroupCreateBottomSheet()
            is HomeScreenAction.SaveGroup -> saveGroup()
            is HomeScreenAction.UpdateGroupName -> updateGroupName(action.name)
        }
    }

    private fun updateGroupName(name: String) = intent {
        if (name.length == 1 && name[0] == ' ') return@intent
        if (name.length <= 30) reduce { state.copy(groupName = name) }
    }

    private fun saveGroup() = intent {
        runCatching { saveGroupUseCase(state.groupName) }
            .onSuccess { id ->
                val groups = state.groups.toMutableList()
                groups.add(Group(id, state.groupName))

                reduce { state.copy(groups = groups, groupNameError = null, errorId = 0) }
                postSideEffect(HomeScreenSideEffect.CloseBottomSheet)
            }
            .onFailure { ex ->
                when (ex) {
                    is EmptyGroupNameException, is AlreadyExistingGroupNameException -> {
                        reduce { state.copy(groupNameError = ex.message, errorId = state.errorId + 1) }
                    }
                    else -> throw ex
                }
            }
    }

    private fun closeGroupCreateBottomSheet() = intent {
        reduce { state.copy(isGroupCreateBottomSheetVisible = false, groupNameError = null, errorId = 0, groupName = "") }
    }

    private fun openGroupCreateBottomSheet() = intent {
        reduce { state.copy(isGroupCreateBottomSheetVisible = true) }
    }

    private fun searchGroup(search: String) = intent {
        reduce { state.copy(searchState = search) }
    }

    private fun focusOnSearch(focus: Boolean) = intent {
        reduce { state.copy(searchFocused = focus) }
    }

    private fun canselSearch() = intent {
        reduce { state.copy(searchState = "") }
    }
}
