package com.example.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.fetch.data.ItemRepository
import com.example.fetch.data.source.ItemRemoteSource
import com.example.fetch.fetch.FetchAccess
import com.example.fetch.item_groups.ItemGroupsScreen
import com.example.fetch.item_groups.ItemGroupsScreenState
import com.example.fetch.item_groups.ItemGroupsViewModel

//handles MainScreen of app that retrieves a list of JSON data and displays all the items grouped by "listId", //sorted first by "listId" then by "name" and filtering out any items where "name" is blank or null.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ItemGroupsViewModel = makeGroupsViewModel()
        viewModel.loadItems()
        enableEdgeToEdge()
        setContent {
            MainScreen(viewModel)
        }
    }

    private fun makeGroupsViewModel(): ItemGroupsViewModel {
        val fetchApi = FetchAccess.getInstance().fetchApi
        val itemRemoteSource = ItemRemoteSource(fetchApi)
        val itemRepository = ItemRepository(itemRemoteSource)
        return ViewModelProvider.create(
            owner = this,
            factory = ItemGroupsViewModel.Factory,
            extras = MutableCreationExtras().apply {
                set(ItemGroupsViewModel.MY_REPOSITORY_KEY, itemRepository)
            },
        )[ItemGroupsViewModel::class]
    }
}

@Composable
fun MainScreen(viewModel: ItemGroupsViewModel) {
    //we can't preview a Composable w/ any ViewModel so we call ItemGroupsScreen w/ only needed data
    val state by viewModel.state
    val itemGroupsScreenState =
        ItemGroupsScreenState(state.isLoading, state.itemGroups, state.gotError)
    ItemGroupsScreen(itemGroupsScreenState)
}
