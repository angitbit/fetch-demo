package com.example.fetch.fetch

//holds item data from FetchApi; Comparable allows for custom sort
data class FetchItem(var listId:Int) : Comparable<FetchItem>{
    private var id:Int= 0 //unused for now but is sent by api
    var name:String?= null

    //for custom sort, prioritize listId before name
    override fun compareTo(other: FetchItem): Int {
        //compare listId
        if (listId != other.listId){
            return listId.compareTo(other.listId)
        }
        //then compare name
        val thisName = name ?: ""
        val otherName = other.name ?: ""
        return thisName.compareTo(otherName)
    }
}
