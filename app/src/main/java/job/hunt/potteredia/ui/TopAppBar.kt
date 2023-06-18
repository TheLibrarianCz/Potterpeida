@file:OptIn(ExperimentalMaterial3Api::class)

package job.hunt.potteredia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import job.hunt.potteredia.R

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit = { },
    isPrimaryBackground: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    isTopDestination: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = {
            TopAppBarTitle(title, isPrimaryBackground)
        },
        navigationIcon = {
            if (!isTopDestination) {
                NavigationIcon(
                    isPrimaryBackground = isPrimaryBackground,
                    contentDescription = stringResource(id = R.string.up),
                    onNavigationIconClick = onNavigationIconClick
                )
            }
        },
        modifier = modifier,
        actions = actions,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = if (isPrimaryBackground) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.background
            }
        )
    )
}

@Composable
private fun TopAppBarTitle(
    title: String,
    isPrimaryBackground: Boolean
) {
    Text(
        text = title,
        color = if (isPrimaryBackground) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.primary
        },
        fontWeight = FontWeight.Bold,
        fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize
    )
}

@Composable
private fun NavigationIcon(
    isPrimaryBackground: Boolean,
    contentDescription: String? = null,
    onNavigationIconClick: () -> Unit
) {
    Icon(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = onNavigationIconClick,
                indication = rememberRipple(bounded = false)
            ),
        imageVector = Icons.Default.ArrowBack,
        tint = if (isPrimaryBackground) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.primary
        },
        contentDescription = contentDescription
    )
}
