# Keural
Library for easy implementation of neural networks using Kotlin

This library was created in order to support my final graduation project at FACENS - Faculdade de Engenharia de Sorocaba.

# How to use

Extend [`KeuralManager`](https://github.com/gdomingues/keural/blob/master/src/main/kotlin/br/com/germanno/keural/KeuralManager.kt) and implement the abstract properties defined by the class, according to your rules, to convert the objects from and to `DoubleArray`s.

You can also define how many layers the network will have, the activation function, and the learning rate through arguments passed to `KeuralManager` constructor.

There's a unit test class called [`KeuralManagerTest`](https://github.com/gdomingues/keural/blob/master/src/test/kotlin/br/com/germanno/keural/KeuralManagerTest.kt) where you can find an example usage of `KeuralManager`.

As you can see by that example, **it's possible to get a neural network trained and running in less than 25 lines.**
