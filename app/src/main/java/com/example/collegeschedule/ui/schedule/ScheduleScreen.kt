package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.local.FavoritesManager
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    selectedGroup: GroupDto?,                     // принимаем выбранную группу из родителя
    onGroupSelected: (GroupDto) -> Unit,          // колбэк для выбора группы
    viewModel: ScheduleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val favoritesManager = remember { FavoritesManager(context) }
    val favoriteGroups by favoritesManager.favoritesFlow.collectAsState(initial = emptySet())
    val coroutineScope = rememberCoroutineScope()

    // Загрузка списка групп при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    // Если selectedGroup изменился извне (например, из избранного), синхронизируем ViewModel
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { viewModel.selectGroup(it) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GroupSelector(
                groups = uiState.groups,
                selectedGroup = selectedGroup,          // используем переданную группу
                onGroupSelected = onGroupSelected,      // передаём выбор наверх
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка «Избранное» для текущей группы
            if (selectedGroup != null) {
                val isFavorite = favoriteGroups.contains(selectedGroup!!.groupName)
                IconButton(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            if (isFavorite) {
                                favoritesManager.removeFavorite(selectedGroup!!.groupName)
                            } else {
                                favoritesManager.addFavorite(selectedGroup!!.groupName)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

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
}