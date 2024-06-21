package com.keykat.richtext

import androidx.compose.material3.Text
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.keykat.richtext.attributes.TextAlign
import com.keykat.richtext.resources.Pretendard
import kotlin.math.max

/**한 줄의 텍스트에 여러 스타일을 적용하고 싶을 때 사용합니다.
 *
 *    CustomRichText(
 *       listOf(
 *          RichText(text = "h", style = CustomStyle()),
 *          RichText(text = "ello")
 *          RichText(text = " world!", endOfLine = true)
 *       )
 *    )
 *
 * @param texts 텍스트와 텍스트 스타일을 담아줍니다. []RichText] 클래스를 사용해주고 list로 묶어주세요.
 **/
@Deprecated("Text를 이용해서 RichText를 만드는 방식에서 Layout을 이용해서 만들겠습니다.")
@Composable
fun CustomRichText(
    texts: List<RichText>,
    defaultTextSize: Float = 14F,
    defaultTextColor: Color = Color.Black,
    defaultFontFamily: FontFamily = Pretendard,
    defaultFontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val align = when (textAlign) {
        TextAlign.Start -> androidx.compose.ui.text.style.TextAlign.Left
        TextAlign.End -> androidx.compose.ui.text.style.TextAlign.Right
        TextAlign.Center -> androidx.compose.ui.text.style.TextAlign.Center
    }

    return Text(
        buildAnnotatedString {
            texts.forEach {
                //
                val spanStyle = SpanStyle(
                    color = it.textStyle.fontColor ?: defaultTextColor,
                    fontWeight = it.textStyle.fontWeight ?: defaultFontWeight,
                    fontSize = it.textStyle.fontSize?.sp ?: defaultTextSize.sp,
                    fontFamily = it.textStyle.fontFamily ?: defaultFontFamily,
                    background = Color.Transparent,

                    )

                withStyle(style = spanStyle) {
                    append(it.text)
                }

                if (it.endOfLine) {
                    append("\n")
                }
            }
        },
        textAlign = align
    )
}


@Composable
fun CustomRichText(
    defaultTextSize: Float = 14F,
    defaultTextColor: Color = Color.Black,
    defaultFontFamily: FontFamily = Pretendard,
    defaultFontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    content: @Composable RichTextScope.() -> Unit
) {
    val scope = RichTextScope(
        defaultTextSize = defaultTextSize,
        defaultTextColor = defaultTextColor,
        defaultFontFamily = defaultFontFamily,
        defaultFontWeight = defaultFontWeight,
    )
    scope.content()
    val richContent = scope.richTextContent()

    return Layout(
        modifier = Modifier.wrapContentSize(),
        content = richContent,
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var xPosition = 0
        var yPosition = 0

        val placeableMap = mutableMapOf<Int, Pair<Int, Int>>()
        var lineStartIndex = 0

        // 각각 라인의 너비를 구하고 layoutWidth를 구하기 위한 변수
        var lineWidth = 0
        // wrap content를 계산하기 위해 너비를 구하는 변수
        var layoutWidth = 0
        // 높이를 구하는 변수
        var layoutHeight = 0

        // 일단 컨텐츠 너비부터 구해줘야 뭘 할 수 있다.
        placeables.forEachIndexed { index, placeable ->
            lineWidth += placeable.width

            if (scope.isEndOfLine(index)) {
                layoutWidth = max(lineWidth, layoutWidth)
                lineWidth = 0
            }
        }

        placeables.forEachIndexed { index, placeable ->
            layoutHeight += placeable.height

            // x좌표가 0이면 라인이 새로 시작되는 것
            if (xPosition == 0) {
                lineStartIndex = index
            }

            placeableMap[index] = Pair(xPosition, yPosition)
            // 라인이 끝나거나 마지막 라인일 경우
            if (scope.isEndOfLine(index) || index == placeables.lastIndex) {

                // 가운데 정렬
                if (textAlign == TextAlign.Center) {
                    val movePosition = layoutWidth / 2 - placeable.width / 2
                    for (i: Int in index..lineStartIndex) {
                        placeableMap[i]?.let {
                            placeableMap[i] = Pair(it.first + movePosition, it.second)
                        }
                    }
                }

                // 오른쪽 정렬
                else if (textAlign == TextAlign.End) {
                    val movePosition = layoutWidth - placeable.width
                    for (i: Int in index..lineStartIndex) {
                        placeableMap[i]?.let {
                            placeableMap[i] = Pair(it.first + movePosition, it.second)
                        }
                    }
                }

                // 좌측 정렬은 아무 작업 안하면 알아서 잘 된다.

                xPosition = 0
                yPosition += placeable.height
            } else {
                xPosition += placeable.width
            }
        }

        layout(layoutWidth, layoutHeight) {

            placeables.forEachIndexed { index, placeable ->
                val pair = placeableMap[index]
                println("[keykat pair x: ${pair?.first}")
                pair?.let {
                    placeable.placeRelative(x = pair.first, y = pair.second)
                }
            }
        }
    }
}