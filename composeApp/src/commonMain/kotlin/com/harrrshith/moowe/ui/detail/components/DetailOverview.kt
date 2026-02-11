package com.harrrshith.moowe.ui.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.theme.AppTheme

fun LazyListScope.detailOverview(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    item {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "overview",
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = movie.overview,
                style = AppTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                color = AppTheme.colorScheme.onSurface.copy(alpha = 0.87f)
            )
        }
    }
}
