package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Repository(private val application: Application) : BluetoothReceiver.Callback {

    private val avaliableDevices = mutableMapOf<String, Device>()

    private val filter = IntentFilter().apply {
        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        addAction(BluetoothDevice.ACTION_FOUND)
    }

    private val receiver = BluetoothReceiver(this)

    private val state = MutableLiveData<BluetoothScanningState>(BluetoothScanningState.Initial)

    val discoveryState: LiveData<BluetoothScanningState>
        get() = state

    override fun onDiscoverStarted() {
        state.value = BluetoothScanningState.InProgress
    }

    override fun onDiscoverFinished() {
        state.value = BluetoothScanningState.Concluded(avaliableDevices.values.toList())
        application.unregisterReceiver(receiver)
    }

    override fun onDeviceFound(device: BluetoothDevice) {
        avaliableDevices[device.address] = Device(device.address, device.name)
    }

    fun start() {
        avaliableDevices.clear()
        application.registerReceiver(receiver, filter)
        BluetoothAdapter.getDefaultAdapter()?.startDiscovery()
    }

    fun cancel() {
        BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
    }

    fun bondedDevices() = BluetoothAdapter.getDefaultAdapter()?.bondedDevices

    fun isBluetoothEnabled(): Boolean = BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false

}