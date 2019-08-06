package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothDevice

data class DiscoveryViewState(
    var inProgress: Boolean = false,
    var devices: MutableList<BluetoothDevice> = mutableListOf()
)