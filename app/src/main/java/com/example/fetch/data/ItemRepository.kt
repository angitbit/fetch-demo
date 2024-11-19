package com.example.fetch.data

import com.example.fetch.data.source.ItemSource
import com.example.fetch.fetch.FetchItem

class ItemRepository(private val itemSource: ItemSource) {
    suspend fun getFetchItems(): List<FetchItem>{
        return itemSource.getFetchItems()
    }
}