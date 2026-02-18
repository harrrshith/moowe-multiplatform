package com.harrrshith.moowe.ui.detail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Review
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format
import kotlin.math.absoluteValue

private val StarColor = Color(0xFFFFB300)

fun LazyListScope.detailReviews(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
) {
    if (reviews.isEmpty()) return

    item(key = "reviews_header") {
        ReviewsHeader(modifier = modifier, reviews = reviews)
    }

    items(
        items = reviews,
        key = { it.id },
    ) { review ->
        ReviewCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            review = review,
        )
    }
}

// ─── Header ──────────────────────────────────────────────────────────────────

@Composable
private fun ReviewsHeader(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
) {
    val ratingsWithValues = reviews.mapNotNull { it.rating }
    val averageRating = if (ratingsWithValues.isNotEmpty()) ratingsWithValues.average() else null

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "reviews",
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (averageRating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier
                            .background(
                                color = StarColor.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "\u2605",
                            color = StarColor,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                        )
                        Text(
                            text = averageRating.format(1),
                            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StarColor,
                        )
                    }
                }
                Text(
                    text = "${reviews.size}",
                    style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = AppTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Review card ─────────────────────────────────────────────────────────────

@Composable
private fun ReviewCard(
    modifier: Modifier = Modifier,
    review: Review,
) {
    var expanded by remember { mutableStateOf(false) }
    val isLong = remember(review.content) { review.content.length > 220 }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surfaceContainerHigh,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 200)),
        ) {
            // Author row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuthorAvatar(
                    avatarPath = review.avatarPath,
                    authorName = review.author,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = review.author.ifBlank { review.authorUsername },
                            style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        if (review.rating != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            RatingBadge(rating = review.rating)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "@${review.authorUsername}",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                        Text(
                            text = "·",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = formatReviewDate(review.createdAt),
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = AppTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            )

            // Review content
            Text(
                text = review.content,
                style = AppTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = AppTheme.colorScheme.onSurface.copy(alpha = 0.87f),
                maxLines = if (expanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis,
            )

            if (isLong) {
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = if (expanded) "Show less" else "Read more",
                        style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AppTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

// ─── Avatar ───────────────────────────────────────────────────────────────────

private val AvatarPalette = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFFEC407A),
    Color(0xFF7E57C2), Color(0xFF42A5F5), Color(0xFFEF5350),
    Color(0xFF66BB6A), Color(0xFFFF7043), Color(0xFFAB47BC),
)

@Composable
private fun AuthorAvatar(
    avatarPath: String?,
    authorName: String,
) {
    val initial = authorName.firstOrNull()?.uppercase() ?: "?"
    val bgColor = remember(authorName) {
        AvatarPalette[authorName.hashCode().absoluteValue % AvatarPalette.size]
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        if (avatarPath != null) {
            val imageUrl = if (avatarPath.startsWith("/https://")) {
                avatarPath.removePrefix("/")
            } else {
                "$IMAGE_BASE_URL$avatarPath"
            }
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Text(
                text = initial,
                color = Color.White,
                style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

// ─── Rating badge ─────────────────────────────────────────────────────────────

@Composable
private fun RatingBadge(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .background(
                color = StarColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 6.dp, vertical = 3.dp),
    ) {
        Text(
            text = "\u2605",
            color = StarColor,
            fontSize = 10.sp,
            lineHeight = 14.sp,
        )
        Text(
            text = rating.toInt().toString(),
            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = StarColor,
        )
    }
}

// ─── Date formatting ──────────────────────────────────────────────────────────

private val MonthAbbreviations = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)

private fun formatReviewDate(isoDate: String): String {
    return try {
        val datePart = isoDate.substringBefore('T')
        val parts = datePart.split('-')
        if (parts.size < 3) return datePart
        val year = parts[0]
        val month = parts[1].toIntOrNull() ?: return datePart
        val day = parts[2].toIntOrNull() ?: return datePart
        val monthAbbr = MonthAbbreviations.getOrNull(month - 1) ?: return datePart
        "$monthAbbr $day, $year"
    } catch (_: Exception) {
        isoDate.substringBefore('T')
    }
}
