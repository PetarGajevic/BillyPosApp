package com.wind.billypos.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


abstract class BaseViewModel() : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var skip: Int? = 0

    val mutableLiveDataLoading = MutableLiveData(false)
    val mutableLiveDataLoadMoreLoading = MutableLiveData(false)

    val mutableLiveDataErrorHandler: MutableLiveData<Throwable> = MutableLiveData<Throwable>(null)

    fun handleError(throwable: Throwable) {
        mutableLiveDataErrorHandler.value =
            if (throwable is CompositeException && !throwable.exceptions.isNullOrEmpty()) throwable.exceptions[0] else throwable
    }

    fun <T> addCompositeDisposable(
        observable: Observable<T>,
        onSuccess: (T) -> Unit,
        onError: (() -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false,
        handleError: Boolean = true
    ) {
        mutableLiveDataLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    skip = THRESHOLD
                    onSuccess(it)
                }, {
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    if(handleError) {
                        handleError(it)
                    }
                    onError?.let { _ ->
                        onError()
                    }
                })
        )
    }

    fun <T> addCompositeDisposable(
        maybe: Maybe<T>,
        onSuccess: (T) -> Unit,
        onError: (() -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false,
        handleError: Boolean = true
    ) {
        mutableLiveDataLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            maybe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    skip = THRESHOLD
                    onSuccess(it)
                }, {
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    if(handleError) {
                        handleError(it)
                    }
                    onError?.let { _ ->
                        onError()
                    }
                }, {
                    onError?.let { _ ->
                        onError()
                    }
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                })
        )
    }

    fun <T> addCompositeDisposable(
        single: Single<T>,
        onSuccess: (T) -> Unit,
        onError: (() -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false,
        handleError: Boolean = true
    ) {
        mutableLiveDataLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    skip = THRESHOLD
                    onSuccess(it)
                }, {
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    if(handleError) {
                        handleError(it)
                    }
                    onError?.let { _ ->
                        onError()
                    }
                })
        )
    }

    fun <T> addCompositeDisposableLoadMore(
        observable: Observable<List<T>>,
        onSuccess: (List<T>) -> Unit,
        onError: (() -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false
    ) {
        if (skip == null) {
            return
        }

        mutableLiveDataLoadMoreLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    skip = if (it.isNullOrEmpty() || it.size < THRESHOLD) {
                        null
                    } else {
                        skip?.plus(it.size)
                    }
                    mutableLiveDataLoadMoreLoading.value = keepLoadingOnFinish
                    onSuccess(it)
                }, {
                    it.printStackTrace()
                    mutableLiveDataLoadMoreLoading.value = keepLoadingOnFinish
                    handleError(it)
                    onError?.let { _ ->
                        onError()
                    }
                })
        )
    }

    fun <T> addCompositeDisposableLoadMore(
        maybe: Maybe<List<T>>,
        onSuccess: (List<T>) -> Unit,
        onError: (() -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false
    ) {
        if (skip == null) {
            return
        }

        mutableLiveDataLoadMoreLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            maybe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    skip = if (it.isNullOrEmpty() || it.size < THRESHOLD) {
                        null
                    } else {
                        skip?.plus(it.size)
                    }
                    mutableLiveDataLoadMoreLoading.value = keepLoadingOnFinish
                    onSuccess(it)
                }, {
                    mutableLiveDataLoadMoreLoading.value = keepLoadingOnFinish
                    handleError(it)
                    onError?.let { _ ->
                        onError()
                    }
                })
        )
    }

    fun <T> addCompositeDisposableUnhandledError(
        observable: Observable<T>,
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null,
        keepLoadingOnFinish: Boolean = false
    ) {
        mutableLiveDataLoading.value = true
        mutableLiveDataErrorHandler.value = null
        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    onSuccess(it)
                }, {
                    mutableLiveDataLoading.value = keepLoadingOnFinish
                    onError?.let { _ ->
                        onError(it)
                    }
                })
        )
    }



    fun resetSkip() {
        skip = 0
    }

    companion object {
        const val THRESHOLD = 100
    }

}