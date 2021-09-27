package com.josephshawcroft.spacexapi.flightlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.databinding.FragmentFilterDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment() {

    private lateinit var viewModel: LaunchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LaunchListViewModel.get(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFilterDialogBinding.inflate(inflater, container, false).run {
        sortAscending.setOnClickListener { view -> onSortRadioButtonClicked(view) }
        sortDescending.setOnClickListener { view -> onSortRadioButtonClicked(view) }
        root
    }

    private fun setFilters(vararg filters: LaunchFilter) = viewModel.setFilters(*filters)

    private fun onSortRadioButtonClicked(view: View) = when (view.id) {
        R.id.sortAscending -> viewModel.sortLaunchesBy(ascending = true)
        R.id.sortDescending -> viewModel.sortLaunchesBy(ascending = false)
        else -> Unit
    }
}