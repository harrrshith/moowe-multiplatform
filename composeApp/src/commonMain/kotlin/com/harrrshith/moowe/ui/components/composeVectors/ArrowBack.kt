package com.harrrshith.moowe.ui.components.composeVectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ArrowBackIcon: ImageVector
    get() {
        if (arrowBackIcon != null) {
            return arrowBackIcon!!
        }
        arrowBackIcon = Builder(name = "Arrow back icon", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveToRelative(313.0f, 520.0f)
                lineToRelative(224.0f, 224.0f)
                lineToRelative(-57.0f, 56.0f)
                lineToRelative(-320.0f, -320.0f)
                lineToRelative(320.0f, -320.0f)
                lineToRelative(57.0f, 56.0f)
                lineToRelative(-224.0f, 224.0f)
                horizontalLineToRelative(487.0f)
                verticalLineToRelative(80.0f)
                lineTo(313.0f, 520.0f)
                close()
            }
        }
            .build()
        return arrowBackIcon!!
    }

private var arrowBackIcon: ImageVector? = null