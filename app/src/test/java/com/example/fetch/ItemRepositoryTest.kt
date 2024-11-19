package com.example.fetch

import com.example.fetch.data.ItemRepository
import com.example.fetch.data.source.ItemFakeSource
import com.example.fetch.fetch.FetchItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

class ItemRepositoryTest {
    private val item1 = FetchItem(2)
    private val item2 = FetchItem(2)
    private val item3 = FetchItem(1)
    private val item4 = FetchItem(3)
    private val items = listOf(item1, item2, item3, item4)
    private val itemFakeSource = ItemFakeSource(items)
    private lateinit var itemRepository: ItemRepository

    @Before
    fun setUp() {
        item1.name = "item1"
        item2.name = "item2"
        item3.name = "item3"
        item4.name = "item4"
        itemRepository = ItemRepository(itemFakeSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFetchItems() = runTest {
        val list = itemRepository.getFetchItems()
        advanceUntilIdle() //await getFetchItems
        assertThat(list, IsEqual(items))
    }
}