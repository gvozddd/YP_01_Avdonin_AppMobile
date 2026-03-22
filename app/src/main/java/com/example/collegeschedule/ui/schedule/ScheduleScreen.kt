package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Компонент выбора группы
        GroupSelector(
            groups = uiState.groups,
            selectedGroup = uiState.selectedGroup,
            onGroupSelected = { group ->
                viewModel.selectGroup(group)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text("Ошибка: ${uiState.error}")
            }
            else -> {
                ScheduleList(data = uiState.schedule)
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }
}