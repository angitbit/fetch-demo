package com.example.fetch.item_groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemGroupsScreen(itemGroupsScreenState: ItemGroupsScreenState) {
    val margin = dimensionResource(R.dimen.margin)
    val title = stringResource(R.string.items_grouped)
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(title)
                },
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, margin)
            )
        }
    ) { innerPadding ->
        if (itemGroupsScreenState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center)
                )

            }
        } else if (itemGroupsScreenState.gotError) {
            ItemsError()
        } else {
            ItemGroupsList(itemGroupsScreenState.itemGroups, innerPadding)
        }
    }
}

@Preview
@Composable
private fun ItemsLoadingPreview() {
    val itemGroupsScreenState = ItemGroupsScreenState(true, emptyList(), false)
    ItemGroupsScreen(itemGroupsScreenState)
}

@Composable
private fun ItemsError() {
    val margin = dimensionResource(R.dimen.margin)
    val title = stringResource(R.string.load_error)
    val text = stringResource(R.string.load_error_subtext)
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            title, modifier = Modifier.padding(horizontal = margin), fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text, modifier = Modifier.padding(margin), fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ItemsErrorPreview() {
    val itemGroupsScreenState = ItemGroupsScreenState(false, emptyList(), true)
    ItemGroupsScreen(itemGroupsScreenState)
}

@Composable
private fun ItemGroupsList(itemGroups: List<ItemGroup>, innerPadding: PaddingValues) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(itemGroups) { item ->
            Item(item.names, item.listId)
        }
    }
}

@Preview
@Composable
private fun ItemGroupsListPreview() {
    val itemGroups = mutableListOf<ItemGroup>()
    itemGroups.add(ItemGroup(1, mutableListOf("name1", "name2")))
    itemGroups.add(ItemGroup(2, mutableListOf("name1", "name2")))
    val itemGroupsScreenState = ItemGroupsScreenState(false, itemGroups, false)
    ItemGroupsScreen(itemGroupsScreenState)
}

@Composable
private fun Item(names: List<String>, listId: Int) {
    val listIdStr = stringResource(R.string.list_id)
    val margin = dimensionResource(R.dimen.margin)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin, 0.dp, margin, margin)
    ) {
        Text(
            text = "$listIdStr $listId",
            modifier = Modifier
                .fillMaxWidth()
                .padding(margin),
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        for (name in names) {
            Text(name, modifier = Modifier.padding(margin, 0.dp, margin, 8.dp))
        }
    }
}

@Preview
@Composable
private fun ItemPreview() {
    Item(listOf("name1", "name2"), 123)
}