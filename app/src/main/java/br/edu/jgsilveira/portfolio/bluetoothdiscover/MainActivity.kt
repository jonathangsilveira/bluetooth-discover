package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BluetoothViewModel

    private var adapter: DeviceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(BluetoothViewModel::class.java)
        viewModel.discoveryState.observe(this, discoveryStateObserver())
        viewModel.deviceState.observe(this, Observer { adapter?.updateState(state = it) })
        discoverButton.setOnClickListener {
            viewModel.discover()
        }
        avaliableDevices.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        if (requiresPermissionsAtRuntime() && isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION))
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)
        else
            requestEnable()
    }

    private fun discoveryStateObserver(): Observer<DiscoveryViewState> =
        Observer {
            if (it != null) {
                progressContainer.visibility = if (it.inProgress) View.VISIBLE else View.GONE
                adapter = DeviceAdapter(it.devices ?: listOf()).apply {
                    setOnClickListener { address -> viewModel.pair(address = address) }
                }
                avaliableDevices.adapter = adapter
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED)
            finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            requestEnable()
    }

    private fun requestEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private fun requiresPermissionsAtRuntime() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @RequiresApi(Build.VERSION_CODES.M)
    fun FragmentActivity.isPermissionDenied(permission: String): Boolean =
        checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED

    companion object {

        private const val REQUEST_ENABLE_BT = 101

        private const val REQUEST_LOCATION_PERMISSION = 102

    }

}
