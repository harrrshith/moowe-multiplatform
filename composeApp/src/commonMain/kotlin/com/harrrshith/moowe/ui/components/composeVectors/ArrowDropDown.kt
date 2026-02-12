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

public val ArrowDropDownIcon: ImageVector
    get() {
        if (arrowDropDownIcon != null) {
            return arrowDropDownIcon!!
        }
        arrowDropDownIcon = Builder(name = "Arrow drop down icon", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(480.0f, 560.0f)
                lineTo(280.0f, 360.0f)
                horizontalLineToRelative(400.0f)
                lineTo(480.0f, 560.0f)
                close()
            }
        }
            .build()
        return arrowDropDownIcon!!
    }

private var arrowDropDownIcon: ImageVector? = null
