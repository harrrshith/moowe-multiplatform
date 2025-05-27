package com.harrrshith.moowe.ui.components.composeVectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val TrendingIcon: ImageVector
get() {
    if (trendingIcon != null) {
        return trendingIcon!!
    }
    trendingIcon = ImageVector.Builder(
        name = "Trending Icon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(
            fill = SolidColor(Color(0xFFE3E3E3)),
            fillAlpha = 1.0f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(136f, 720f)
            lineToRelative(-56f, -56f)
            lineToRelative(296f, -298f)
            lineToRelative(160f, 160f)
            lineToRelative(208f, -206f)
            horizontalLineTo(640f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(240f)
            verticalLineToRelative(240f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(-104f)
            lineTo(536f, 640f)
            lineTo(376f, 480f)
            lineTo(136f, 720f)
            close()
        }
    }.build()
    return trendingIcon!!
}

private var trendingIcon: ImageVector? = null