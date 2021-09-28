package com.josephshawcroft.spacexapi.ui.filterdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.databinding.FragmentFilterDialogBinding
import com.josephshawcroft.spacexapi.ui.filterdialog.NameSortedAscendingState.*
import com.josephshawcroft.spacexapi.ui.filterdialog.SuccessfulLaunchState.*
import com.josephshawcroft.spacexapi.ui.filterdialog.SuccessfulLaunchState.NONE
import com.josephshawcroft.spacexapi.ui.flightlist.LaunchFilter
import com.josephshawcroft.spacexapi.ui.flightlist.LaunchListViewModel
import com.josephshawcroft.spacexapi.ui.flightlist.LaunchListViewModelImpl
import com.josephshawcroft.spacexapi.ui.flightlist.SuccessFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment() {

    private val launchViewModel: LaunchListViewModel by hiltNavGraphViewModels<LaunchListViewModelImpl>(
        R.id.nav_graph
    )

    private val dialogViewModel: FilterDialogViewModelImpl by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFilterDialogBinding.inflate(inflater, container, false).run {
        answerSuccessful.setOnClickListener {
            addFilter(SuccessFilter(true))
            dialogViewModel.updateSuccessfulLaunchAnswer(SUCCESSFUL)
        }
        answerUnsuccessful.setOnClickListener {
            addFilter(SuccessFilter(false))
            dialogViewModel.updateSuccessfulLaunchAnswer(UNSUCCESSFUL)
        }
        clearFiltersButton.setOnClickListener {
            clearFilters()
            wasLaunchSuccessRadioGroup.clearCheck()
            dialogViewModel.updateSuccessfulLaunchAnswer(NONE)
        }

        sortAscending.setOnClickListener { view -> onSortRadioButtonClicked(view) }
        sortDescending.setOnClickListener { view -> onSortRadioButtonClicked(view) }

        dialogViewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            if (state == null) return@Observer
            answerSuccessful.isChecked = (state.successfulLaunchState == SUCCESSFUL)
            answerUnsuccessful.isChecked = (state.successfulLaunchState == UNSUCCESSFUL)

            sortAscending.isChecked = (state.nameSortedAscendingState == ASCENDING)
            sortDescending.isChecked = (state.nameSortedAscendingState == DESCENDING)
        })

        root
    }

    private fun setFilters(vararg filters: LaunchFilter) = launchViewModel.setFilters(*filters)

    private fun addFilter(filter: LaunchFilter) = launchViewModel.setFilters(filter)

    private fun clearFilters() = launchViewModel.setFilters()

    private fun onSortRadioButtonClicked(view: View) = when (view.id) {
        R.id.sortAscending -> {
            dialogViewModel.updateNamingSortBy(ASCENDING)
            launchViewModel.sortLaunchesBy(ascending = true)
        }
        R.id.sortDescending -> {
            dialogViewModel.updateNamingSortBy(DESCENDING)
            launchViewModel.sortLaunchesBy(ascending = false)
        }
        else -> Unit
    }
}