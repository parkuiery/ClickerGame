package com.uiel.clickergame

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uiel.clickergame.component.RankingItem
import com.uiel.clickergame.component.RankingMyItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RankingScreen(modifier: Modifier = Modifier) {
    val rankingViewModel: RankingViewModel = viewModel()
    val uiState by rankingViewModel.uiState.collectAsState()
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }

    LaunchedEffect(Unit) {
        rankingViewModel.fetchUserData()
    }

    LaunchedEffect(Unit) {
        rankingViewModel.startSSEConnection()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val firstScore = NumberFormat.getNumberInstance(Locale.US).format(uiState.rankingList.firstOrNull()?.score ?: 0)
            Image(
                modifier = Modifier
                    .width(60.dp),
                painter = painterResource(id = R.drawable.crown),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        BorderStroke(4.dp, rainbowColorsBrush),
                        CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.red_eye_cat),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Text(text = uiState.rankingList.firstOrNull()?.nickname ?: "")
            Text(text = firstScore)
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(uiState.rankingList) { indexed , item->
                RankingItem(
                    ranking = indexed + 1,
                    nickName = item.nickname,
                    score = NumberFormat.getNumberInstance(Locale.US).format(item.score),
                )
            }
        }
        val myRank = if(uiState.myRank == null) "-" else uiState.myRank.toString()
        RankingMyItem(
            modifier = Modifier,
            ranking = myRank,
            nickName = uiState.myNickname,
            score = uiState.myScore,
        )
    }
}