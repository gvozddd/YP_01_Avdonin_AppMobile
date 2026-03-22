package com.example.collegeschedule.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun getBuildingColor(buildingName: String): Color {
    return when (buildingName.lowercase()) {
        "главный корпус", "главный" -> Color(0xFF4CAF50)   // зелёный
        "корпус б", "б" -> Color(0xFF2196F3)              // синий
        "корпус в", "в" -> Color(0xFFFF9800)              // оранжевый
        else -> Color(0xFF9E9E9E)                          // серый
    }
}

fun getLessonIcon(subject: String): ImageVector {
    return when {
        subject.contains("физик", ignoreCase = true) -> Icons.Default.Science
        subject.contains("математик", ignoreCase = true) -> Icons.Default.Calculate
        subject.contains("программирование", ignoreCase = true) -> Icons.Default.Code
        subject.contains("язык", ignoreCase = true) -> Icons.Default.Translate
        subject.contains("база данных", ignoreCase = true) -> Icons.Default.Storage
        subject.contains("сеть", ignoreCase = true) -> Icons.Default.Router
        subject.contains("дизайн", ignoreCase = true) -> Icons.Default.DesignServices
        subject.contains("экономик", ignoreCase = true) -> Icons.Default.MonetizationOn
        else -> Icons.Default.School
    }
}