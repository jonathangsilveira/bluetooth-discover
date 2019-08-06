package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Repository(private val application: Application): BluetoothReceiver.Callback {

    private val avaliableDevices = mutableListOf<BluetoothDevice>()

    private val filter = IntentFilter().apply {
        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        addAction(BluetoothDevice.ACTION_FOUND)
    }

    private val receiver = BluetoothReceiver(this)

    override fun onDiscoverStarted() {
    }

    override fun onDiscoverFinished() {
        application.unregisterReceiver(receiver)
    }

    override fun onDeviceFound(device: BluetoothDevice) {

    }

    fun start() {
        application.registerReceiver(receiver, filter)
        BluetoothAdapter.getDefaultAdapter()?.startDiscovery()
    }

    fun cancel() {
        BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
    }

}