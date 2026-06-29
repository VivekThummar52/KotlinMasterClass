package com.example.kotlinmasterclass.features.jobdiscovery

import android.R
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

data class JobType(
    val id: Int,
    val name: String
)

@HiltViewModel
class JobDiscoveryViewModel @Inject constructor() : ViewModel() {

    val jobRoles = listOf(
        JobType(1, "All"),
        JobType(2, "Product Designer"),
        JobType(3, "Mobile Developer"),
        JobType(4, "UI/UX Designer"),
        JobType(5, "HR Manager")
    )

    val jobList = listOf(
        JobItem(
            id = 1,
            companyName = "Amazon",
            companyLogo = R.drawable.ic_menu_gallery, // Placeholder
            jobTitle = "Senior Product Designer",
            salary = "$120K",
            status = "Applied",
            applicantsCount = "50+",
            backgroundColor = 0xFFB4F159
        ),
        JobItem(
            id = 2,
            companyName = "Google",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "UX Researcher",
            salary = "$150K",
            status = "New",
            applicantsCount = "120+",
            backgroundColor = 0xFFE0E0E0
        ),
        JobItem(
            id = 3,
            companyName = "Netflix",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Senior Android Developer",
            salary = "$180K",
            status = "Applied",
            applicantsCount = "80+",
            backgroundColor = 0xFFFFCDD2
        ),
        JobItem(
            id = 4,
            companyName = "Meta",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Product Manager",
            salary = "$160K",
            status = "Saved",
            applicantsCount = "200+",
            backgroundColor = 0xFFBBDEFB
        ),
        JobItem(
            id = 5,
            companyName = "Apple",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "iOS Developer",
            salary = "$145K",
            status = "Applied",
            applicantsCount = "45+",
            backgroundColor = 0xFFF5F5F5
        ),
        JobItem(
            id = 6,
            companyName = "Airbnb",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "UI Designer",
            salary = "$110K",
            status = "Saved",
            applicantsCount = "30+",
            backgroundColor = 0xFFFFEBEE
        ),
        JobItem(
            id = 7,
            companyName = "Uber",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Backend Engineer",
            salary = "$155K",
            status = "New",
            applicantsCount = "90+",
            backgroundColor = 0xFFEEEEEE
        ),
        JobItem(
            id = 8,
            companyName = "Spotify",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Data Scientist",
            salary = "$135K",
            status = "Applied",
            applicantsCount = "65+",
            backgroundColor = 0xFFC8E6C9
        ),
        JobItem(
            id = 9,
            companyName = "Slack",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Frontend Developer",
            salary = "$125K",
            status = "New",
            applicantsCount = "40+",
            backgroundColor = 0xFFE1BEE7
        ),
        JobItem(
            id = 10,
            companyName = "Microsoft",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Cloud Architect",
            salary = "$170K",
            status = "Applied",
            applicantsCount = "110+",
            backgroundColor = 0xFFB3E5FC
        ),
        JobItem(
            id = 11,
            companyName = "Tesla",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Mechanical Engineer",
            salary = "$140K",
            status = "New",
            applicantsCount = "55+",
            backgroundColor = 0xFFFFCCBC
        ),
        JobItem(
            id = 12,
            companyName = "Twitter",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Security Engineer",
            salary = "$130K",
            status = "Saved",
            applicantsCount = "25+",
            backgroundColor = 0xFFE3F2FD
        ),
        JobItem(
            id = 13,
            companyName = "LinkedIn",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Marketing Specialist",
            salary = "$95K",
            status = "Applied",
            applicantsCount = "75+",
            backgroundColor = 0xFFDCEDC8
        ),
        JobItem(
            id = 14,
            companyName = "Adobe",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Graphic Designer",
            salary = "$105K",
            status = "New",
            applicantsCount = "85+",
            backgroundColor = 0xFFFFF9C4
        ),
        JobItem(
            id = 15,
            companyName = "Oracle",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Database Admin",
            salary = "$120K",
            status = "Applied",
            applicantsCount = "35+",
            backgroundColor = 0xFFD7CCC8
        ),
        JobItem(
            id = 16,
            companyName = "Intel",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Hardware Engineer",
            salary = "$150K",
            status = "New",
            applicantsCount = "50+",
            backgroundColor = 0xFFB2EBF2
        ),
        JobItem(
            id = 17,
            companyName = "Nvidia",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "AI Researcher",
            salary = "$190K",
            status = "Applied",
            applicantsCount = "150+",
            backgroundColor = 0xFFF0F4C3
        ),
        JobItem(
            id = 18,
            companyName = "Zoom",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "QA Automation",
            salary = "$115K",
            status = "Saved",
            applicantsCount = "20+",
            backgroundColor = 0xFFE1F5FE
        ),
        JobItem(
            id = 19,
            companyName = "Salesforce",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "Solution Architect",
            salary = "$165K",
            status = "Applied",
            applicantsCount = "95+",
            backgroundColor = 0xFFD1C4E9
        ),
        JobItem(
            id = 20,
            companyName = "Shopify",
            companyLogo = R.drawable.ic_menu_gallery,
            jobTitle = "E-commerce Dev",
            salary = "$125K",
            status = "New",
            applicantsCount = "60+",
            backgroundColor = 0xFFC8E6C9
        )
    )
}