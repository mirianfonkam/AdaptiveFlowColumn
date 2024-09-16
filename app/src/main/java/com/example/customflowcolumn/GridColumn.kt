package com.example.customflowcolumn


data class GridColumn(
    var availableHeight: Int,
    val listOfItems: MutableList<ColumnItem> = mutableListOf()
) {
    fun addItem(item: ColumnItem) {
        listOfItems.add(item)
        availableHeight -= item.height
    }
}

data class ColumnItem(
    val orderItemGuid: String,
    var height: Int,
)