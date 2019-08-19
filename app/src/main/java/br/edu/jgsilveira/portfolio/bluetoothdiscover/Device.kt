package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothDevice

data class Device(
    var address: String,
    var name: String?,
    var state: BondState = BondState.NONE
) {

    fun updateState(device: BluetoothDevice) {
        state = when (device.bondState) {
            BluetoothDevice.BOND_BONDING -> BondState.BONDING
            BluetoothDevice.BOND_BONDED -> BondState.BONDED
            else -> BondState.NONE
        }
    }

    companion object {

        fun createFromBluetooth(device: BluetoothDevice) =
            Device(address = device.address, name = device.name).apply { updateState(device) }

    }

}

data class DeviceState(val address: String, val state: BondState)

enum class BondState { BONDED, BONDING, NONE }