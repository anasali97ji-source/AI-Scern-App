package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.TextGray

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    Column(
        modifier = Modifier.fillMaxSize().background(SpaceBackground).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalPager(state = pagerState, modifier = Modifier.weight(3f)) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                when (page) {
                    0 -> {
                        Icon(Icons.Default.Camera, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(24.dp))
                        Text("Multi-Modal Detection", color = SoftWhite, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        Text("Text, image, audio, video. One forensic tool.", color = TextGray)
                    }
                    1 -> {
                        Text("85%", color = ElectricCyan, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Average detection accuracy", color = TextGray)
                    }
                    2 -> {
                       Text("Zero-Trust Privacy", color = SoftWhite, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                       Spacer(Modifier.height(8.dp))
                       Text("No account required. Your data stays local.", color = TextGray)
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                Box(modifier = Modifier
                    .padding(4.dp)
                    .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                    .background(if (pagerState.currentPage == index) ElectricCyan else TextGray, CircleShape)
                )
            }
        }
        
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
        ) {
            Text("Get Started", color = SpaceBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onFinish) {
            Text("Skip", color = TextGray)
        }
    }
}
