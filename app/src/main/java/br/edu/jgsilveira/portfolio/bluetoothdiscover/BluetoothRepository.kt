package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.companion.BluetoothDeviceFilter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BluetoothRepository(private val application: Application) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> onDeviceFound(intent)
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> onDeviceStateChanged(intent)
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> onDiscoveryStarted()
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> onDiscoveryFinished()
                BluetoothAdapter.ACTION_STATE_CHANGED -> onConnectionStateChanged(intent)
            }
        }
    }

    private val filter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
    }

    private val avaliableDevices = mutableMapOf<String, Device>()

    private var address: String? = null

    private val connectionState: MutableLiveData<ConnectionState> by lazy {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val state = if (adapter.enable())
            ConnectionState.ON
        else
            ConnectionState.OFF
        MutableLiveData(state)
    }

    private val deviceState: MutableLiveData<DeviceState> = MutableLiveData()

    private val discoveryState: MutableLiveData<DiscoveryState> = MutableLiveData(DiscoveryState.Finished())

    private fun onDeviceFound(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        if (avaliableDevices.containsKey(device.address))
            avaliableDevices[device.address]?.updateState(device)
        else
            avaliableDevices[device.address] = Device.createFromBluetooth(device)
    }

    private fun onDeviceStateChanged(intent: Intent) {
        val state = when (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)) {
            BluetoothDevice.BOND_BONDED -> BondState.BONDED
            BluetoothDevice.BOND_BONDING -> BondState.BONDING
            else -> BondState.NONE
        }
        deviceState.value = DeviceState(address = address!!, state = state)
    }

    private fun onDiscoveryStarted() {
        discoveryState.value = DiscoveryState.Started
    }

    private fun onDiscoveryFinished() {
        discoveryState.value = DiscoveryState.Finished(avaliableDevices.values.toList())
    }

    private fun onConnectionStateChanged(intent: Intent) {
        connectionState.value = when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)) {
            BluetoothAdapter.STATE_ON -> ConnectionState.ON
            BluetoothAdapter.STATE_TURNING_ON -> ConnectionState.TURNING_ON
            BluetoothAdapter.STATE_TURNING_OFF -> ConnectionState.TURNING_OFF
            else -> ConnectionState.OFF
        }
    }

    fun start() {
        application.registerReceiver(receiver, filter)
    }

    fun finish() {
        application.unregisterReceiver(receiver)
    }

    fun connectOrDisconnect(turnOn: Boolean): LiveData<ConnectionState> {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        when {
            turnOn && adapter.isEnabled -> {}
            turnOn -> adapter.enable()
            else -> adapter.disable()
        }
        return connectionState
    }

    fun pair(address: String): LiveData<DeviceState> {
        this.address = address
        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter.getRemoteDevice(address)?.createBond()
        return deviceState
    }

    fun discovery(): LiveData<DiscoveryState> {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter.startDiscovery()
        return discoveryState
    }

}