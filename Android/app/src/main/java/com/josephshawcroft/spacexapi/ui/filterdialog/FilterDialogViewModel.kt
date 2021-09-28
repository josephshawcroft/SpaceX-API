package com.josephshawcroft.spacexapi.ui.filterdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface FilterDialogViewModel {

    val viewState: LiveData<ViewState>

    fun updateSuccessfulLaunchAnswer(state: SuccessfulLaunchState)
    fun updateNamingSortBy(state: NameSortedAscendingState)
}