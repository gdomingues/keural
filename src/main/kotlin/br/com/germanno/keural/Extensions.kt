package br.com.germanno.keural

/**
 * @author Germanno Domingues - germanno.domingues@gmail.com
 * @since 2/5/17 2:01 PM
 */

fun Char.toBitArray() = DoubleArray(8).mapIndexed { index, _ -> ((toInt() shr index) and 1).toDouble() }