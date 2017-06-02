package br.com.germanno.keural

import com.squareup.moshi.Json

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/29/17 3:02 AM
 */
class NeuralNetwork(
        inputSize: Int,
        @Json(name = "learningRate") private val learningRate: Double,
        @Json(name = "debugMode") private val debugMode: Boolean = false
) {

    @Json(name = "layers")
    val layers: MutableList<List<Neuron>> = mutableListOf()

    @Json(name = "inputLayer")
    val inputLayer: DoubleArray = DoubleArray(inputSize)

    fun addLayer(numberOfNeurons: Int) {
        val layer = mutableListOf<Neuron>()
        val numberOfSynapses = if (layers.size == 0) inputLayer.size else layers.last().size

        (0 until numberOfNeurons).forEach {
            layer.add(Neuron(numberOfSynapses))
        }

        layers.add(layer)
    }

    fun evaluate(input: DoubleArray): DoubleArray {
        input.forEachIndexed { i, inputValue -> inputLayer[i] = inputValue }

        var signals = DoubleArray(inputLayer.size + 1)
        signals[0] = 1.0

        input.forEachIndexed { i, inputValue ->
            signals[i + 1] = inputValue
        }

        layers.forEach { layer ->
            layer.forEach { it.evaluate(signals) }

            signals = DoubleArray(layer.size + 1)
            signals[0] = 1.0

            layer.forEachIndexed { i, neuron ->
                signals[i + 1] = neuron.output
            }
        }

        return signals
    }

    fun trainNetwork(inputs: Array<DoubleArray>, outputs: Array<DoubleArray>, epochs: Int) {
        (0 until epochs).forEach {
            val isNow = debugMode && it.rem(500) == 0
            var error = 0.0

            inputs.forEachIndexed { i, input ->
                updateSynapses(input, outputs[i])

                if (isNow) {
                    val res = evaluate(input)
                    outputs[i].forEachIndexed { j, _ ->
                        error += outputs[i][j] - res[j + 1]
                    }
                }
            }

            if (isNow) {
                println("$it - Error: $error")
            }
        }
    }

    private fun updateSynapses(input: DoubleArray, output: DoubleArray) {
        val obtained = evaluate(input)

        layers.last().forEachIndexed { i, _ ->
            val error = output[i] - obtained[i + 1]
            with(layers.last()[i]) {
                sigma = firstDerivative() * error
            }
        }

        for (i in layers.size - 2 downTo 0) {
            val layer = layers[i]
            for (j in 0 until layer.size) {
                val neuron = layer[j]
                val nextLayer = layers[i + 1]
                val s = (0 until nextLayer.size).sumByDouble {
                    nextLayer[it].sigma * nextLayer[it].synapses[j + 1]
                }

                neuron.sigma = neuron.firstDerivative() * s
            }
        }

        layers.forEach { layer ->
            layer.forEach { neuron ->
                neuron.synapses.forEachIndexed { i, _ ->
                    neuron.synapses[i] += learningRate * neuron.sigma * neuron.input[i]
                }
            }
        }
    }

}