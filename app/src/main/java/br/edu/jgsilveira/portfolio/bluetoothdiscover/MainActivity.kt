package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BluetoothViewModel

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(BluetoothViewModel::class.java)
        viewModel.discoveryViewState.observe(this, discoveryStateObserver())
        discoverButton.setOnClickListener {
            viewModel.discover()
        }
        /*if (isBtDisabled())
            requestEnable()
        else {
            val filter = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
            registerReceiver(receiver, filter)
            discover()
        }*/
    }

    private fun discoveryStateObserver(): Observer<DiscoveryViewState> =
        Observer {
            if (it != null) {
                progressContainer.visibility = if (it.inProgress) View.VISIBLE else View.GONE
                foundDevices.adapter = DeviceAdapter(it.devices ?: listOf())
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
