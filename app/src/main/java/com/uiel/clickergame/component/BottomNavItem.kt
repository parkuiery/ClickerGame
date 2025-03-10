package com.uiel.clickergame.component

import com.uiel.clickergame.R

sealed class BottomNavItem(
    val route: String,
    val title: Int,
    val icon: Int,
) {
    data object Home : BottomNavItem("home", R.string.home, R.drawable.baseline_home_24)
    data object Ranking : BottomNavItem("ranking", R.string.ranking, R.drawable.baseline_bar_chart_24)
}