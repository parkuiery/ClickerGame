package com.uiel.clickergame.component

import com.uiel.clickergame.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    data object Home : BottomNavItem("home", "홈", R.drawable.baseline_home_24)
    data object Ranking : BottomNavItem("ranking", "랭킹", R.drawable.baseline_bar_chart_24)
}