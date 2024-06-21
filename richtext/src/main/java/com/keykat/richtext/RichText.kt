package com.keykat.richtext

import com.keykat.richtext.attributes.TextStyle

/** CustomRichText에 담을 자식 클래스입니다.
 * @param text 단순히 텍스트를 담아주세요.
 * @param textStyle 스타일을 넣어주세요. []CustomTextStyle] 클래스를 사용해주세요.
 * @param endOfLine 다음 줄로 줄바꿈하고 싶을 때 true를 해주세요.
 */
@Deprecated("CustomRichText v1용으로 사용하던 클래스.")
open class RichText(
    val text: String = "",
    val textStyle: TextStyle = TextStyle(),
    val endOfLine: Boolean = false,
)
