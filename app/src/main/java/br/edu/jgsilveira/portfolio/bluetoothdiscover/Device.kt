package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothDevice

data class Device(
    var address: String,
    var name: String?,
    var state: DeviceState = DeviceState.UNBONDED
) {

    fun updateState(device: BluetoothDevice) {
        state = when (device.bondState) {
            BluetoothDevice.BOND_BONDING -> DeviceState.BONDING
            BluetoothDevice.BOND_BONDED -> DeviceState.BONDED
            else -> DeviceState.UNBONDED
        }
    }

    companion object {

        fun createFromBluetooth(device: BluetoothDevice) =
            Device(address = device.address, name = device.name).apply { updateState(device) }

    }

}