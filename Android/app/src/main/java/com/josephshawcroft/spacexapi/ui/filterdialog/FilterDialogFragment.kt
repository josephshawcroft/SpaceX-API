package com.josephshawcroft.spacexapi.ui.filterdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.databinding.FragmentFilterDialogBinding
import com.josephshawcroft.spacexapi.ui.filterdialog.NameSortedAscendingState.*
import com.josephshawcroft.spacexapi.ui.filterdialog.SuccessfulLaunchState.*
import com.josephshawcroft.spacexapi.ui.filterdialog.SuccessfulLaunchState.NONE
import com.josephshawcroft.spacexapi.ui.flightlist.*
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime

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
            launchViewModel.clearFilters()

            fromYear.text.clear()
            toYear.text.clear()

            wasLaunchSuccessRadioGroup.clearCheck()
            sortByRadioGroup.clearCheck()

            dialogViewModel.updateSuccessfulLaunchAnswer(NONE)
            dialogViewModel.updateNamingSortBy(NameSortedAscendingState.NONE)
        }

        fromYear.doOnTextChanged { text, _, _, _ ->
            val count = text?.count() ?: 0
            val toYearCount = toYear.text?.count() ?: 0
            if (count < 4 || toYearCount < 4) return@doOnTextChanged
            text?.let { fromYear ->
                toYear.text?.let { toYear ->
                    evaluateDateRange(
                        Integer.parseInt(fromYear.toString()),
                        Integer.parseInt(toYear.toString())
                    )
                }
            }
        }

        toYear.doOnTextChanged { text, _, _, _ ->
            val count = text?.count() ?: 0
            val fromYearCount = fromYear.text?.count() ?: 0
            if (count < 4 || fromYearCount < 4) return@doOnTextChanged
            text?.let { toYear ->
                fromYear.text?.let { fromYear ->
                    evaluateDateRange(
                        Integer.parseInt(fromYear.toString()),
                        Integer.parseInt(toYear.toString())
                    )
                }
            }
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

    private fun evaluateDateRange(fromYear: Int, toYear: Int) {
        if (fromYear > toYear) {
            Toast.makeText(requireContext(), getString(R.string.incorrect_date_range), LENGTH_SHORT)
                .show()
            return
        }

        val fromDate = DateTime().withDate(fromYear, 1, 1).withTimeAtStartOfDay()
        val toDate = DateTime().withDate(toYear + 1, 1, 1).withTimeAtStartOfDay()

        launchViewModel.addFilter(YearFilter(fromDate to toDate))
    }

    private fun addFilter(filter: LaunchFilter) = launchViewModel.addFilter(filter)

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