package com.josephshawcroft.spacexapi.flightlist

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import coil.ImageLoader
import com.josephshawcroft.spacexapi.BaseFragment
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.databinding.FragmentLaunchListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LaunchListFragment : BaseFragment<FragmentLaunchListBinding>() {

    private lateinit var viewModel: LaunchListViewModel
    private val adapter by lazy { LaunchListAdapter(ImageLoader(requireContext())) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = LaunchListViewModel.get(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchListBinding.inflate(inflater, container, false).run {
        setBinding()

        viewModel.fetchPageData()

        with(binding.userList) {
            val dividerItemDecoration = DividerItemDecoration(context, VERTICAL)
            addItemDecoration(dividerItemDecoration)
            adapter = this@LaunchListFragment.adapter
        }

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
        val companyInfo = state.companyInfo
        binding.errorText.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.companyInfoTextView.text = binding.root.context.getString(
            R.string.company_info_text,
            companyInfo.name,
            companyInfo.founder,
            companyInfo.founded,
            companyInfo.employees,
            companyInfo.launchSites,
            companyInfo.valuation
        )
        adapter.updateList(state.launchesList)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filterDialogMenuItem -> {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_launchListFragment_to_filterDialogFragment)
            true
        }
        else -> false
    }
}