package com.example.fetch.data

import com.example.fetch.data.source.ItemSource
import com.example.fetch.fetch.FetchItem

//this Repository provides FetchItem data/Model [the 1st M in MVVM architecture] 
class ItemRepository(private val itemSource: ItemSource) {
    suspend fun getFetchItems(): List<FetchItem>{
        return itemSource.getFetchItems()
    }
}