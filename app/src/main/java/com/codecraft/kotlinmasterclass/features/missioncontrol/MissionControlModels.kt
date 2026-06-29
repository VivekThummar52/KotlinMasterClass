package com.codecraft.kotlinmasterclass.features.missioncontrol

enum class ReactorMode {
    OFFLINE,
    STARTING,
    NOMINAL,
    WARNING,
    CRITICAL,
    STABILIZED
}

data class TelemetryPoint(
    val power: Float,
    val temperature: Float,
    val stability: Float
)

data class RadarContact(
    val id: Int,
    val angleDegrees: Float,
    val distance: Float,
    val threat: Boolean
)

data class MissionEvent(
    val sequence: Int,
    val message: String,
    val severity: EventSeverity
)

enum class EventSeverity {
    INFO,
    SUCCESS,
    WARNING,
    DANGER
}

data class MissionControlState(
    val mode: ReactorMode = ReactorMode.OFFLINE,
    val isRunning: Boolean = false,
    val power: Float = 0f,
    val targetPower: Float = 68f,
    val temperature: Float = 24f,
    val coolant: Float = 72f,
    val shields: Float = 62f,
    val oxygen: Float = 98f,
    val stability: Float = 100f,
    val shieldsOnline: Boolean = true,
    val boostActive: Boolean = false,
    val tick: Int = 0,
    val telemetry: List<TelemetryPoint> = emptyList(),
    val contacts: List<RadarContact> = defaultRadarContacts,
    val events: List<MissionEvent> = listOf(
        MissionEvent(0, "Command systems ready. Reactor is offline.", EventSeverity.INFO)
    ),
    val autopilot: AiAutopilotState = AiAutopilotState()
) {
    val alertMessage: String
        get() = when (mode) {
            ReactorMode.OFFLINE -> "Awaiting reactor ignition"
            ReactorMode.STARTING -> "Reactor ignition sequence active"
            ReactorMode.NOMINAL -> "All systems operating within nominal range"
            ReactorMode.WARNING -> "Thermal load rising. Increase coolant flow"
            ReactorMode.CRITICAL -> "Critical instability. Stabilize or shut down"
            ReactorMode.STABILIZED -> "Containment restored. Systems recovering"
        }
}

internal val defaultRadarContacts = listOf(
    RadarContact(id = 1, angleDegrees = 32f, distance = 0.72f, threat = false),
    RadarContact(id = 2, angleDegrees = 118f, distance = 0.48f, threat = true),
    RadarContact(id = 3, angleDegrees = 214f, distance = 0.82f, threat = false),
    RadarContact(id = 4, angleDegrees = 305f, distance = 0.58f, threat = false)
)

// --- AI AUTOPILOT MODELS ---

data class Pulse(
    val fromId: String,
    val toId: String,
    val progress: Float
)

data class AiAutopilotState(
    val isEngaged: Boolean = false,
    val isIntervening: Boolean = false,
    val pulses: List<Pulse> = emptyList(),
    val activeNodes: Set<String> = emptySet()
)