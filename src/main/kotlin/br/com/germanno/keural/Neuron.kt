package br.com.germanno.keural

import com.squareup.moshi.Json
import java.lang.IllegalArgumentException
import java.util.*

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/28/17 10:40 PM
 */
class Neuron(numberOfInputs: Int) {

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
            rand.nextDouble() - 0.5
        }.toDoubleArray()
    }

    fun evaluate(input: DoubleArray) {
        if (input.size != synapses.size) {
            throw IllegalArgumentException("Invalid input size")
        }

        this.input = input.clone()

        val vk = (0 until input.size).sumByDouble { input[it] * synapses[it] }

        output = 1.7159 * Math.tanh(2 * vk / 3)
    }

    fun firstDerivative() = 1.14393 * 1 / Math.pow(Math.cosh(2 * output / 3), 2.0)

}