package com.uiel.clickergame.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun InlineColoredText(
    modifier: Modifier = Modifier,
    text : String, setTextSize : Float = 64f ,
    setStrokeWidth : Float = 12f,
    setStrokeMiter : Float = 10f,
    setTextColor : Int = android.graphics.Color.BLACK,
    setBackgroundTextColor : Int = android.graphics.Color.WHITE){

    val textPaintStroke = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.STROKE
        textSize = setTextSize //64f
        color = setTextColor
        strokeWidth = setStrokeWidth
        strokeMiter = setStrokeMiter
        strokeJoin = android.graphics.Paint.Join.ROUND
    }

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.FILL
        textSize = setTextSize // 64f
        color = setBackgroundTextColor
    }

    Canvas(
        modifier = modifier,
        onDraw = {
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    text,
                    0f,
                    120.dp.toPx(),
                    textPaintStroke
                )
                it.nativeCanvas.drawText(
                    text,
                    0f,
                    120.dp.toPx(),
                    textPaint
                )
            }
        }
    )
}
