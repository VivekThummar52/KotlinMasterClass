package com.example.kotlinmasterclass.features.missioncontrol

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MissionControlEngineTest {

    @Test
    fun `starting reactor begins ignition sequence`() {
        val state = MissionControlEngine.start(MissionControlState())

        assertTrue(state.isRunning)
        assertEquals(ReactorMode.STARTING, state.mode)
        assertTrue(state.power > 0f)
        assertEquals(EventSeverity.SUCCESS, state.events.first().severity)
    }

    @Test
    fun `telemetry history remains bounded during long simulations`() {
        var state = MissionControlEngine.start(MissionControlState())

        repeat(120) {
            state = MissionControlEngine.tick(state)
        }

        assertEquals(48, state.telemetry.size)
        assertTrue(state.events.size <= 8)
    }

    @Test
    fun `high output with no coolant enters critical mode`() {
        var state = MissionControlEngine.start(MissionControlState())
        state = MissionControlEngine.setTargetPower(state, 100f)
        state = MissionControlEngine.setCoolant(state, 0f)
        state = MissionControlEngine.toggleBoost(state)

        repeat(80) {
            state = MissionControlEngine.tick(state)
        }

        assertEquals(ReactorMode.CRITICAL, state.mode)
        assertTrue(state.temperature > 100f)
        assertTrue(state.stability < 40f)
    }

    @Test
    fun `stabilize command reduces load and restores containment`() {
        val unstable = MissionControlState(
            mode = ReactorMode.CRITICAL,
            isRunning = true,
            targetPower = 100f,
            coolant = 12f,
            stability = 20f,
            boostActive = true
        )

        val stabilized = MissionControlEngine.stabilize(unstable)

        assertEquals(ReactorMode.STABILIZED, stabilized.mode)
        assertEquals(52f, stabilized.targetPower)
        assertEquals(34f, stabilized.coolant)
        assertEquals(48f, stabilized.stability)
        assertFalse(stabilized.boostActive)
    }

    @Test
    fun `emergency shutdown resets active reactor systems`() {
        val running = MissionControlEngine.start(MissionControlState())
        val stopped = MissionControlEngine.emergencyShutdown(running)

        assertFalse(stopped.isRunning)
        assertEquals(ReactorMode.OFFLINE, stopped.mode)
        assertEquals(0f, stopped.power)
        assertTrue(stopped.telemetry.isEmpty())
        assertEquals(EventSeverity.DANGER, stopped.events.first().severity)
    }
}
