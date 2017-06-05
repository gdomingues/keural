package br.com.germanno.keural.activation_functions

import br.com.germanno.keural.Neuron

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 6/5/17 12:37 AM
 */
class HyperbolicTangent : ActivationFunction {

    override fun calculateOutput(activationPotential: Double): Double =
            Math.tanh(activationPotential)

    override fun firstDerivative(neuron: Neuron): Double =
            1 - Math.pow(neuron.output, 2.0)

}