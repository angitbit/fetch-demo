package com.example.fetch.data.source

import com.example.fetch.fetch.FetchItem

//Fake Item Source to test ItemRepository
class ItemFakeSource(private val items:List<FetchItem>): ItemSource {
    override suspend fun getFetchItems(): List<FetchItem> {
        return items
    }
}