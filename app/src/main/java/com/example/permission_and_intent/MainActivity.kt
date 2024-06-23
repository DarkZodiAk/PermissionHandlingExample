package com.example.permission_and_intent

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permission_and_intent.ui.theme.Permission_and_intentTheme

class MainActivity : ComponentActivity() {
    private var originRequestOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Permission_and_intentTheme {
                val viewModel = viewModel<MainViewModel>()
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                    ) { hasReadPermission ->
                        restoreOrientation()
                        viewModel.submitReadPermissionResult(
                            hasRead = hasReadPermission,
                            shouldShowRead = shouldShowRequestReadRationale()
                        )
                    }

                    val settingsLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) {
                        viewModel.submitReadPermissionResult(
                            hasRead = hasReadPermission(),
                            shouldShowRead = shouldShowRequestReadRationale()
                        )
                    }

                    LaunchedEffect(key1 = true) {
                        lockOrientation()
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        viewModel.showReadPermissionRationale.collect {
                            if(it) {
                                lockOrientation()
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    }

                    if(viewModel.shouldShowReadSettingsRationale) {
                        Text(text = "You permanently declined permission, open app settings to grant it")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            settingsLauncher.launch(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                            )
                        }) {
                            Text(text = "Open settings")
                        }
                    }

                    if(viewModel.hasReadPermission) {
                        Text(text = "Permission is granted, well done! :)")
                    }
                }
            }
        }
    }

    private fun lockOrientation() {
        originRequestOrientation = requestedOrientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    private fun restoreOrientation() {
        requestedOrientation = originRequestOrientation
    }
}

fun ComponentActivity.shouldShowRequestReadRationale() : Boolean {
    return shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
}

fun Context.hasReadPermission() : Boolean {
    return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}