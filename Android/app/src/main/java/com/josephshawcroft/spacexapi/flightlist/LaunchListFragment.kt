package com.josephshawcroft.spacexapi.flightlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.josephshawcroft.spacexapi.BaseFragment
import com.josephshawcroft.spacexapi.databinding.FragmentLaunchListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchListFragment : BaseFragment<FragmentLaunchListBinding>() {

    private lateinit var viewModel: LaunchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LaunchListViewModel.get(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchListBinding.inflate(inflater, container, false).run {
        setBinding()
        viewModel.fetchPageData()

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Loading -> showLoadingState()
                is ViewState.Loaded -> showLoadedState(state)
                is ViewState.Error -> showErrorState()
            }
        }
        root
    }

    private fun showLoadingState() {
        binding.errorText.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showErrorState() {
        binding.errorText.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoadedState(state: ViewState.Loaded) {
        binding.errorText.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }
}