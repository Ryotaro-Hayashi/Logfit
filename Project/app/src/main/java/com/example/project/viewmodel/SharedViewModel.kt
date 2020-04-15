package com.example.project.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var bodyWeight: Int = 58
        private set

    fun updateInformation() {
        bodyWeight = 60
    }
}