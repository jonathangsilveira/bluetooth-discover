package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repository(application)

    private val state = DiscoveryViewState()

    val discoveryViewState = Transformations.map(repo.discoveryState) {
        when (it) {
            is BluetoothScanningState.Initial -> state
            is BluetoothScanningState.InProgress -> state.apply { inProgress = true }
            is BluetoothScanningState.Concluded -> state.apply {
                inProgress = false
                devices = it.devices
            }
        }
    }

    override fun onCleared() {
        repo.cancel()
        super.onCleared()
    }

    fun discover() = repo.start()

    fun cancel() = repo.cancel()

}