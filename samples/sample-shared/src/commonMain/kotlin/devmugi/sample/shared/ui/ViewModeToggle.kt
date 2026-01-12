package devmugi.sample.shared.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import devmugi.sample.shared.model.ViewMode

@Composable
fun ViewModeToggle(
    mode: ViewMode,
    onModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        FilterChip(
            selected = mode == ViewMode.LIST,
            onClick = { onModeChange(ViewMode.LIST) },
            label = { Text("List") },
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = null,
                )
            },
            modifier = Modifier.padding(end = 8.dp),
        )
        FilterChip(
            selected = mode == ViewMode.GRID,
            onClick = { onModeChange(ViewMode.GRID) },
            label = { Text("Grid") },
            leadingIcon = {
                Icon(
                    Icons.Default.GridView,
                    contentDescription = null,
                )
            },
        )
    }
}
