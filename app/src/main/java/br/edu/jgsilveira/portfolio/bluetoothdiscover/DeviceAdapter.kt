package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(private val devices: List<Device>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) = holder.bind(devices[position])

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(device: Device) {
            (itemView as TextView).text = "${device.name} - ${device.address}"
        }

    }

}