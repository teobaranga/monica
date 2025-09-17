package com.teobaranga.monica.core.ui.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("MagicNumber")
val MonicaLogo: ImageVector
    get() {
        if (_MonicaLogo != null) {
            return _MonicaLogo!!
        }
        _MonicaLogo = ImageVector.Builder(
            name = "IconName",
            defaultWidth = 128.dp,
            defaultHeight = 128.dp,
            viewportWidth = 500f,
            viewportHeight = 500f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    lineTo(500f, 0f)
                    lineTo(500f, 500f)
                    lineTo(0f, 500f)
                    lineTo(0f, 0f)
                    close()
                }
            ) {
                path(
                    fill = SolidColor(Color(0xFF2C2B29)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(253.94f, 471f)
                    curveTo(368.15f, 471f, 494.88f, 397.54f, 478.86f, 256.26f)
                    curveTo(462.83f, 114.98f, 368.15f, 41.52f, 253.94f, 41.52f)
                    curveTo(139.72f, 41.52f, 45.78f, 105.91f, 21.28f, 256.26f)
                    curveTo(-3.22f, 406.6f, 139.72f, 471f, 253.94f, 471f)
                    close()
                }
                path(
                    fill = SolidColor(Color.White),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(252.41f, 429.68f)
                    curveTo(344.03f, 429.68f, 445.69f, 370.79f, 432.83f, 257.52f)
                    curveTo(419.98f, 144.24f, 344.03f, 85.35f, 252.41f, 85.35f)
                    curveTo(160.79f, 85.35f, 85.44f, 136.98f, 65.78f, 257.52f)
                    curveTo(46.13f, 378.06f, 160.79f, 429.68f, 252.41f, 429.68f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF2C2B29)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(71.59f, 194.28f)
                    curveTo(84f, 194.32f, 89.39f, 149.9f, 111.57f, 129.68f)
                    curveTo(131.92f, 111.11f, 178.68f, 104.74f, 178.68f, 83.28f)
                    curveTo(178.68f, 38.43f, 130.06f, 29f, 87.12f, 29f)
                    curveTo(44.19f, 29f, 6f, 74.49f, 6f, 119.34f)
                    curveTo(6f, 164.19f, 47.77f, 194.2f, 71.59f, 194.28f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF2C2B29)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(428.41f, 194.28f)
                    curveTo(416f, 194.32f, 410.61f, 149.9f, 388.43f, 129.68f)
                    curveTo(368.08f, 111.11f, 321.32f, 104.74f, 321.32f, 83.28f)
                    curveTo(321.32f, 38.43f, 369.94f, 29f, 412.88f, 29f)
                    curveTo(455.81f, 29f, 494f, 74.49f, 494f, 119.34f)
                    curveTo(494f, 164.19f, 452.23f, 194.2f, 428.41f, 194.28f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF2B2A28)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(350.92f, 345.22f)
                    curveTo(383.41f, 337.47f, 396.96f, 323.81f, 396.96f, 279.65f)
                    curveTo(396.96f, 235.49f, 376.26f, 190.65f, 340.62f, 190.65f)
                    curveTo(304.99f, 190.65f, 281.32f, 222.54f, 281.32f, 266.7f)
                    curveTo(281.32f, 310.86f, 318.43f, 352.97f, 350.92f, 345.22f)
                    close()
                }
                path(
                    fill = SolidColor(Color.White),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(339.02f, 289.2f)
                    curveTo(347.86f, 289.2f, 350.49f, 286.94f, 354.49f, 281.87f)
                    curveTo(358.97f, 276.19f, 362.31f, 269.6f, 361.22f, 255.98f)
                    curveTo(359.39f, 233.24f, 349.8f, 222.77f, 331.05f, 222.77f)
                    curveTo(312.29f, 222.77f, 308.63f, 237.19f, 308.63f, 255.98f)
                    curveTo(308.63f, 274.78f, 320.26f, 289.2f, 339.02f, 289.2f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF2B2A28)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(150.33f, 345.22f)
                    curveTo(117.84f, 337.47f, 104.29f, 323.81f, 104.29f, 279.65f)
                    curveTo(104.29f, 235.49f, 124.99f, 190.65f, 160.63f, 190.65f)
                    curveTo(196.26f, 190.65f, 219.93f, 222.54f, 219.93f, 266.7f)
                    curveTo(219.93f, 310.86f, 182.82f, 352.97f, 150.33f, 345.22f)
                    close()
                }
                path(
                    fill = SolidColor(Color.White),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(162.23f, 289.2f)
                    curveTo(153.39f, 289.2f, 150.76f, 286.94f, 146.76f, 281.87f)
                    curveTo(142.28f, 276.19f, 138.94f, 269.6f, 140.03f, 255.98f)
                    curveTo(141.86f, 233.24f, 151.45f, 222.77f, 170.2f, 222.77f)
                    curveTo(188.96f, 222.77f, 192.62f, 237.19f, 192.62f, 255.98f)
                    curveTo(192.62f, 274.78f, 180.99f, 289.2f, 162.23f, 289.2f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF2C2B29)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(251.51f, 374.58f)
                    curveTo(269.17f, 374.58f, 286.29f, 348.6f, 286.29f, 337.45f)
                    curveTo(286.29f, 326.3f, 268.91f, 324.5f, 251.26f, 324.5f)
                    curveTo(233.6f, 324.5f, 216.22f, 326.3f, 216.22f, 337.45f)
                    curveTo(216.22f, 348.6f, 233.85f, 374.58f, 251.51f, 374.58f)
                    close()
                }
            }
        }.build()

        return _MonicaLogo!!
    }

@Suppress("ObjectPropertyName")
private var _MonicaLogo: ImageVector? = null

@Preview
@Composable
private fun PreviewMonicaLogo() {
    Image(
        imageVector = MonicaLogo,
        contentDescription = null,
    )
}
