package devmugi.sample.shared.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import devmugi.sample.shared.model.CardUi

private const val CARD_ASPECT_RATIO = 488f / 680f

@Composable
fun CardGridItem(
    card: CardUi,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(CARD_ASPECT_RATIO),
    ) {
        AsyncImage(
            model = card.normalImageUrl,
            contentDescription = card.name,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit,
        )
    }
}
