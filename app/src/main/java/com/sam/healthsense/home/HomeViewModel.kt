package com.sam.healthsense.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    fun logDetailScreenViewEvent(){
        homeRepository.logDetailScreenViewEvent()
    }
}
