package com.josephshawcroft.spacexapi.ui.filterdialog

data class ViewState(
    val successfulLaunchState: SuccessfulLaunchState = SuccessfulLaunchState.NONE,
    val nameSortedAscendingState: NameSortedAscendingState = NameSortedAscendingState.NONE
)

enum class SuccessfulLaunchState {
    NONE, SUCCESSFUL, UNSUCCESSFUL
}

enum class NameSortedAscendingState {
    NONE, ASCENDING, DESCENDING
}