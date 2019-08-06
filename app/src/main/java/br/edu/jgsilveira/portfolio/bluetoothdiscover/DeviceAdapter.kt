package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter: RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private val devices: MutableList<BluetoothDevice> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    fun add(device: BluetoothDevice) {
        devices.add(device)
        notifyItemInserted(devices.size - 1)
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(device: BluetoothDevice) {
            itemView.findViewById<TextView>(android.R.id.text1)?.text = "${device.name} - ${device.address}"
        }

    }

}