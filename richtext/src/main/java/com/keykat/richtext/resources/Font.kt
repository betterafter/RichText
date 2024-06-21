package com.keykat.richtext.resources

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.keykat.richtext.R


val Pretendard = FontFamily(
    fonts = listOf(
        Font(R.font.pretendard_light, weight = FontWeight.Light),
        Font(R.font.pretendard_medium, weight = FontWeight.Medium),
        Font(R.font.pretendard_black, weight = FontWeight.Black),
        Font(R.font.pretendard_bold, weight = FontWeight.Bold),
        Font(R.font.pretendard_extrabold, weight = FontWeight.ExtraBold),
        Font(R.font.pretendard_extralight, weight = FontWeight.ExtraLight),
        Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
        Font(R.font.pretendard_regular, weight = FontWeight.Normal),
        Font(R.font.pretendard_thin, weight = FontWeight.Thin)
    )
)

val MoveSans = FontFamily(
    fonts = listOf(
        Font(R.font.movesans_light),
        Font(R.font.movesans_medium),
        Font(R.font.movesans_bold),
    )
)

val Gimpo = FontFamily(
    fonts = listOf(
        Font(R.font.gimpo_1, weight = FontWeight.Light),
        Font(R.font.gimpo_2, weight = FontWeight.Normal)
    )
)
