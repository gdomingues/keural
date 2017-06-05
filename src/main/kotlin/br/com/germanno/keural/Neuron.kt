package br.com.germanno.keural

import br.com.germanno.keural.activation_functions.ActivationFunction
import com.squareup.moshi.Json
import java.lang.IllegalArgumentException
import java.util.*

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/28/17 10:40 PM
 */
class Neuron(
        numberOfInputs: Int,
        private val activationFunction: ActivationFunction
) {

    @Json(name = "input")
    var input: DoubleArray = DoubleArray(0)
        private set

    @Json(name = "output")
    var output: Double = Double.MIN_VALUE
        private set

    @Json(name = "synapses")
    var synapses: DoubleArray = DoubleArray(0)

    @Json(name = "sigma")
    var sigma: Double = Double.MIN_VALUE

    init {
        val rand = Random()

        synapses = DoubleArray(numberOfInputs + 1).map {
            rand.nextDouble() * 0.1
        }.toDoubleArray()
    }

    fun evaluate(input: DoubleArray) {
        if (input.size != synapses.size) {
            throw IllegalArgumentException("Invalid input size")
        }

        this.input = input.clone()

        val vk = input.foldIndexed(0.0) { index, sum, value ->
            sum + value * synapses[index]
        }

        output = activationFunction.calculateOutput(vk)
    }

    fun firstDerivative() = activationFunction.firstDerivative(this)

}