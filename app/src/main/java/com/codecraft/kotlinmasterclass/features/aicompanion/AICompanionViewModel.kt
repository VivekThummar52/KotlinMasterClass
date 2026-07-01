package com.codecraft.kotlinmasterclass.features.aicompanion

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AITopics(
    val topicName: String
)

data class AIQueries(
    val queryQuestion: String,
    val queryAnswer: String
)

@HiltViewModel
class AICompanionViewModel @Inject constructor(): ViewModel() {

    val aITopics = listOf(
        AITopics("Daily Life"),
        AITopics("Business"),
        AITopics("Health"),
        AITopics("Development"),
        AITopics("Construction"),
        AITopics("Education")
    )

    val aiQueries = listOf(
        AIQueries("How to improve daily productivity?", "Try using the Pomodoro technique and prioritizing tasks with a to-do list."),
        AIQueries("What are the key steps to start a business?", "Market research, creating a business plan, and securing funding are essential first steps."),
        AIQueries("How can I maintain a healthy lifestyle?", "Focus on a balanced diet, regular physical activity, and getting enough sleep."),
        AIQueries("What is the best way to learn a new programming language?", "Build small projects, read documentation, and practice consistently every day."),
        AIQueries("How do I stay motivated while studying?", "Set clear goals, take regular breaks, and reward yourself for reaching milestones.")
    )

    private val _selectedTopic = MutableStateFlow(aITopics.first())
    val selectedTopic: StateFlow<AITopics> = _selectedTopic.asStateFlow()

    fun onTopicSelected(topic: AITopics) {
        _selectedTopic.value = topic
    }

}