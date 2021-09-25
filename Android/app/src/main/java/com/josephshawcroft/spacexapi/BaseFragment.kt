package com.josephshawcroft.spacexapi

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding
            ?: throw RuntimeException("Don't call getBinding() without first calling setBinding()")

    fun setBinding(binding: T) {
        _binding = binding
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}