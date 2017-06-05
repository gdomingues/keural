package br.com.germanno.keural

import br.com.germanno.keural.activation_functions.ActivationFunction
import com.squareup.moshi.Json

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/29/17 3:02 AM
 */
class NeuralNetwork(
        inputSize: Int,
        @Json(name = "activationFunction") private val activationFunction: ActivationFunction,
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
            layer.add(Neuron(numberOfSynapses, activationFunction))
        }

        layers.add(layer)
    }

    fun evaluate(input: DoubleArray): DoubleArray {
        val bias = listOf(1.0)
        var output = input.map { it }

        layers.forEach { neurons ->
            neurons.forEach {
                it.evaluate((bias + output).toDoubleArray())
            }

            output = neurons.map { it.output }
        }

        return output.toDoubleArray()
    }

    fun trainNetwork(inputs: Array<DoubleArray>, outputs: Array<DoubleArray>, epochs: Int) {
        (0 until epochs).forEach {
            val isNow = debugMode && it % 500 == 0
            var error = 0.0

            inputs.forEachIndexed { i, input ->
                updateSynapses(input, outputs[i])

                if (isNow) {
                    val res = evaluate(input)
                    outputs[i].forEachIndexed { j, _ ->
                        error += outputs[i][j] - res[j]
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
        var nextLayer = layers.last()

        with(nextLayer) {
            forEachIndexed { index, neuron ->
                val error = output[index] - obtained[index]
                neuron.sigma = neuron.firstDerivative() * error
            }
        }

        layers.dropLast(1)
                .asReversed()
                .forEach { layer ->
                    layer.forEachIndexed { index, neuron ->
                        val proportionalError =
                                (0 until nextLayer.size).sumByDouble {
                                    nextLayer[it].sigma * nextLayer[it].synapses[index + 1]
                                }

                        neuron.sigma = neuron.firstDerivative() * proportionalError
                    }
                    nextLayer = layer
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