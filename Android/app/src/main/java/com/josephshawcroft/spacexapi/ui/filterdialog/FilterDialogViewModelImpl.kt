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

    fun updateSuccessfulLaunchAnswer(state: SuccessfulLaunchState) {
        viewState.value?.let {
            _viewState.value = it.copy(successfulLaunchState = state)
        }
    }

    fun updateNamingSortBy(state: NameSortedAscendingState) {
        viewState.value?.let {
            _viewState.value = it.copy(
                nameSortedAscendingState = state
            )
        }
    }
}