package com.kyoya.adrpods.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.kyoya.adrpods.ui.theme.ADRPodsTheme

class MainActivity : ComponentActivity() {

    // TODO: Replace with a proper ViewModel injection mechanism.
    private val viewModel: AirPodsViewModel by lazy {
        // This is a temporary solution. In a real app, you would use a dependency injection framework like Hilt or Koin.
        AirPodsViewModelFactory(this).create(AirPodsViewModel::class.java)
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            // All permissions granted.
            // TODO: Start scanning for devices.
        } else {
            // Not all permissions granted.
            // TODO: Show a rationale to the user.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        setContent {
            ADRPodsTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
