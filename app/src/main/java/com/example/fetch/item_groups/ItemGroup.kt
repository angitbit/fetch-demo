package com.example.fetch.item_groups

//a group of item name(s) w/ same listId
data class ItemGroup(val listId:Int, val names:MutableList<String>) {
}