package br.edu.jgsilveira.portfolio.bluetoothdiscover

data class DiscoveryViewState(
    var inProgress: Boolean = false,
    var devices: List<Device>? = null
)