package com.josephshawcroft.spacexapi.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/*
    Extension method to manually call VM onCleared for testing purposes
 */
fun ViewModel.callOnCleared() {
    val viewModelStore = ViewModelStore()
    val viewModelProvider = ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = this@callOnCleared as T
    })
    viewModelProvider.get(this@callOnCleared::class.java)

    viewModelStore.clear()//To call clear() in ViewModel
}