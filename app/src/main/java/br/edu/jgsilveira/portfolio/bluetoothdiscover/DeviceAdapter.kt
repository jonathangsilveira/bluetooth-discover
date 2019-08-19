package br.edu.jgsilveira.portfolio.bluetoothdiscover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.discovery_item.view.*

class DeviceAdapter(private val devices: List<Device>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private var onClick: (address: String) -> Unit = {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.discovery_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) = holder.bind(devices[position])

    fun setOnClickListener(onClick: (address: String) -> Unit) {
        this.onClick = onClick
    }

    fun updateState(state: DeviceState) {
        val device = devices.find { it.address == state.address }
        if (device != null) {
            val index = devices.indexOf(device)
            device.state = state.state
            notifyItemChanged(index)
        }
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(device: Device) {
            itemView.setOnClickListener {
                onClick(device.address)
            }
            itemView.device_name.visibility = if (device.name.isNullOrEmpty())
                View.GONE
            else {
                itemView.device_name.text = device.name
                View.VISIBLE
            }
            itemView.device_address.text = device.address
            val icon = when (device.state) {
                BondState.BONDED -> R.drawable.ic_bluetooth_connected_24dp
                BondState.BONDING -> R.drawable.ic_bluetooth_24dp
                BondState.NONE -> R.drawable.ic_bluetooth_disabled_24dp
            }
            itemView.state.setImageResource(icon)
        }

    }

}