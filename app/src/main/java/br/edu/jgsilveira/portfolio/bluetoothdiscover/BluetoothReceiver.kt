package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothReceiver(private val callback: Callback): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> callback.onDiscoverStarted()
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> callback.onDiscoverFinished()
            BluetoothDevice.ACTION_FOUND -> callback.onDeviceFound(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
        }
    }

    interface Callback {

        fun onDiscoverStarted()

        fun onDiscoverFinished()

        fun onDeviceFound(device: BluetoothDevice)

    }

}