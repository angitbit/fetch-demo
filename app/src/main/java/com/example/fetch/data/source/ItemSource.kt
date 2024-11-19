package com.example.fetch.data.source

import com.example.fetch.fetch.FetchItem

// item Source interface for ItemRepository
interface ItemSource {
    suspend fun getFetchItems(): List<FetchItem>
}