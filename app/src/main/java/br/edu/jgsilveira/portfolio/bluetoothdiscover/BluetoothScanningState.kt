package br.edu.jgsilveira.portfolio.bluetoothdiscover

sealed class BluetoothScanningState {

    object Initial: BluetoothScanningState()

    object InProgress: BluetoothScanningState()

    data class Concluded(val devices: List<Device>): BluetoothScanningState()

}