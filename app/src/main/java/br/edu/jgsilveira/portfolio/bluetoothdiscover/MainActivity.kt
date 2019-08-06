package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var deviceAdapter: DeviceAdapter? = null

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    foundDevice(intent)
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    progressContainer.visibility = View.VISIBLE
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    progressContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun foundDevice(intent: Intent) {
        // Discovery has found a device. Get the BluetoothDevice
        // object and its info from the Intent.
        val device: BluetoothDevice =
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Log.d("dente azul", device.name)
        deviceAdapter?.add(device)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceAdapter = DeviceAdapter()
        foundDevices.adapter = deviceAdapter
        if (isBtDisabled())
            requestEnable()
        else {
            val filter = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
            registerReceiver(receiver, filter)
            discover()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
            discover()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun isBtDisabled() = BluetoothAdapter.getDefaultAdapter()?.isEnabled == false

    private fun requestEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private fun discover() {
        BluetoothAdapter.getDefaultAdapter()?.startDiscovery()
    }

    private fun cancel() {
        BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
    }

    companion object {

        private const val REQUEST_ENABLE_BT = 101

    }

}
