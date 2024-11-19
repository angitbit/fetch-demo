package com.example.fetch.data.source

import com.example.fetch.fetch.FetchApi
import com.example.fetch.fetch.FetchItem

//remote Item Source for ItemRepository
class ItemRemoteSource(private val fetchApi: FetchApi) : ItemSource {
    override suspend fun getFetchItems(): List<FetchItem> {
        return fetchApi.getFetchItems()
    }
}