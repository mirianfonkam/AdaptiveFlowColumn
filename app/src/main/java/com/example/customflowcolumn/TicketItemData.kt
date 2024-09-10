package com.example.customflowcolumn

/**
 * OrderItemData will be split into different view pieces for the UI.
 *
 * There will be mappers to convert Data DTOs to UI models.
 */
data class OrderItemData(
    val guid: String,
    val header: OrderHeaderData,
    val menuItems: List<MenuItemData>,
)

data class OrderHeaderData(
    val title: String,
    val identifier: String,
    val subtitle: String,
)

data class MenuItemData(
    val name: String,
    val quantity: String,
)