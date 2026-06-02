package com.example.kotlinmasterclass.features.generics

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// --- DOMAIN MODEL HIERARCHY FOR VISUALIZING VARIANCE ---
open class Fruit(val name: String)
class Apple(name: String = "Apple") : Fruit(name)
class Banana(name: String = "Banana") : Fruit(name)

// 1. Covariance (<out T>): Produces T. Safe to assign subtype to supertype reference. Read-only.
class FruitProducer<out T>(private val item: T) {
    fun produce(): T = item // Allowed: T is in 'out' position
    // fun consume(item: T) { } // Compile Error: T cannot be in 'in' position!
}

// 2. Contravariance (<in T>): Consumes T. Safe to assign supertype to subtype reference. Write-only.
class FruitConsumer<in T> {
    fun consume(item: T) { // Allowed: T is in 'in' position
        Log.d("MasterclassLog", "Consumed item: $item")
    }
    // fun produce(): T { } // Compile Error: T cannot be in 'out' position!
}

@HiltViewModel
class GenericsViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MasterclassLog"

    private val _uiState = MutableStateFlow("Tap a variance button below to evaluate type rules.")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    /**
     * Demonstrates Covariance (out): Preserves subtyping relationship.
     */
    fun runCovarianceDemo() {
        // Because FruitProducer is covariant (<out T>), an Apple producer IS A Fruit producer.
        val appleProducer: FruitProducer<Apple> = FruitProducer(Apple())
        val fruitProducer: FruitProducer<Fruit> = appleProducer // Compiles safely!

        val producedFruit = fruitProducer.produce()
        
        Log.d(TAG, "Covariance: Successfully treated Producer<Apple> as Producer<Fruit>.")
        _uiState.value = "Covariance (<out T>) Executed:\n" +
                "Assigned Producer<Apple> to Producer<Fruit> reference safely.\n" +
                "Produced item: ${producedFruit.name} (Read operation allowed)"
    }

    /**
     * Demonstrates Contravariance (in): Reverses subtyping relationship.
     */
    fun runContravarianceDemo() {
        // Because FruitConsumer is contravariant (<in T>), a Fruit consumer IS AN Apple consumer.
        val fruitConsumer: FruitConsumer<Fruit> = FruitConsumer()
        val appleConsumer: FruitConsumer<Apple> = fruitConsumer // Compiles safely! (Reversed)

        appleConsumer.consume(Apple())

        Log.d(TAG, "Contravariance: Successfully treated Consumer<Fruit> as Consumer<Apple>.")
        _uiState.value = "Contravariance (<in T>) Executed:\n" +
                "Assigned Consumer<Fruit> to Consumer<Apple> reference safely.\n" +
                "Consumed an Apple (Write operation allowed)"
    }

    /**
     * Demonstrates Reified Type Parameters.
     * Normally, generic types are erased at runtime. 'inline' + 'reified' keeps the type info alive!
     */
    private inline fun <reified T> identifyType(item: Any): String {
        return if (item is T) {
            "Match confirmed! Item is an instance of ${T::class.java.simpleName}."
        } else {
            "Mismatch. Item is not a ${T::class.java.simpleName}."
        }
    }

    fun runReifiedTypeDemo() {
        val unknownInstance: Any = Banana()

        // We check if our Any object is a Banana, and then if it's an Apple
        val result1 = identifyType<Banana>(unknownInstance)
        val result2 = identifyType<Apple>(unknownInstance)

        Log.d(TAG, "Reified Check: $result1 | $result2")
        _uiState.value = "Reified Type Evaluation:\nInput instance: Banana\n\n" +
                "Check 1: $result1\n" +
                "Check 2: $result2"
    }
}