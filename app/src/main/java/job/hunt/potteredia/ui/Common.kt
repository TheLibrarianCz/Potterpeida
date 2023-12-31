package job.hunt.potteredia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingCommon() {
    PlainCenteredContent {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorCommon(
    infoMessage: String? = null,
    errorMessage: String? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    PlainCenteredContent {
        if (infoMessage != null) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = infoMessage
            )
        }

        if (snackbarHostState != null && errorMessage != null) {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }
}

@Composable
private fun PlainCenteredContent(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}
