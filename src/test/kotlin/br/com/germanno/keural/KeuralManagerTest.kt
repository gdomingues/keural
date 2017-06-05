package br.com.germanno.keural

import br.com.germanno.keural.activation_functions.HyperbolicTangent
import org.junit.Assert
import org.junit.Test

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 1/30/17 3:10 AM
 */
class KeuralManagerTest {

    private val wordsDatabase = arrayOf(
            "banana",
            "sorteio",
            "carvalho",
            "montanha",
            "frasco"
    )

    private val inputSize = wordsDatabase.maxBy { it.length }!!.length * 8

    private val keuralManager by lazy {

        object : KeuralManager<String, String>(
                inputSize,
                wordsDatabase.size,
                activationFunction = HyperbolicTangent(),
                debugMode = true
        ) {

            override val inputToDoubleArray: (String) -> DoubleArray = { string ->
                val bitsInString = string.length * 8
                val initialArray = DoubleArray(inputSize - bitsInString) { 0.0 }

                string.reversed().fold(initialArray) { array, c -> array + c.toBitArray() }
            }

            override val doubleArrayToOutput: (DoubleArray) -> String = { doubleArray ->
                with(doubleArray) { wordsDatabase[indexOf(max()!!)] }
            }

            override val outputToDoubleArray: (String) -> DoubleArray = { string ->
                wordsDatabase.map { if (it == string) 1.0 else 0.0 }.toDoubleArray()
            }

        }.apply { trainNetwork(wordsDatabase, wordsDatabase, 100000) }

    }

    @Test
    fun recognize() {
        val enteredWords = arrayOf(
                "batata",
                "sorvete",
                "carrasco",
                "montando",
                "frescor"
        )

        enteredWords.forEachIndexed { i, s ->
            Assert.assertEquals(wordsDatabase[i], keuralManager.recognize(s))
        }
    }

}
