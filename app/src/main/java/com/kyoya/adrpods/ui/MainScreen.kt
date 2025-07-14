package com.kyoya.adrpods.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyoya.adrpods.ble.ConnectionState

@Composable
fun MainScreen(viewModel: AirPodsViewModel) {

    val connectionState by viewModel.connectionState.collectAsState()
    val batteryLevelLeft by viewModel.batteryLevelLeft.collectAsState()
    val batteryLevelRight by viewModel.batteryLevelRight.collectAsState()
    val batteryLevelCase by viewModel.batteryLevelCase.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (connectionState) {
            is ConnectionState.Connected -> {
                Text("Status: Connected")
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text("Left: ${batteryLevelLeft ?: "N/A"}%")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Right: ${batteryLevelRight ?: "N/A"}%")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Case: ${batteryLevelCase ?: "N/A"}%")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // TODO: Add noise control buttons here.
                Button(onClick = { viewModel.disconnect() }) {
                    Text("Disconnect")
                }
            }
            is ConnectionState.Connecting -> {
                Text("Status: Connecting...")
            }
            is ConnectionState.Disconnected -> {
                Text("Status: Disconnected")
                // TODO: Add a button to start scanning and connect.
            }
            is ConnectionState.Error -> {
                Text("Status: Error")
                Text((connectionState as ConnectionState.Error).message)
            }
        }
    }
}
