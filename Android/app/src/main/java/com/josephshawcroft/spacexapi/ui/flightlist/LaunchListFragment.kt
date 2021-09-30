package com.josephshawcroft.spacexapi.ui.flightlist

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import coil.ImageLoader
import com.josephshawcroft.spacexapi.BaseFragment
import com.josephshawcroft.spacexapi.R
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.databinding.FragmentLaunchListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException

@AndroidEntryPoint
class LaunchListFragment : BaseFragment<FragmentLaunchListBinding>(), LaunchListItemClickListener {

    private val viewModel: LaunchListViewModel by hiltNavGraphViewModels<LaunchListViewModelImpl>(R.id.nav_graph)
    private var adapter: LaunchListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchListBinding.inflate(inflater, container, false).run {
        setBinding()

        adapter = LaunchListAdapter(ImageLoader(binding.root.context), this@LaunchListFragment)

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
        binding.companyInfoDivider.visibility = View.VISIBLE
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
        adapter?.updateList(state.launchesList)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filterDialogMenuItem -> {
            Navigation.findNavController(requireView()).run {
                if (currentDestination?.id == R.id.launchListFragment) navigate(R.id.action_launchListFragment_to_filterDialogFragment)
                true
            }
        }
        else -> false
    }

    override fun onLaunchItemClicked(launch: LaunchWithRocketInfo) {
        launch.launch.articleUrl?.let { openWebPage(it) } ?: displayErrorToast()
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)

        val uri = try {
            Uri.parse(url)
        } catch (e: NullPointerException) {
            return
        }

        intent.data = uri

        if (intent.resolveActivityInfo(
                requireActivity().packageManager,
                PackageManager.MATCH_DEFAULT_ONLY
            )?.exported == true
        ) {
            startActivity(intent)
        } else {
            displayErrorToast()
        }
    }

    private fun displayErrorToast() =
        Toast.makeText(requireContext(), getString(R.string.web_page_unavailable), LENGTH_SHORT)
            .show()

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

}