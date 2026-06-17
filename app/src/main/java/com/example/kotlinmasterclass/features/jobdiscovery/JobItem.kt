package com.example.kotlinmasterclass.features.jobdiscovery

data class JobItem(
    val id: Int,
    val companyName: String,
    val companyLogo: Int,
    val jobTitle: String,
    val salary: String,
    val status: String,
    val applicantsCount: String,
    val backgroundColor: Long
)