package br.com.germanno.keural.activation_functions

import br.com.germanno.keural.Neuron

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 6/5/17 12:30 AM
 */
interface ActivationFunction {

    fun calculateOutput(activationPotential: Double): Double

    fun firstDerivative(neuron: Neuron): Double

}