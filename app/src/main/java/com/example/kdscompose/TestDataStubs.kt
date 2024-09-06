package com.example.kdscompose

fun generateOrderHeaderData(): OrderHeaderData {
    return OrderHeaderData(
        title = "Dine in",
        identifier = "#4121",
        employeeName = "John Doe",
        time = "1:00"
    )
}

fun generateMenuItemDataA(): MenuItemData {
    return MenuItemData(
        name = "Philly Cheesesteak",
        quantity = "1",
        seatName = "1"
    )
}

fun generateOrderGridDataA(): OrderGridData {
    return OrderGridData(
        guid = "1234",
        header = generateOrderHeaderData(),
        wasRushed = false,
        menuItems = List(4) { generateMenuItemDataA() },
        hasCompleteButton = true,
        hasTicketCutoutTop = false,
        hasTicketCutoutBottom = false,
        height = 0.0
    )
}

fun generateOrderGridDataB(): OrderGridData {
    return OrderGridData(
        guid = "1235",
        wasRushed = false,
        header = OrderHeaderData(
            title = "Takeout",
            identifier = "Mirian",
            employeeName = "Allie",
            time = "5:23"
        ),
        menuItems = List(19) {
            generateMenuItemDataA()
        },
        hasCompleteButton = false,
        hasTicketCutoutTop = false,
        hasTicketCutoutBottom = true,
        height = 0.0
    )
}

fun generateOrderGridDataC(): OrderGridData {
    return OrderGridData(
        guid = "1235",
        wasRushed = false,
        menuItems = List(5) { generateMenuItemDataA() },
        hasCompleteButton = true,
        hasTicketCutoutTop = true,
        hasTicketCutoutBottom = false,
        height = 0.0
    )
}

fun generateListOfOrderGridData(): List<OrderGridData> {
    return listOf(
        generateOrderGridDataA(),
        generateOrderGridDataB(),
        generateOrderGridDataC(),
    )
}