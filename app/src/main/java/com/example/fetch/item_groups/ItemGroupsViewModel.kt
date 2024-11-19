package com.example.fetch.item_groups

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fetch.data.ItemRepository
import com.example.fetch.fetch.FetchItem
import kotlinx.coroutines.launch

//denotes State of ItemGroupsScreen so that ItemGroupsViewModel can update UI/View
data class ItemGroupsScreenState(
    val isLoading: Boolean = false,
    val itemGroups: List<ItemGroup> = emptyList(),
    val gotError: Boolean = false
)

//this ViewModel [the VM in MVVM architecture] gets data/Model [the 1st M in MVVM] from ItemRepository
//and updates UI/View [the 1st V in MVVM] via ItemGroupsScreenState;
//It retrieves a list of JSON data and groups all the items by "listId", sorted first by "listId" then by 
//"name" and filtering out any items where "name" is blank or null.
class ItemGroupsViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    var itemGroups:List<ItemGroup>? = null
    private val _state = mutableStateOf(ItemGroupsScreenState())
    val state: State<ItemGroupsScreenState> = _state

    fun loadItems() {
        itemGroups?.let{
            return //avoid reload
        }
        viewModelScope.launch {
            try {
                //init UI state
                _state.value = _state.value.copy(
                    isLoading = true,
                    itemGroups = _state.value.itemGroups,
                    gotError = false
                )
                val namedItems = getNamedItems(itemRepository.getFetchItems())
                val newGroups = makeItemGroups(namedItems)
                //update UI state
                _state.value =
                    _state.value.copy(isLoading = false, itemGroups = newGroups, gotError = false)
                itemGroups = newGroups
            } catch (ex: Exception) {
                //update UI state
                _state.value = _state.value.copy(
                    isLoading = false,
                    itemGroups = emptyList(),
                    gotError = true
                )
                val errorMsg = ex.message ?: "loadItems error"
                Log.e("TAG", "loadItems: $errorMsg")
            }
        }
    }

    private fun getNamedItems(items: List<FetchItem>): List<FetchItem> {
        val namedItems = mutableListOf<FetchItem>()
        for (item in items) {
            item.name?.let {
                if (item.name!!.isNotEmpty()) {
                    namedItems.add(item)
                }
            }
        }
        return namedItems
    }

    private fun makeItemGroups(namedItems: List<FetchItem>): List<ItemGroup> {
        var currentListId = -1 //init to invalid listId
        var currentItemGroup: ItemGroup? = null
        val itemGroups = mutableListOf<ItemGroup>()
        //loop thru sorted namedItems
        for (item in namedItems.sorted()) {
            //if item listId differs
            if (currentListId != item.listId) {
                //make new itemGroup containing item name
                val names = mutableListOf<String>()
                item.name?.let { names.add(it) }
                currentItemGroup = ItemGroup(item.listId, names)
                itemGroups.add(currentItemGroup)

                currentListId = item.listId //listId is now current
            } else {
                //add item name to currentitemGroup
                currentItemGroup?.names?.add(item.name ?: "")
            }
        }
        return itemGroups
    }

    companion object {
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<ItemRepository> {}

        //custom viewModelFactory as ItemGroupsViewModel depends on ItemRepository
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as ItemRepository
                ItemGroupsViewModel(
                    itemRepository = myRepository
                )
            }
        }
    }

}