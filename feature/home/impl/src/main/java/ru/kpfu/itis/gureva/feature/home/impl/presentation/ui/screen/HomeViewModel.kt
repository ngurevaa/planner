package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.core.database.entity.GroupEntity
import ru.kpfu.itis.gureva.core.utils.CalendarUtil
import ru.kpfu.itis.gureva.feature.home.api.model.Group
import ru.kpfu.itis.gureva.feature.home.api.repository.HomeRepository
import ru.kpfu.itis.gureva.feature.home.api.usecase.GetAllGroupsUseCase
import javax.inject.Inject

data class HomeScreenState(
    val weekday: String = "",
    val date: String = "",
    val groups: List<Group> = listOf(),
    val searchState: String = "",
    val expanded: Boolean = true,
    val showBottomSheet: Boolean = false
)

sealed interface HomeScreenEvent {
    data class OnSearchChanged(val search: String) : HomeScreenEvent
    data class OnFocusChanged(val focus: Boolean) : HomeScreenEvent
    data object OnCanselClicked : HomeScreenEvent
    data object OnGroupCreateClicked: HomeScreenEvent
    data object OnBottomSheetClose: HomeScreenEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calendar: CalendarUtil,
    private val getAllGroupsUseCase: GetAllGroupsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        HomeScreenState(
            weekday = calendar.getWeekday().uppercase(),
            date = calendar.getDate()
        )
    )
    val state: StateFlow<HomeScreenState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(groups = getAllGroupsUseCase())
            }
        }
    }

    fun obtainEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnSearchChanged -> onSearchChanged(event.search)
            is HomeScreenEvent.OnFocusChanged -> onFocusChanged(event.focus)
            is HomeScreenEvent.OnCanselClicked -> onCanselClicked()
            is HomeScreenEvent.OnGroupCreateClicked -> onGroupCreateClicked()
            is HomeScreenEvent.OnBottomSheetClose -> onBottomSheetClose()
        }
    }

    private fun onBottomSheetClose() {
        viewModelScope.launch {
            _state.update {
                it.copy(groups = getAllGroupsUseCase(), showBottomSheet = false)
            }
        }
    }


    private fun onGroupCreateClicked() {
        _state.update { it.copy(showBottomSheet = true) }
    }

    private fun onSearchChanged(search: String) {
        _state.update { it.copy(searchState = search) }
    }

    private fun onFocusChanged(focus: Boolean) {
        _state.update { it.copy(expanded = focus) }
    }

    private fun onCanselClicked() {
        _state.update { it.copy(searchState = "") }
    }
}
