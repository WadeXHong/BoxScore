package com.wadexhong.boxscore.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wadexhong.boxscore.R

@Composable
fun DashboardScreen(
    onStartGameClick: () -> Unit,
    onTeamManageClick: () -> Unit,
    onGameHistoryClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_main),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val shape = RoundedCornerShape(25.dp)
            val btnColor = colorResource(id = R.color.colorButtonInMain)
            val textColor = colorResource(id = R.color.colorTextInMainButton)

            DashboardButton("START GAME", shape, btnColor, textColor, onStartGameClick)
            Spacer(modifier = Modifier.height(16.dp))
            DashboardButton("TEAM MANAGE", shape, btnColor, textColor, onTeamManageClick)
            Spacer(modifier = Modifier.height(16.dp))
            DashboardButton("GAME HISTORY", shape, btnColor, textColor, onGameHistoryClick)
            Spacer(modifier = Modifier.height(16.dp))
            DashboardButton("SETTING", shape, btnColor, textColor, onSettingClick)
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    shape: RoundedCornerShape,
    btnColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
    ) {
        Text(text, color = textColor)
    }
}
