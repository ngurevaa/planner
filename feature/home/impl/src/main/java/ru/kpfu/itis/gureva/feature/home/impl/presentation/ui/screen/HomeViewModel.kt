package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.gureva.core.utils.CalendarUtil
import ru.kpfu.itis.gureva.core.utils.ResourceManager
import ru.kpfu.itis.gureva.feature.home.api.model.Group
import ru.kpfu.itis.gureva.feature.home.api.usecase.GetAllGroupsUseCase
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import ru.kpfu.itis.gureva.feature.home.impl.R
import javax.inject.Inject

data class HomeScreenState(
    val weekday: String = "",
    val date: String = "",
    val groups: List<Group> = listOf(),
    val searchState: String = "",
    val searchFocused: Boolean = false,
    val isGroupCreateBottomSheetVisible: Boolean = false,
    val groupNameError: String? = null,
    val errorId: Int = 0
)

sealed interface HomeScreenAction {
    data class SearchGroup(val search: String) : HomeScreenAction
    data class FocusOnSearch(val focus: Boolean) : HomeScreenAction
    data object CleanSearch : HomeScreenAction
    data object OpenGroupCreateBottomSheet : HomeScreenAction
    data object CloseGroupCreateBottomSheet : HomeScreenAction
    data class SaveGroup(val name: String) : HomeScreenAction
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
    private val resourceManager: ResourceManager,
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
            is HomeScreenAction.SaveGroup -> saveGroup(action.name)
        }
    }

    private fun saveGroup(name: String) = intent {
        if (name.trim().isEmpty()) {
            reduce { state.copy(
                groupNameError = resourceManager.getString(R.string.empty_group_name_error),
                errorId = state.errorId + 1
            ) }
        }
        else if (saveGroupUseCase(name)) {
            val groups = getAllGroupsUseCase()
            reduce { state.copy(groups = groups, groupNameError = null, errorId = 0) }
            postSideEffect(HomeScreenSideEffect.CloseBottomSheet)
        }
        else {
            reduce { state.copy(
                groupNameError = resourceManager.getString(R.string.existing_group_name_error),
                errorId = state.errorId + 1
            ) }
        }
    }

    private fun closeGroupCreateBottomSheet() = intent {
        reduce { state.copy(isGroupCreateBottomSheetVisible = false, groupNameError = null, errorId = 0) }
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
