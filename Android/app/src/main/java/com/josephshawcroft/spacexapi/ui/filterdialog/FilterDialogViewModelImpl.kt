package com.josephshawcroft.spacexapi.ui.filterdialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDialogViewModelImpl @Inject constructor() : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState())
    val viewState
        get() = _viewState

    fun updateSuccessfulLaunchAnswer(wasSuccessful: Boolean? = null) {
        viewState.value?.let {
            _viewState.value = it.copy(
                isSuccessfulLaunchAnswerTrueSelected = wasSuccessful ?: false,
                isSuccessfulLaunchAnswerFalseSelected = wasSuccessful?.not() ?: false
            )
        }
    }
}