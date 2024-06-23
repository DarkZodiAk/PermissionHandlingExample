package com.example.permission_and_intent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var hasReadPermission by mutableStateOf(false)
        private set
    var shouldShowReadSettingsRationale by mutableStateOf(false)
        private set

    private val channel = Channel<Boolean>()
    val showReadPermissionRationale = channel.receiveAsFlow()

    fun submitReadPermissionResult(hasRead: Boolean, shouldShowRead: Boolean) {
        hasReadPermission = hasRead
        shouldShowReadSettingsRationale = !shouldShowRead && !hasRead
        viewModelScope.launch {
            channel.send(shouldShowRead)
        }
    }
}