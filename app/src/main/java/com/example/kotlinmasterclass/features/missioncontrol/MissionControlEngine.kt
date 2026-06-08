package com.example.kotlinmasterclass.features.missioncontrol

import kotlin.math.abs
import kotlin.math.sin

object MissionControlEngine {
    private const val MAX_TELEMETRY_POINTS = 48
    private const val MAX_EVENTS = 8

    fun start(state: MissionControlState): MissionControlState {
        if (state.isRunning) return state

        return state.copy(
            mode = ReactorMode.STARTING,
            isRunning = true,
            power = 8f,
            temperature = 38f,
            stability = 96f,
            tick = 0,
            telemetry = emptyList()
        ).withEvent("Reactor ignition confirmed.", EventSeverity.SUCCESS)
    }

    fun tick(state: MissionControlState): MissionControlState {
        if (!state.isRunning) return state

        val nextTick = state.tick + 1
        val boostLoad = if (state.boostActive) 24f else 0f
        val effectiveTarget = (state.targetPower + boostLoad).coerceAtMost(100f)
        val power = approach(state.power, effectiveTarget, if (state.boostActive) 4.8f else 2.6f)

        val shieldHeat = if (state.shieldsOnline) 6f else 0f
        val thermalTarget = 34f + power * 0.78f + boostLoad * 0.75f + shieldHeat - state.coolant * 0.48f
        val thermalNoise = sin(nextTick * 0.62f) * (1.2f + power / 55f)
        val temperature = approach(state.temperature, thermalTarget, 2.3f) + thermalNoise

        val thermalPenalty = ((temperature - 78f).coerceAtLeast(0f)) * 1.25f
        val outputPenalty = ((power - 88f).coerceAtLeast(0f)) * 0.7f
        val coolantPenalty = ((34f - state.coolant).coerceAtLeast(0f)) * 0.75f
        val targetStability = (100f - thermalPenalty - outputPenalty - coolantPenalty).coerceIn(4f, 100f)
        val stability = approach(state.stability, targetStability, 3.4f)

        val shields = if (state.shieldsOnline) {
            (state.shields - 0.08f - if (state.boostActive) 0.15f else 0f).coerceAtLeast(0f)
        } else {
            (state.shields + 0.18f).coerceAtMost(100f)
        }
        val oxygen = (state.oxygen - if (temperature > 90f) 0.035f else 0.008f).coerceAtLeast(0f)
        val mode = calculateMode(nextTick, temperature, stability)

        var next = state.copy(
            mode = mode,
            power = power.coerceIn(0f, 100f),
            temperature = temperature.coerceIn(20f, 140f),
            stability = stability,
            shields = shields,
            oxygen = oxygen,
            tick = nextTick,
            contacts = updateContacts(state.contacts, nextTick)
        )

        next = next.copy(
            telemetry = (next.telemetry + TelemetryPoint(next.power, next.temperature, next.stability))
                .takeLast(MAX_TELEMETRY_POINTS)
        )

        if (mode != state.mode) {
            next = next.withEvent(
                message = modeEvent(mode),
                severity = when (mode) {
                    ReactorMode.NOMINAL -> EventSeverity.SUCCESS
                    ReactorMode.WARNING -> EventSeverity.WARNING
                    ReactorMode.CRITICAL -> EventSeverity.DANGER
                    else -> EventSeverity.INFO
                }
            )
        }
        return next
    }

    fun setTargetPower(state: MissionControlState, value: Float): MissionControlState =
        state.copy(targetPower = value.coerceIn(20f, 100f))

    fun setCoolant(state: MissionControlState, value: Float): MissionControlState =
        state.copy(coolant = value.coerceIn(0f, 100f))

    fun toggleShields(state: MissionControlState): MissionControlState {
        val online = !state.shieldsOnline
        return state.copy(shieldsOnline = online).withEvent(
            if (online) "Deflector shields online." else "Deflector shields in recharge mode.",
            if (online) EventSeverity.SUCCESS else EventSeverity.WARNING
        )
    }

    fun toggleBoost(state: MissionControlState): MissionControlState {
        if (!state.isRunning) return state
        val active = !state.boostActive
        return state.copy(boostActive = active).withEvent(
            if (active) "Overdrive engaged. Thermal load increasing." else "Overdrive disengaged.",
            if (active) EventSeverity.WARNING else EventSeverity.INFO
        )
    }

    fun stabilize(state: MissionControlState): MissionControlState {
        if (!state.isRunning) return state
        return state.copy(
            mode = ReactorMode.STABILIZED,
            targetPower = 52f,
            coolant = (state.coolant + 22f).coerceAtMost(100f),
            boostActive = false,
            stability = (state.stability + 28f).coerceAtMost(100f)
        ).withEvent("Containment pulse deployed. Reactor stabilizing.", EventSeverity.SUCCESS)
    }

    fun emergencyShutdown(state: MissionControlState): MissionControlState =
        state.copy(
            mode = ReactorMode.OFFLINE,
            isRunning = false,
            power = 0f,
            targetPower = 68f,
            temperature = 28f,
            stability = 100f,
            boostActive = false,
            telemetry = emptyList()
        ).withEvent("Emergency shutdown complete. Core secured.", EventSeverity.DANGER)

    private fun calculateMode(tick: Int, temperature: Float, stability: Float): ReactorMode =
        when {
            stability < 34f || temperature > 112f -> ReactorMode.CRITICAL
            stability < 68f || temperature > 88f -> ReactorMode.WARNING
            tick < 5 -> ReactorMode.STARTING
            else -> ReactorMode.NOMINAL
        }

    private fun approach(current: Float, target: Float, step: Float): Float {
        if (abs(target - current) <= step) return target
        return if (target > current) current + step else current - step
    }

    private fun updateContacts(contacts: List<RadarContact>, tick: Int): List<RadarContact> =
        contacts.mapIndexed { index, contact ->
            contact.copy(
                angleDegrees = (contact.angleDegrees + 0.45f + index * 0.08f) % 360f,
                distance = (contact.distance + sin((tick + index * 5) * 0.08f) * 0.0015f)
                    .coerceIn(0.2f, 0.92f)
            )
        }

    private fun MissionControlState.withEvent(
        message: String,
        severity: EventSeverity
    ): MissionControlState = copy(
        events = (listOf(MissionEvent(tick, message, severity)) + events).take(MAX_EVENTS)
    )

    private fun modeEvent(mode: ReactorMode): String = when (mode) {
        ReactorMode.OFFLINE -> "Reactor offline."
        ReactorMode.STARTING -> "Ignition sequence in progress."
        ReactorMode.NOMINAL -> "Reactor synchronized at nominal output."
        ReactorMode.WARNING -> "Thermal warning threshold crossed."
        ReactorMode.CRITICAL -> "Containment integrity is critical."
        ReactorMode.STABILIZED -> "Containment pulse active."
    }
}
