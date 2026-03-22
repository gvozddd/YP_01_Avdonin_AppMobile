package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.collegeschedule.data.dto.GroupDto

@Composable
fun GroupSelector(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue(selectedGroup?.groupName ?: "")) }

    val filteredGroups = remember(searchText.text, groups) {
        if (searchText.text.isBlank()) groups
        else groups.filter { it.groupName.contains(searchText.text, ignoreCase = true) }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Выберите группу") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Раскрыть список")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (filteredGroups.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Нет групп") },
                    onClick = { expanded = false }
                )
            } else {
                filteredGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.groupName) },
                        onClick = {
                            onGroupSelected(group)
                            searchText = TextFieldValue(group.groupName)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}