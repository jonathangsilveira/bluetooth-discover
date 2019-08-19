package br.edu.jgsilveira.portfolio.bluetoothdiscover

sealed class DiscoveryState {

    object Started: DiscoveryState()

    data class Finished(val devices: List<Device>? = null): DiscoveryState()

}