package com.healthapp.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    items: List<String>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit
) {
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.CalendarToday,
        Icons.Filled.AccessTime
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item
                    )
                },
                label = { Text(text = item) },
                selected = selectedItem == index,
                onClick = { onItemClick(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3D7EF8),
                    selectedTextColor = Color(0xFF3D7EF8),
                    indicatorColor = Color(0xFFE6EBFF),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
