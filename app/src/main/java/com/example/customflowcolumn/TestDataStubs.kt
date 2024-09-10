package com.example.customflowcolumn

import kotlin.random.Random

fun generateOrderHeaderData(): OrderHeaderData {
    return OrderHeaderData(
        title = "Header ${Random.nextInt(1,22)}",
        identifier = "#4121",
        subtitle = "Subtitle",
    )
}

val listOfMenu = listOf(
    MenuItemData(
        name = "Philly Cheesesteak",
        quantity = "1",
    ),
    MenuItemData(
        name = "Soda",
        quantity = "2",
    ),
    MenuItemData(
        name = "Fries",
        quantity = "4",
    ),
    MenuItemData(
        name = "Burger",
        quantity = "1",
    ),
)

fun generateMenuItemDataA(): MenuItemData {
    return MenuItemData(
        name = "Philly Cheesesteak",
        quantity = "1",
    )
}

fun generateOrderItemDataA(): OrderItemData {
    return OrderItemData(
        guid = "1234",
        header = generateOrderHeaderData(),
        menuItems = List(4) { listOfMenu.random() }
    )
}

fun generateOrderItemDataB() : OrderItemData {
    return OrderItemData(
        guid = "1235",
        header = OrderHeaderData(
            title = "Header ${Random.nextInt(99, 1233)}",
            identifier = "Mirian",
            subtitle = "Subtitle",
        ),
        menuItems = List(Random.nextInt(10, 30)) { listOfMenu.random() }
    )
}

fun generateOrderItemDataC(): OrderItemData {
    return OrderItemData(
        guid = "1235",
        header = OrderHeaderData(
            title = "Header 3",
            identifier = "Mirian",
            subtitle = "Subtitle",
        ),
        menuItems = List(30) { generateMenuItemDataA() }
    )
}

fun generateListOfOrderItemData(): List<OrderItemData> {
    return listOf(
        generateOrderItemDataA(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataA(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataA(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataA(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataB(),
        generateOrderItemDataA(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
        generateOrderItemDataA(),
        generateOrderItemDataB(),
        generateOrderItemDataC(),
        generateOrderItemDataB(),
    )
}