package com.cavigna.tdcackes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cavigna.tdcackes.model.local.db.BaseDeDatos
import com.cavigna.tdcackes.model.local.db.CakeDao
import com.cavigna.tdcackes.model.models.Cake
import com.cavigna.tdcackes.model.models.CakeDetails
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class LocalTestDB {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test.db")
    lateinit var db: BaseDeDatos

    private lateinit var  dao: CakeDao

    @Before
    fun init(){
        hiltRule.inject()
        dao = db.dao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertCakeTest()= runBlockingTest{
        val cake = Cake(120, "Prueba")
        val cakeNot = Cake(150, " No está")


        dao.insertCakes(listOf(cake))
        val allCakes = dao.selectAllCAkes()
        assertThat(allCakes).contains(cake)
       // assertThat(allCakes).contains(cakeNot)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertCakeDetailsTest() = runBlockingTest {
        val cakeDetails = CakeDetails(500, "CakeDetails")

        dao.insertCakeDetails(cakeDetails)
        assertThat(dao.selectCakeDetail(500)).isEqualTo(cakeDetails)
    }
}