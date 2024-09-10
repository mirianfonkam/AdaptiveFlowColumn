package com.example.customflowcolumn

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ContextualFlowColumn
import androidx.compose.foundation.layout.ContextualFlowColumnOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * This is an experiment using the ContextualFlowColumn to match my use case.
 * Issues and pending work:
 * - The ContextualFlowColumnScope proprieties do not provide the correct values for the items that
 * are pushed to the next column. This seems to be by design. See [ContextualFlowColumnScope] doc.
 * - The horizontal scrolling makes the column scrollable but the items are all composed. This is
 * inefficient for a large number of items because items will not be loaded lazily.
 * - When the items will be partitioned into different columns, the itemCount will have to be updated,
 * and the items
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomContextFlowColumn(
    composableItems: SnapshotStateList<@Composable @UiComposable () -> Unit>,
    scrollState: ScrollState,
    maxWidthOfColumn: Int
) {
    ContextualFlowColumn(
        itemCount = composableItems.size.coerceAtLeast(generateListOfOrderItemData().size),
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        overflow = ContextualFlowColumnOverflow.Visible,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) { index ->
        generateListOfOrderItemData().getOrNull(index)?.let {
            var headerCount = 0
            var menuItemCount = 0
            val availableHeight = maxHeightInLine
            SubcomposeLayout(modifier = Modifier.width(width = with(LocalDensity.current) { maxWidthOfColumn.toDp() })) { constraints ->
                Log.d(TAG, "Available height: $availableHeight for item ${it.header.title}")
                Log.d(TAG, "lineIndex: $lineIndex for item ${it.header.title}")
                Log.d(TAG, "index in line: $indexInLine for item ${it.header.title}")

                var canItemFitCompletely = false

                val headerPlaceable = subcompose("header${headerCount++}") {
                    OrderHeaderComponent(
                        headerData = it.header,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(8.dp)
                    )
                }.first().measure(constraints)

                val menuItemsPlaceables: List<Placeable> = it.menuItems.map { menuItemData ->
                    subcompose("menuItem${menuItemCount++}") {
                        MenuItemComponent(
                            menuItemData = menuItemData,
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }.flatten().map { menuMesurable -> menuMesurable.measure(constraints) }

                Log.d(
                    TAG,
                    "Item ${it.header.title}: There are ${menuItemsPlaceables.size} == ${it.menuItems.size} menu items total"
                )

                val cutOutBottomDecoratorBottom = subcompose("cutOutBottomDecoratorBottom") {
                    CutOutOrderDecoratorBottom()
                }.first().measure(constraints)

                val minHeight =
                    headerPlaceable.height +
                            menuItemsPlaceables.first().height +
                            cutOutBottomDecoratorBottom.height +
                            16.dp.roundToPx()

                var itemsHeight = minHeight

                // the position of the last menu item that was added to the list
                var lastMenuItemIndex = 0

                if (availableHeight.roundToPx() > minHeight) {
                    for (menuPlaceable in 1 until menuItemsPlaceables.size) {
                        if (menuItemsPlaceables[menuPlaceable].height + itemsHeight < availableHeight.roundToPx()) {
                            itemsHeight += menuItemsPlaceables[menuPlaceable].height
                            lastMenuItemIndex = menuPlaceable
                        } else {
                            Log.d(TAG, "${it.header.title}: menu item $menuPlaceable can't fit")
                            break
                        }
                    }
                } else {
                    Log.d(
                        TAG,
                        "Item ${it.header.title} cannot show any other menu item other that the first item. "
                    )
                    (1 until menuItemsPlaceables.size).count().let {
                        if (it > 0) {
                            Log.d(
                                TAG,
                                "There are $it items that can't fit because availableHeight is ${availableHeight} == $maxHeightInLine and minHeight is $minHeight"
                            )
                        }
                        // BUG: maxHeightInLine is reporting the wrong value for the items that are pushed to next column. See [ContextualFlowColumnScope]
                    }
                }
                Log.d(TAG, "Available height: $availableHeight for item ${it.header.title}")


                val completeButtonPlaceable = subcompose("completeButton") {
                    CompleteButton(onOrderCompletionCLick = {})
                }.first().measure(constraints)

                if (menuItemsPlaceables.lastIndex == lastMenuItemIndex) {
                    if (completeButtonPlaceable.height + itemsHeight < availableHeight.roundToPx()) {
                        itemsHeight += completeButtonPlaceable.height - cutOutBottomDecoratorBottom.height
                    }
                    canItemFitCompletely = true
                }
                layout(maxWidthOfColumn, itemsHeight) {
                    var currentY = 0

                    // header and the first menu item are always placed together
                    headerPlaceable.place(0, currentY)
                    currentY += headerPlaceable.height
                    menuItemsPlaceables.first().place(0, currentY)
                    currentY += menuItemsPlaceables.first().height


                    for (menuIndex in 1..lastMenuItemIndex) {
                        menuItemsPlaceables[menuIndex].place(0, currentY)
                        currentY += menuItemsPlaceables[menuIndex].height
                    }

                    if (canItemFitCompletely) {
                        completeButtonPlaceable.place(0, currentY)
                    } else {
                        cutOutBottomDecoratorBottom.place(0, currentY)
                    }
                    Log.d(
                        TAG,
                        "Available height after layout: $availableHeight for item ${it.header.title}"
                    )
                }
            }
        }
    }
}

data class GridColumn(
    val index: Int,
    var availableHeight: Int,
    val listOfItems: MutableList<ColumnItem> = mutableListOf()
)

data class ColumnItem(
    val orderItemGuid: String,
    val indexInColumn: Int,
    val height: Int,
)