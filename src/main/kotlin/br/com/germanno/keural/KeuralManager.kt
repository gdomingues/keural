package br.com.germanno.keural

import br.com.germanno.keural.activation_functions.ActivationFunction
import br.com.germanno.keural.activation_functions.LogisticSigmoid
import com.squareup.moshi.Moshi

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/29/17 10:59 PM
 */
abstract class KeuralManager<TInput, TOutput>(
        inputSize: Int,
        outputSize: Int,
        hiddenLayersConfiguration: IntArray = intArrayOf(10),
        activationFunction: ActivationFunction = LogisticSigmoid(),
        learningRate: Double = 0.2,
        debugMode: Boolean = false,
        neuralNetwork: NeuralNetwork? = null
) {

    protected abstract val inputToDoubleArray: (TInput) -> DoubleArray

    protected abstract val doubleArrayToOutput: (DoubleArray) -> TOutput

    protected abstract val outputToDoubleArray: (TOutput) -> DoubleArray

    private val network: NeuralNetwork =
            neuralNetwork ?: NeuralNetwork(inputSize, activationFunction, learningRate, debugMode).apply {
                hiddenLayersConfiguration.forEach { addLayer(it) }
                addLayer(outputSize)
            }

    fun trainNetwork(inputs: Array<TInput>, outputs: Array<TOutput>, cycles: Int) {
        val inputsDouble = inputs.map { inputToDoubleArray(it) }.toTypedArray()
        network.trainNetwork(inputsDouble, outputs.map { outputToDoubleArray(it) }.toTypedArray(), cycles)
    }

    fun recognize(input: TInput): TOutput {
        val output = network.evaluate(inputToDoubleArray(input))
        return doubleArrayToOutput(output)
    }

    fun networkAsJson(): String =
            Moshi.Builder()
                    .build()
                    .adapter(NeuralNetwork::class.java)
                    .toJson(network)

}