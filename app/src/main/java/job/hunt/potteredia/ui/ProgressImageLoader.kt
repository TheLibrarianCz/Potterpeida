package job.hunt.potteredia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import job.hunt.potteredia.R

@Composable
fun ProgressImageLoader(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )
    when (painter.state) {
        AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            Loading(modifier)
        }

        is AsyncImagePainter.State.Error -> {
            Error(
                modifier,
                painter = if (imageUrl.isBlank()) {
                    painterResource(id = R.drawable.ic_image)
                } else {
                    painterResource(id = R.drawable.ic_no_internet)
                }
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                modifier = modifier,
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
private fun Error(
    modifier: Modifier,
    painter: Painter
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            painter = painter,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null
        )
    }
}

@Composable
private fun Loading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
