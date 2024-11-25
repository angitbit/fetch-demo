package com.example.fetch

import com.example.fetch.data.ItemRepository
import com.example.fetch.data.source.ItemFakeSource
import com.example.fetch.fetch.FetchItem
import com.example.fetch.item_groups.ItemGroup
import com.example.fetch.item_groups.ItemGroupsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ItemGroupsViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val item1 = FetchItem(2)
    private val item2 = FetchItem(2)
    private val item3 = FetchItem(1)
    private val item4 = FetchItem(3)
    private val item5 = FetchItem(1)
    private val item6 = FetchItem(2)
    //mix Item order to test ViewModel grouping/sort
    private val items = listOf(item2, item1, item3, item4, item5)
    private val itemFakeSource = ItemFakeSource(items)
    private lateinit var itemRepository: ItemRepository
    private lateinit var viewModel: ItemGroupsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        item1.name = "item1"
        item2.name = "item2"
        item3.name = "item3"
        item4.name = "item4"
        item5.name = ""     //test items w/ blank names will be removed
        item6.name = null   //test items w/ null names will be removed
        itemRepository = ItemRepository(itemFakeSource)
        viewModel = ItemGroupsViewModel(itemRepository)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadItems() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        //Main dispatcher that wraps the Android UI thread will be unavailable here, as these tests are executed on a local JVM and not an Android;
        //viewModelScope uses a hardcoded Main dispatcher so we've to Dispatchers.setMain to test
        Dispatchers.setMain(testDispatcher)
        try {
            viewModel.loadItems()
            advanceUntilIdle() //wait for loadItems

            assert(!viewModel.state.value.isLoading)
            assert(viewModel.state.value.itemGroups == viewModel.itemGroups)
            assert(viewModel.state.value.itemGroups.size == 3)
            //verify ItemGroup & name order
            assert(viewModel.state.value.itemGroups[0] == ItemGroup(1, mutableListOf(item3.name!!)))
            assert(
                viewModel.state.value.itemGroups[1] == ItemGroup(
                    2,
                    mutableListOf(item1.name!!, item2.name!!)
                )
            )
            assert(viewModel.state.value.itemGroups[2] == ItemGroup(3, mutableListOf(item4.name!!)))
        } finally {
            Dispatchers.resetMain()
        }
    }
}