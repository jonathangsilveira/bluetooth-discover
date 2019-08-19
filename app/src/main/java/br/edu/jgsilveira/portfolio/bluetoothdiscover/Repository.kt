package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Repository(private val application: Application) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> onDeviceFound(intent)
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> onDeviceStateChanged(intent)
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> onDiscoveryStarted(intent)
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> onDiscoveryFinished(intent)
                BluetoothAdapter.ACTION_STATE_CHANGED -> onConnectionStateChanged(intent)
            }
        }
    }

    private val avaliableDevices = mutableMapOf<String, Device>()

    private val filter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
    }

    private val connectionState: MutableLiveData<ConnectionState> by lazy {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val state = if (adapter.enable())
            ConnectionState.ON
        else
            ConnectionState.OFF
        MutableLiveData(state)
    }

    private fun onDeviceFound(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        if (avaliableDevices.containsKey(device.address))
            avaliableDevices[device.address]?.updateState(device)
        else
            avaliableDevices[device.address] = Device.createFromBluetooth(device)
    }

    private fun onDeviceStateChanged(intent: Intent) {

    }

    private fun onDiscoveryStarted(intent: Intent) {

    }

    private fun onDiscoveryFinished(intent: Intent) {

    }

    private fun onConnectionStateChanged(intent: Intent) {

    }

    fun start() {
        application.registerReceiver(receiver, filter)
    }

    fun finish() {
        application.unregisterReceiver(receiver)
    }

    fun connectOrDisconnect(turnOn: Boolean): LiveData<ConnectionState> {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (turnOn && adapter.isEnabled)
            connectionState
        if (turnOn)
            adapter.enable()
        else
            adapter.enable()
        return connectionState
    }

}