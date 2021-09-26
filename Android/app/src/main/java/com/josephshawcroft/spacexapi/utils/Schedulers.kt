package com.josephshawcroft.spacexapi.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> Single<T>.ioToMainScheduler(): Single<T> = run {
    observeOn(Schedulers.io())
    subscribeOn(AndroidSchedulers.mainThread())
}