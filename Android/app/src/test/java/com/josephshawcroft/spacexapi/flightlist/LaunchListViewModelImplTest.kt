package com.josephshawcroft.spacexapi.flightlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.josephshawcroft.spacexapi.data.LaunchBuilder
import com.josephshawcroft.spacexapi.data.Response
import com.josephshawcroft.spacexapi.data.RocketBuilder
import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.repository.SpaceXRepository
import com.josephshawcroft.spacexapi.util.callOnCleared
import com.josephshawcroft.spacexapi.util.observeForTesting
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LaunchListViewModelImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var spaceXRepository: SpaceXRepository = mockk()
    private var compositeDisposable: CompositeDisposable = mockk()
    private lateinit var viewModel: LaunchListViewModelImpl

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = LaunchListViewModelImpl(spaceXRepository, compositeDisposable)

        every { compositeDisposable.add(any()) } returns true
    }

    @Test
    fun givenViewModel_whenFetchCompanyInfoCalledAndSuccessful_thenCompanyInfoLiveDataLoadsAndThenPostsSuccess() {

        val companyInfo = CompanyInfo(
            employees = 20,
            founded = 1970,
            founder = "Ronald McDonald",
            launchSites = 9,
            name = "Woolworths",
            valuation = 9405L
        )

        val subject: PublishSubject<CompanyInfo> = PublishSubject.create()
        every { spaceXRepository.fetchCompanyInfo() } returns Single.just(companyInfo)
            .delaySubscription(subject)

        viewModel.fetchCompanyInfo()

        viewModel.companyInfo.assertIsLoading()
        subject.onComplete()
        viewModel.companyInfo.assertIsSuccessAndEqualTo(companyInfo)
    }

    @Test
    fun givenViewModel_whenFetchCompanyInfoCalledAndUnsuccessful_thenCompanyInfoLiveDataLoadsAndThenPostsError() {

        val anException = Exception()

        val subject: PublishSubject<CompanyInfo> = PublishSubject.create()
        every { spaceXRepository.fetchCompanyInfo() } returns Single.error<CompanyInfo>(anException)
            .delaySubscription(subject)

        viewModel.fetchCompanyInfo()

        viewModel.companyInfo.assertIsLoading()
        subject.onComplete()
        viewModel.companyInfo.assertIsErrorAndEqualTo(anException)
    }

    @Test
    fun givenViewModel_whenFetchLaunchListCalledAndIsSuccessful_thenLaunchesLiveDataLoadsAndThenPostsSuccess() {

        val rocketIds = listOf("1", "2", "3")
        val launches = rocketIds.map { id ->
            LaunchBuilder(rocketId = id).build()
        }

        val rockets = rocketIds.map { id ->
            RocketBuilder(id = id).build()
        }

        val expectedMergedResult = launches.mapIndexed { index, launch ->
            LaunchWithRocketInfo(launch, rockets[index])
        }

        val launchesSubject = PublishSubject.create<LaunchesList>()
        every { spaceXRepository.fetchLaunches() } returns Single.just(launches)
            .delaySubscription(launchesSubject)

        val rocketsSubject = PublishSubject.create<List<Rocket>>()
        every { spaceXRepository.fetchRockets() } returns Single.just(rockets)
            .delaySubscription(rocketsSubject)

        viewModel.fetchLaunchList()
        viewModel.launches.assertIsLoading()

        launchesSubject.onComplete()
        viewModel.launches.assertIsLoading() //should still be loading as fetchRockets() is not yet complete

        rocketsSubject.onComplete()

        viewModel.launches.assertIsSuccessAndEqualTo(expectedMergedResult)
    }

    @Test
    fun givenViewModel_whenFetchLaunchListCalledAndFetchLaunchesSucceedsButFetchRocketsFails_thenLaunchesLiveDataLoadsAndThenPostsError() {

        val rocketIds = listOf("1", "2", "3")
        val launches = rocketIds.map { id ->
            LaunchBuilder(rocketId = id).build()
        }

        val launchesSubject = PublishSubject.create<LaunchesList>()
        every { spaceXRepository.fetchLaunches() } returns Single.just(launches)
            .delaySubscription(launchesSubject)

        val rocketsSubject = PublishSubject.create<List<Rocket>>()
        every { spaceXRepository.fetchRockets() } returns Single.error<List<Rocket>>(Exception())
            .delaySubscription(rocketsSubject)

        viewModel.fetchLaunchList()
        viewModel.launches.assertIsLoading()

        launchesSubject.onComplete()
        viewModel.launches.assertIsLoading()

        rocketsSubject.onComplete()
        viewModel.launches.assertIsErrorAndEqualTo(null)
    }


    @Test
    fun givenViewModel_whenFetchLaunchListCalledAndFetchLaunchesFailsButFetchRocketsSucceeds_thenLaunchesLiveDataLoadsAndThenPostsError() {

        val rocketIds = listOf("1", "2", "3")
        val rockets = rocketIds.map { id ->
            RocketBuilder(id = id).build()
        }

        val launchesSubject = PublishSubject.create<List<Launch>>()
        every { spaceXRepository.fetchLaunches() } returns Single.error<List<Launch>>(Exception())
            .delaySubscription(launchesSubject)

        val rocketsSubject = PublishSubject.create<List<Rocket>>()
        every { spaceXRepository.fetchRockets() } returns Single.just(rockets)
            .delaySubscription(rocketsSubject)

        viewModel.fetchLaunchList()
        viewModel.launches.assertIsLoading()

        rocketsSubject.onComplete()
        viewModel.launches.assertIsLoading()

        launchesSubject.onComplete()
        viewModel.launches.assertIsErrorAndEqualTo(null)
    }

    @Test
    fun givenViewModel_whenFetchLaunchListCalledAndApiRequestsSucceedButNotMatchingRocketIds_thenLaunchesLiveDataLoadsAndThenPostsSuccessMinusTheNonMatchingId() {

        val rocketIds = listOf("1", "2", "3")
        val launches = rocketIds.map { id ->
            LaunchBuilder(rocketId = id).build()
        }

        val rockets = rocketIds.map { id ->
            RocketBuilder(id = id).build()
        } + RocketBuilder(id = "Not a matching id").build()

        val expectedMergedResult = launches.mapIndexed { index, launch ->
            LaunchWithRocketInfo(launch, rockets[index])
        }

        val launchesSubject = PublishSubject.create<LaunchesList>()
        every { spaceXRepository.fetchLaunches() } returns Single.just(launches)
            .delaySubscription(launchesSubject)

        val rocketsSubject = PublishSubject.create<List<Rocket>>()
        every { spaceXRepository.fetchRockets() } returns Single.just(rockets)
            .delaySubscription(rocketsSubject)

        viewModel.fetchLaunchList()
        viewModel.launches.assertIsLoading()

        launchesSubject.onComplete()
        viewModel.launches.assertIsLoading()

        rocketsSubject.onComplete()

        viewModel.launches.assertIsSuccessAndEqualTo(expectedMergedResult)
    }


    //TODO properly test fetchData- ie ACTUALLY CALL THE METHOD!!!

    @Test
    fun givenViewModel_whenFetchDataCalledAndBothFetchLaunchListAndFetchCompanyInfoSucceed_thenViewStateIsLoaded() {

        viewModel.viewState.observeForTesting { fetchCompanyInfoSucceeds() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Loading::class.java)

        viewModel.viewState.observeForTesting { fetchLaunchListSucceeds() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Loaded::class.java)
    }

    @Test
    fun givenViewModel_whenFetchDataCalledAndFetchLaunchListFailsAndFetchCompanyInfoSucceeds_thenViewStateIsError() {

        viewModel.viewState.observeForTesting { fetchLaunchListFails() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Error::class.java)

        viewModel.viewState.observeForTesting { fetchCompanyInfoSucceeds() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Error::class.java)
    }

    @Test
    fun givenViewModel_whenFetchDataCalledAndFetchLaunchListSucceedsAndFetchCompanyInfoFails_thenViewStateIsError() {

        viewModel.viewState.observeForTesting { fetchLaunchListSucceeds() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Loading::class.java)

        viewModel.viewState.observeForTesting { fetchCompanyInfoFails() }
        assertThat(viewModel.viewState.value).isInstanceOf(ViewState.Error::class.java)
    }


    @Test
    fun givenViewModel_whenOnClearedCalled_thenDisposableDisposed() {
        every { compositeDisposable.dispose() } returns Unit

        viewModel.callOnCleared()
        verify(exactly = 1) { compositeDisposable.dispose() }
    }

    private fun <T> LiveData<T>.assertIsLoading() {
        verify { compositeDisposable.add(any()) }
        assertThat(value).isInstanceOf(Response.IsLoading::class.java)
    }

    private fun <T> LiveData<Response<T>>.assertIsSuccessAndEqualTo(expected: T) {
        assertThat(value).isEqualTo(Response.Success(expected))
    }

    private fun <T> LiveData<Response<T>>.assertIsErrorAndEqualTo(expected: Throwable?) {
        assertThat(value).isEqualTo(Response.Error<T>(expected))
    }

    private fun fetchLaunchListSucceeds() =
        givenViewModel_whenFetchLaunchListCalledAndIsSuccessful_thenLaunchesLiveDataLoadsAndThenPostsSuccess()

    private fun fetchLaunchListFails() =
        givenViewModel_whenFetchLaunchListCalledAndFetchLaunchesFailsButFetchRocketsSucceeds_thenLaunchesLiveDataLoadsAndThenPostsError()

    private fun fetchCompanyInfoSucceeds() =
        givenViewModel_whenFetchCompanyInfoCalledAndSuccessful_thenCompanyInfoLiveDataLoadsAndThenPostsSuccess()

    private fun fetchCompanyInfoFails() =
        givenViewModel_whenFetchCompanyInfoCalledAndUnsuccessful_thenCompanyInfoLiveDataLoadsAndThenPostsError()
}