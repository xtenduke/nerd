package com.xtenduke.nerd.data.util

import android.util.Log
import android.util.SparseArray
import androidx.core.util.valueIterator
import com.xtenduke.nerd.data.model.Core
import com.xtenduke.nerd.data.model.Cpu
import com.xtenduke.nerd.data.model.Gpu
import com.xtenduke.nerd.data.model.OhmModelParsed
import com.xtenduke.nerd.data.model.OhmRawResponse
import com.xtenduke.nerd.data.model.Value
import kotlin.math.min

class OpenHardwareMonitorParser {
    // loop over every object and do stuff
    fun parse(
        rawData: OhmRawResponse,
    ): OhmModelParsed {
        val parsed = OhmModelParsed()
        // exit if the structure probably changed
        val data = rawData.Children?.get(0)?.Children ?: return parsed

        for (child in data) {
            when (child.ImageURL) {
                "images_icon/cpu.png" -> parsed.cpu = handleCpu(child)
                "images_icon/nvidia.png", "images_icon/amd.png" -> parsed.gpus[child.id.toString()] =
                    handleGpu(child)
            }
        }

        return parsed
    }

    // safe
    private fun getCpuCore(text: String): String? {
        val coreNumber = text.substring(
            text.lastIndexOf("#") +1,
            text.length
        )
        // only return if its a number
        return if (coreNumber.toIntOrNull() != null) coreNumber else null
    }

    private fun handleCpu(data: OhmRawResponse): Cpu {
        val cpu = Cpu(name = data.Text)

        if (data.Children != null) {
            for (cpuValueGroup in data.Children) {
                if (cpuValueGroup.Children != null) {
                    for (cpuValue in cpuValueGroup.Children) {
                        // ignoring bus speed
                        val coreNumber = getCpuCore(cpuValue.Text)

                        val currValue = Value(
                            cpuValue.Value, cpuValue.Min, cpuValue.Max
                        )

                        if (coreNumber != null) {
                            var core = cpu.cores[coreNumber] ?: Core(number = coreNumber)

                            when (cpuValueGroup.Text) {
                                "Clocks" -> {
                                    core.clock = currValue
                                }
                                "Powers" -> {
                                    core.power = currValue
                                }
                                "Load" -> {
                                    core.load = currValue
                                }
                                // Probably has temps here on recent-ish intel cpus
                                // Ryzen cpus also have per-ccd temps...
                            }

                            cpu.cores[coreNumber] = core
                        } else {
                            // non core specific vals
                            when (cpuValue.Text) {
                                "CPU Total" -> {
                                    cpu.totalLoad = currValue
                                }
                                "CPU Package" -> {
                                    // might be package power or package temp
                                    when (cpuValueGroup.Text) {
                                        "Powers" -> cpu.packagePower = currValue
                                        "Temperatures" -> cpu.packageTemp = currValue
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        // calculate average core frequency
        // avg will result in weird values for hetero cpus like 12th gen +
        var maxCoreFrequency = 0F
        var minCoreFrequency = Float.MAX_VALUE
        var totalCoreFrequency = 0F
        for (core in cpu.cores.values) {
            val freqStr = core.clock?.curr?.replace(" MHz", "") ?: "0"
            val freqFloat = freqStr.toFloatOrNull() ?: 0F

            if (freqFloat > maxCoreFrequency) {
                maxCoreFrequency = freqFloat
            }
            if (freqFloat < minCoreFrequency) {
                minCoreFrequency = freqFloat
            }

            totalCoreFrequency += freqFloat
        }

        cpu.avgCoreFreq = totalCoreFrequency.div(cpu.cores.size).toString() + " MHz"
        cpu.maxCoreFreq= "$maxCoreFrequency MHz"
        cpu.minCoreFreq = "$minCoreFrequency MHz"

        return cpu
    }

    private fun handleGpu(data: OhmRawResponse): Gpu {
        val gpu = Gpu(name = data.Text)
        if (data.Children != null) {
            for (gpuValueGroup in data.Children) {
                if (gpuValueGroup.Children != null) {
                    for (gpuValue in gpuValueGroup.Children) {
                        val currValue = Value(
                            gpuValue.Value, gpuValue.Min, gpuValue.Max
                        )

                        when (gpuValueGroup.Text) {
                            "Clocks" -> {
                                when (gpuValue.Text) {
                                    "GPU Core" -> gpu.coreClock = currValue
                                    "GPU Memory" -> gpu.memoryClock = currValue
                                    "GPU Shader" -> gpu.shaderClock = currValue
                                }
                            }
                            "Temperatures" -> {
                                when (gpuValue.Text) {
                                    "GPU Core" -> gpu.coreTemp = currValue
                                }
                            }
                            "Load" -> {
                                when (gpuValue.Text) {
                                    "GPU Core" -> gpu.coreLoad = currValue
                                    "GPU Frame Buffer" -> gpu.framebufferLoad = currValue
                                    "GPU Video Engine" -> gpu.videoEngineLoad = currValue
                                    "GPU Bus Interface" -> gpu.busInterfaceLoad = currValue
                                    "GPU Memory" -> gpu.memoryLoad = currValue
                                }
                            }
                            "Fans" -> {
                                when (gpuValue.Text) {
                                    "GPU" -> gpu.fanRpm = currValue
                                }
                            }
                            "Controls" -> {
                                when (gpuValue.Text) {
                                    "GPU Fan" -> gpu.fanPercentage = currValue
                                }
                            }
                            "Powers" -> {
                                when (gpuValue.Text) {
                                    "GPU Power" -> gpu.power = currValue
                                }
                            }
                            "Data" -> {
                                when (gpuValue.Text) {
                                    "GPU Memory Free" -> gpu.freeMemory = currValue
                                    "GPU Memory Used" -> gpu.usedMemory = currValue
                                    "GPU Memory Total" -> gpu.totalMemory = currValue
                                }
                            }
                            "Throughput" -> {
                                when (gpuValue.Text) {
                                    "GPU PCIE Rx" -> gpu.pcieReceive = currValue
                                    "GPU PCIE Tx" -> gpu.pcieTransmit = currValue
                                }
                            }
                        }
                    }
                }
            }
        }
        return gpu
    }
}