package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BluetoothRepository(application)

    private val state = DiscoveryViewState()

    private val device: MutableLiveData<String> = MutableLiveData()

    private val connectionOnOff: MutableLiveData<Boolean> = MutableLiveData()

    private val discoverTrigger = MutableLiveData(true)

    val deviceState = Transformations.switchMap(device) { repo.pair(address = it) }

    val connectionState = Transformations.switchMap(connectionOnOff) {
        repo.connectOrDisconnect(turnOn = it)
    }

    val discoveryState = Transformations.switchMap(discoverTrigger) {
        Transformations.map(repo.discovery()) { state ->
            when (state) {
                is DiscoveryState.Started -> this.state.apply { inProgress = true }
                is DiscoveryState.Finished -> this.state.apply {
                    inProgress = false
                    devices = state.devices
                }
            }
        }
    }

    init {
        repo.start()
    }

    override fun onCleared() {
        repo.finish()
        super.onCleared()
    }

    fun onConnectionStateChanged(turnOn: Boolean) {
        connectionOnOff.value = turnOn
    }

    fun pair(address: String) {
        device.value = address
    }

    fun discover() {
        discoverTrigger.value = true
    }

}