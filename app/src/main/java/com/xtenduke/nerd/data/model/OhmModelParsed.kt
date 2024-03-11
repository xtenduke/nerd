package com.xtenduke.nerd.data.model

import android.util.ArrayMap

data class OhmModelParsed(
    var cpu: Cpu? = null,
    val systemMemory: Memory? = null,
    val gpus: ArrayMap<String, Gpu> = ArrayMap<String, Gpu>()
)

data class Cpu(
    var name: String? = null,
    var cores: ArrayMap<String, Core> = ArrayMap<String, Core>(),
    var avgCoreFreq: String? = null,
    var minCoreFreq: String? = null,
    var maxCoreFreq: String? = null,
    var totalLoad: Value? = null,
    var packagePower: Value? = null,
    var coresPower: Value? = null,
    var packageTemp: Value?? = null
)

data class Core(
    var number: String? = null,
    var clock: Value? = null,
    var load: Value? = null,
    var power: Value? = null
)

data class Value(
    val curr: String? = null,
    val min: String? = null,
    val max: String? = null
)

data class Memory(
    var load: Value? = null,
    var used: Value? = null,
    var free: Value? = null
)

data class Gpu(
    var name: String? = null,
    var coreClock: Value? = null,
    var memoryClock: Value? = null,
    var shaderClock: Value? = null,
    var coreTemp: Value? = null,

    var coreLoad: Value? = null,
    var framebufferLoad: Value? = null,
    var videoEngineLoad: Value? = null,
    var busInterfaceLoad: Value? = null,
    var memoryLoad: Value? = null,
    var fanRpm: Value? = null,
    var fanPercentage: Value? = null,
    var power: Value? = null,

    var freeMemory: Value? = null,
    var usedMemory: Value? = null,
    var totalMemory: Value? = null,

    var pcieReceive: Value? = null,
    var pcieTransmit: Value? = null

)