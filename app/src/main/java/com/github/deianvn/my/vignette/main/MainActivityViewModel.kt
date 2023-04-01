package com.github.deianvn.my.vignette.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.deianvn.my.vignette.model.Country
import com.github.deianvn.my.vignette.model.VignetteConfig
import com.github.deianvn.my.vignette.repository.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.joda.time.LocalDateTime
import timber.log.Timber

class MainActivityViewModel(
    private val countryRepository: CountryRepository,
    private val vignetteRepository: VignetteRepository,
    private val validityRepository: ValidityRepository,
    private val vignetteConfigRepository: VignetteConfigRepository
) : ViewModel() {

    enum class Error {
        COUNTRIES_ERROR
    }

    private val disposables = CompositeDisposable()

    val errors = MutableLiveData<Error>()

    val countries = MutableLiveData<List<Country>>()

    val vignetteConfig = MutableLiveData<VignetteConfig?>()

    val validity = MutableLiveData<LocalDateTime?>()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun loadConfigurationData(context: Context) {
        disposables.add(
            countryRepository.getCountries(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    countries.value = it
                    loadVignetteConfig()
                }, {
                    errors.value = Error.COUNTRIES_ERROR
                })
        )
    }

    fun clearSavedData() {
        disposables.add(
            validityRepository.clearValidity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    validity.value = null
                }, {
                    Timber.e("Could not clear validity", it)
                })
        )
    }

    private fun loadVignetteConfig() {
        disposables.add(
            vignetteConfigRepository.retrieveConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vignetteConfig.value = it
                }, {
                    vignetteConfig.value = null
                })
        )
    }

    fun loadValidity() {
        disposables.add(
            validityRepository.getValidity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    validity.value = it
                }, {
                    Timber.e(it, "Validity could not be retrieved from shared preference storage.")
                    validity.value = null
                })
        )
    }

    fun requestVignette(countryCode: String, plate: String) {
        disposables.add(
            vignetteRepository.requestVignette(countryCode, plate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i(it.validityDateTo.toString())
                }, {
                    if (it is NoVignetteAvailableException) {
                        validity.value = null
                        Timber.e(it)
                    } else {
                        Timber.e(it)
                    }
                })
        )
    }

}
