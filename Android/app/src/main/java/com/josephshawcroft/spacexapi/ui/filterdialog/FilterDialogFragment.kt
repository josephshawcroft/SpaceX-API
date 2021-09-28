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
    private val viewModelImpl: FilterDialogViewModelImpl by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFilterDialogBinding.inflate(inflater, container, false).run {
        sortAscending.setOnClickListener { view -> onSortRadioButtonClicked(view) }
        sortDescending.setOnClickListener { view -> onSortRadioButtonClicked(view) }
        answerTrue.setOnClickListener {
            addFilter(SuccessFilter(true))
            viewModelImpl.updateSuccessfulLaunchAnswer(true)
        }
        answerFalse.setOnClickListener {
            addFilter(SuccessFilter(false))
            viewModelImpl.updateSuccessfulLaunchAnswer(false)
        }
        clearFiltersButton.setOnClickListener {
            clearFilters()
            viewModelImpl.updateSuccessfulLaunchAnswer(null)
        }

        viewModelImpl.viewState.observe(viewLifecycleOwner, Observer { state ->
            if (state == null) return@Observer
            answerTrue.isChecked = state.isSuccessfulLaunchAnswerTrueSelected
            answerFalse.isChecked = state.isSuccessfulLaunchAnswerFalseSelected
        })

        root
    }

    private fun setFilters(vararg filters: LaunchFilter) = launchViewModel.setFilters(*filters)

    private fun addFilter(filter: LaunchFilter) = launchViewModel.setFilters(filter)

    private fun clearFilters() = launchViewModel.setFilters()

    private fun onSortRadioButtonClicked(view: View) = when (view.id) {
        R.id.sortAscending -> launchViewModel.sortLaunchesBy(ascending = true)
        R.id.sortDescending -> launchViewModel.sortLaunchesBy(ascending = false)
        else -> Unit
    }
}