package com.uiel.clickergame.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun CkBottomNavigation(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem,
    navController: NavController,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Ranking,
    )

    NavigationBar(
        modifier = Modifier,
        //containerColor = Color.White,
    ) {
        Column {
            Row {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item,
                        onClick = {
                            onItemSelected(item)
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                )
                                Text(
                                    text = stringResource(id = item.title),
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}