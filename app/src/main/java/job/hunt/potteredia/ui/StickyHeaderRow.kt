package job.hunt.potteredia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeaderRow(initial: Char) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Header(initial)
        Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
    }
}

@Composable
fun Header(initial: Char) {
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = initial.toString(),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize
    )
}
