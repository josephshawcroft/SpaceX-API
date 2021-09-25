package com.josephshawcroft.spacexapi.flightlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ): View {
        val binding = FragmentLaunchListBinding.inflate(inflater, container, false)
        setBinding(binding)
        return binding.root
    }
}