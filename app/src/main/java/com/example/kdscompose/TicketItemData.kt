package com.example.kdscompose


/**
 * OrderItemData will be split into OrderGridData depending on the available screen space.
 *
 * There will be mappers to convert Data DTOs to UI models.
 */
data class OrderItemData(
    val guid: String,
    val gridItems: List<OrderGridData> = emptyList(),
    val state: OrderState,
)

data class OrderGridData(
    val guid: String,
    val header: OrderHeaderData? = null,
    val wasRushed: Boolean,
    val menuItems: List<MenuItemData> = emptyList(),
    val hasCompleteButton: Boolean = false,
    val hasTicketCutoutTop: Boolean = false,
    val hasTicketCutoutBottom: Boolean = false,
    val height: Double = 0.0,
)

data class OrderData(
    val guid: String,
    val header: OrderHeaderData,
    val menuItems: List<MenuItemData>,
)

data class OrderHeaderData(
    val title: String,
    val identifier: String,
    val employeeName: String,
    val time: String,
)

data class MenuItemData(
    val name: String,
    val quantity: String,
    val seatName: String,
)

data class ModifierSetData(
    val name: String,
    val items: List<ModifierItemData>,
)

data class ModifierItemData(
    val name: String,
)

enum class OrderState {
    NEW_ORDER, // white background
    ORDER_WITHIN_5_MINUTES, // yellow background
    ORDER_LAST_CALL, // red background
}

const val MIN_NUM_COLUMNS = 3

enum class FontAndColumnConfiguration(val numColumns: Int) {
    SMALL(7),
    DEFAULT(6),
    MEDIUM(5),
    LARGE(4),
    LUDICROUS(MIN_NUM_COLUMNS),
}
