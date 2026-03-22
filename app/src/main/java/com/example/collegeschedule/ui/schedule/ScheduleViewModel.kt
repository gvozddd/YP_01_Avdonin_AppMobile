package com.example.collegeschedule.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange

data class ScheduleUiState(
    val groups: List<GroupDto> = emptyList(),
    val selectedGroup: GroupDto? = null,
    val schedule: List<ScheduleByDateDto> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class ScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository(RetrofitInstance.api)
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val groups = repository.getGroups()
                _uiState.value = _uiState.value.copy(groups = groups, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, loading = false)
            }
        }
    }

    fun selectGroup(group: GroupDto) {
        _uiState.value = _uiState.value.copy(selectedGroup = group, loading = true, error = null)
        loadScheduleForGroup(group.groupName)
    }

    private fun loadScheduleForGroup(groupName: String) {
        viewModelScope.launch {
            val (start, end) = getWeekDateRange()
            try {
                val schedule = repository.loadSchedule(groupName, start, end)
                _uiState.value = _uiState.value.copy(schedule = schedule, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, loading = false)
            }
        }
    }
}