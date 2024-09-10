package com.example.customflowcolumn

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aghajari.compose.lazyflowlayout.LazyFlowColumn


/**
 * This is an experiment using the LazyFlowColumn a open source library
 *
 * Despite the name, the LazyFlowColumn is not lazy. It will compose all the items in the list and
 * does not provide a scrollable view. However, the repo can be cloned and adapted to fit the use case.
 */
@Composable
fun CustomLazyFlowColumn(
    modifier: Modifier = Modifier,
    maxWidthOfColumn: Int,
    maxHeight: Int,
) {
    LazyFlowColumn(
        modifier = modifier,
        maxLines = Int.MAX_VALUE
    ) {
        items(generateListOfOrderItemData().size) { index ->
            generateListOfOrderItemData().getOrNull(index)?.let {
                var headerCount = 0
                var menuItemCount = 0
                SubcomposeLayout(modifier = Modifier.width(width = with(LocalDensity.current) { maxWidthOfColumn.toDp() })) { constraints ->
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

                    if (maxHeight > minHeight) {
                        for (menuPlaceable in 1 until menuItemsPlaceables.size) {
                            if (menuItemsPlaceables[menuPlaceable].height + itemsHeight < maxHeight) {
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
                                    "There are $it items that can't fit because availableHeight is $maxHeight and minHeight is $minHeight"
                                )
                            }
                            // BUG: maxHeightInLine is reporting the wrong value for the items that are pushed to next column. See [ContextualFlowColumnScope]
                        }
                    }
                    Log.d(TAG, "Available height: $maxHeight for item ${it.header.title}")


                    val completeButtonPlaceable = subcompose("completeButton") {
                        CompleteButton(onOrderCompletionCLick = {})
                    }.first().measure(constraints)

                    if (menuItemsPlaceables.lastIndex == lastMenuItemIndex) {
                        if (completeButtonPlaceable.height + itemsHeight < maxHeight) {
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
                            "Available height after layout: $maxHeight for item ${it.header.title}"
                        )
                    }
                }
            }
        }

    }
}