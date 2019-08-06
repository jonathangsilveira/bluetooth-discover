package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothDevice

sealed class BluetoothScanningState {

    object InProgress: BluetoothScanningState()

    data class Concluded(val devices: List<BluetoothDevice>): BluetoothScanningState()

}