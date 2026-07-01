package com.codecraft.kotlinmasterclass.features.smartcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SmartCityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CityState())
    val uiState = _uiState.asStateFlow()

    init {
        generateCity()
        startSimulation()
    }

    private fun generateCity() {
        val bList = mutableListOf<CityBuilding>()
        val nList = mutableListOf<PowerLine>()

        var idCounter = 0
        for (row in 0..9) {
            for (col in 0..9) {
                bList.add(
                    CityBuilding(
                        id = idCounter++,
                        x = col * 400f + Random.nextInt(50, 150),
                        y = row * 400f + Random.nextInt(50, 150),
                        w = Random.nextFloat() * 150f + 100f,
                        h = Random.nextFloat() * 150f + 100f,
                        heightFactor = Random.nextFloat() * 0.4f + 0.1f,
                        isPowerHub = Random.nextFloat() > 0.85f
                    )
                )
            }
        }

        val hubs = bList.filter { it.isPowerHub }
        bList.filter { !it.isPowerHub }.forEach { b ->
            val nearestHub = hubs.minByOrNull { Math.hypot((it.x - b.x).toDouble(), (it.y - b.y).toDouble()) }
            if (nearestHub != null) {
                nList.add(PowerLine(nearestHub.id, b.id))
            }
        }
        _uiState.update { it.copy(buildings = bList, network = nList) }
    }

    private fun startSimulation() {
        // High frequency animation thread link (~60 FPS pipeline)
        viewModelScope.launch {
            while (true) {
                delay(16)
                _uiState.update { it.copy(energyPhase = (it.energyPhase - 2f) % 100f) }
            }
        }

        // Domain Incident generator cycle thread
        viewModelScope.launch {
            while (true) {
                delay(2000)
                _uiState.update { state ->
                    val updatedBuildings = state.buildings.map { b ->
                        val newIncident = if (b.hasIncident) Random.nextFloat() > 0.2f else Random.nextFloat() > 0.98f
                        b.copy(hasIncident = newIncident)
                    }
                    val totalIncidents = updatedBuildings.count { it.hasIncident }
                    val currentLoad = 40f + (totalIncidents * 5f) + Random.nextFloat() * 10f

                    state.copy(
                        buildings = updatedBuildings,
                        incidents = totalIncidents,
                        powerLoad = currentLoad.coerceAtMost(100f)
                    )
                }
            }
        }
    }
}