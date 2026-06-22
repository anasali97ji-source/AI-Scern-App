package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TopBrandHeader
import com.example.ui.components.CyberCard
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: ScanViewModel,
    modifier: Modifier = Modifier
) {
    var darkModeEnabled by remember { mutableStateOf(true) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBackground)
            .padding(16.dp)
    ) {
        TopBrandHeader()

        Spacer(modifier = Modifier.height(20.dp))

        CyberCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("APPEARANCE", color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (darkModeEnabled) Icons.Default.DarkMode else Icons.Default.LightMode, contentDescription = null, tint = SoftWhite)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Dark Mode", color = SoftWhite, fontSize = 14.sp)
                    }
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SpaceBackground,
                            checkedTrackColor = ElectricCyan,
                            uncheckedThumbColor = TextGray,
                            uncheckedTrackColor = SpaceBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("DATA MANAGEMENT", color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, tint = AlertRed)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Clear All Local History", color = SoftWhite, fontSize = 14.sp)
                    }
                    Button(
                        onClick = { showClearConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CLEAR", color = AlertRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ABOUT AISCERN", color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingRowItem(icon = Icons.Default.Info, title = "Version", value = "1.0.4 (Build 8842)")
                Spacer(modifier = Modifier.height(16.dp))
                SettingRowItem(icon = Icons.Default.StarRate, title = "Rate App", value = "")
                Spacer(modifier = Modifier.height(16.dp))
                SettingRowItem(icon = Icons.Default.Policy, title = "Privacy Policy", value = "")
            }
        }

        if (showClearConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showClearConfirmDialog = false },
                title = { Text("Clear History?", color = AlertRed) },
                text = { Text("This will permanently delete all local forensic records.", color = SoftWhite) },
                containerColor = SpaceBackground,
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearAllHistory()
                            showClearConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                    ) {
                        Text("DELETE", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearConfirmDialog = false }) {
                        Text("CANCEL", color = TextGray)
                    }
                }
            )
        }
    }
}

@Composable
fun SettingRowItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = SoftWhite)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, color = SoftWhite, fontSize = 14.sp)
        }
        if (value.isNotEmpty()) {
            Text(value, color = TextGray, fontSize = 12.sp)
        } else {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
        }
    }
}
