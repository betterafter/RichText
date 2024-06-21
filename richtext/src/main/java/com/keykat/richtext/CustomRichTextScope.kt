package com.keykat.richtext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keykat.richtext.attributes.TextDecoration
import com.keykat.richtext.attributes.TextStyle
import com.keykat.richtext.attributes.sp
import com.keykat.richtext.resources.Pretendard

/// 2024.06.21 이거 object로 만드니까 rebuild할 때 items가 초기화가 안되어서
/// rebuild할 때마다 위젯이 복사되는 문제가 있음 (items에 아이템을 계속 넣으니까)
class RichTextScope(
    val defaultTextSize: Float = 14F,
    val defaultTextColor: Color = Color.Black,
    val defaultFontFamily: FontFamily = Pretendard,
    val defaultFontWeight: FontWeight = FontWeight.Normal,
) {
    private val items = mutableListOf<RichTextInstance>()

    @Composable
    fun RichText(
        text: String,
        textStyle: TextStyle = TextStyle(
            fontSize = defaultTextSize,
            fontColor = defaultTextColor,
            fontFamily = defaultFontFamily,
            fontWeight = defaultFontWeight,
        ),
        endOfLine: Boolean = false,
        lineHeight: Dp = 0.dp,
        decoration: TextDecoration = TextDecoration()
    ) {
        val richText = RichTextInstance(
            text = text,
            textStyle = textStyle,
            endOfLine = endOfLine,
            lineHeight = lineHeight,
            decoration = decoration
        )

        items.add(richText)
    }

    private class RichTextInstance(
        val text: String = "",
        val textStyle: TextStyle = TextStyle(),
        val endOfLine: Boolean = false,
        val lineHeight: Dp = 0.dp,
        val decoration: TextDecoration = TextDecoration()
    )


    fun richTextContent(): @Composable () -> Unit {
        return {
            items.forEach {
                val text = it.text + if (it.endOfLine) "\n" else ""

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        // clip 다음에 background를 선언해줘야 색상이 선언된 후 clip이 적용됨
                        .clip(
                            RoundedCornerShape(
                                it.decoration.borderRadius.topLeft,
                                it.decoration.borderRadius.topRight,
                                it.decoration.borderRadius.bottomLeft,
                                it.decoration.borderRadius.bottomRight
                            )
                        )
                        .background(color = it.decoration.background)
                        .padding(
                            it.decoration.padding.left.dp,
                            it.decoration.padding.top.dp,
                            it.decoration.padding.right.dp,
                            it.decoration.padding.bottom.dp,
                        )
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = text,
                        style = it.textStyle.style,
                        lineHeight = it.lineHeight.sp(),
                    )
                }
            }
        }
    }

    fun isEndOfLine(index: Int): Boolean {
        val item = items[index]
        return item.endOfLine
    }
}