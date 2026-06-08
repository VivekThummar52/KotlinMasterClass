package com.example.kotlinmasterclass

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class FlowTestingExampleTest {

    // A mock flow that simulates downloading 3 files, taking 5 seconds each.
    // Normally, testing this would halt your CI/CD pipeline for 15 seconds.
    private val simulatedNetworkFlow = flow {
        emit("Starting Download...")
        delay(5000) // 5 seconds
        emit("File 1 Downloaded")
        delay(5000) // 5 seconds
        emit("File 2 Downloaded")
        delay(5000) // 5 seconds
        emit("All Files Complete")
    }

    @Test
    fun `test network flow with Turbine and Virtual Time`() = runTest {
        // runTest creates a Virtual Schedular. It completely ignores real-world time.
        
        simulatedNetworkFlow.test {
            // 1. Await the first emission
            assertEquals("Starting Download...", awaitItem())
            
            // 2. Await the next item. In real life, this takes 5 seconds.
            // Under runTest, it skips the 5000ms delay instantly!
            assertEquals("File 1 Downloaded", awaitItem())
            
            assertEquals("File 2 Downloaded", awaitItem())
            assertEquals("All Files Complete", awaitItem())
            
            // 3. Ensure the flow cleanly terminates
            awaitComplete()
        }
        
        // This entire test takes ~15 milliseconds to run, despite 15 seconds of delays!
    }
}