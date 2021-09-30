package com.josephshawcroft.spacexapi.ui.filterdialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDialogViewModelImpl @Inject constructor() : ViewModel(), FilterDialogViewModel {

    private val _viewState = MutableLiveData<ViewState>(ViewState())
    override val viewState = _viewState

    override fun updateSuccessfulLaunchAnswer(state: SuccessfulLaunchState) {
        viewState.value?.let {
            _viewState.value = it.copy(successfulLaunchState = state)
        }
    }

    override fun updateNamingSortBy(state: NameSortedAscendingState) {
        viewState.value?.let {
            _viewState.value = it.copy(
                nameSortedAscendingState = state
            )
        }
    }
}