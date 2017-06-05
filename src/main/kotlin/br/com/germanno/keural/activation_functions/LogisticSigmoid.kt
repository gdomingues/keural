package br.com.germanno.keural.activation_functions

import br.com.germanno.keural.Neuron

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 6/5/17 12:35 AM
 */
class LogisticSigmoid : ActivationFunction {

    override fun calculateOutput(activationPotential: Double): Double =
            1 / (1 + Math.exp(-activationPotential))

    override fun firstDerivative(neuron: Neuron): Double =
            neuron.output * (1 - neuron.output)

}