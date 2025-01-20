package ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.core.utils.ResourceManager
import ru.kpfu.itis.gureva.feature.home.api.usecase.SaveGroupUseCase
import ru.kpfu.itis.gureva.feature.home.impl.R
import javax.inject.Inject

sealed interface GroupCreateBottomSheetEvent {
    data class OnGroupSaveClicked(val name: String): GroupCreateBottomSheetEvent
}

sealed class GroupCreateBottomSheetAction(val id: Int) {
    class ShowError(id: Int, val message: String) : GroupCreateBottomSheetAction(id)
    data object CloseBottomSheet: GroupCreateBottomSheetAction(1)
}

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private val resourceManager: ResourceManager,
    private val saveGroupUseCase: SaveGroupUseCase
) : ViewModel() {
    private val _action = MutableStateFlow<GroupCreateBottomSheetAction?>(null)
    val action: StateFlow<GroupCreateBottomSheetAction?>
        get() = _action.asStateFlow()

    private var groupNameErrorCount = 0

    fun obtainEvent(event: GroupCreateBottomSheetEvent) {
        when (event) {
            is GroupCreateBottomSheetEvent.OnGroupSaveClicked -> onGroupSaveClicked(event.name)
        }
    }

    private fun onGroupSaveClicked(name: String) {
        viewModelScope.launch {
            if (name.trim().isEmpty()) {
                _action.update { GroupCreateBottomSheetAction.ShowError(++groupNameErrorCount, resourceManager.getString(
                    R.string.empty_group_name_error)) }
            }
            else if (saveGroupUseCase(name)) {
                _action.update { GroupCreateBottomSheetAction.CloseBottomSheet }
            }
            else {
                _action.update { GroupCreateBottomSheetAction.ShowError(++groupNameErrorCount, resourceManager.getString(
                    R.string.existing_group_name_error)) }
            }
        }
    }
}
