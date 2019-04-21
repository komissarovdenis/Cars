package com.cars

import com.cars.model.Manufacturer
import com.cars.presenter.ManufacturerPresenter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CarsUnitTest {

    private lateinit var view: Contract.View<Manufacturer>
    private lateinit var presenter: Contract.Presenter<Manufacturer>

    companion object {
        const val pageSize = 15
    }

    init {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Before
    fun setup() {
        presenter = ManufacturerPresenter(pageSize)
        view = mock()
        presenter.onCreate()
        presenter.onViewCreated(view)
    }

    @Test
    fun `data initial loading test`() {
        whenever(view.onNewDataSet()).then {}
        verify(view).updateGlobalLoader(false)
        assertThat(presenter.getDataSet().size).isEqualTo(pageSize)
    }

    @Test
    fun `second page loading test`() {
        whenever(view.onNewDataSet()).then {}
        if (presenter.canLoadMore()) {
            presenter.loadNext()
            whenever(view.onNewDataSet()).then {}
            assertThat(presenter.getDataSet().size).isEqualTo(pageSize * 2)
        }
    }

    @Test
    fun `clear presenter resources test`() {
        whenever(view.onNewDataSet()).then {}
        presenter.onDestroy()
        assertThat(presenter.getDataSet().size).isEqualTo(0)
    }

}
