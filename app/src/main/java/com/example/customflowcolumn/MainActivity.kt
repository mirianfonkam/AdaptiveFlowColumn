package com.example.customflowcolumn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowColumn
import androidx.compose.foundation.layout.ContextualFlowColumnOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.customflowcolumn.ui.theme.CustomFlowColumnTheme

var tileGridColumns = 6
const val TAG = "CONSOLE"

@OptIn(ExperimentalLayoutApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomFlowColumnTheme {
                val scrollState = rememberScrollState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray,
                ) {
                    val configuration = LocalConfiguration.current
                    val density = LocalDensity.current
                    val screenWidth = with(density) { configuration.screenWidthDp.dp.roundToPx() }
                    val screenHeight = with(density) { configuration.screenHeightDp.dp.roundToPx() }
                    val paddingInPixel = with(LocalDensity.current) { 16.dp.roundToPx() }
                    val maxWidthOfColumn = (screenWidth - paddingInPixel * (tileGridColumns - 1)) / tileGridColumns
                    val composableItemState =

                    ContextualFlowColumn(
                        itemCount = generateListOfOrderItemData().size,
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(scrollState),
                        overflow = ContextualFlowColumnOverflow.Visible,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) { index ->
                        generateListOfOrderItemData().getOrNull(index)?.let {
                            var headerCount = 0
                            var menuItemCount = 0
                            val menuItems = @Composable {
                                it.menuItems.map { menuItemData ->
                                    MenuItemComponent(
                                        menuItemData = menuItemData,
                                        modifier = Modifier.background(Color.White)
                                    )
                                }
                            }
                            val mutableListOfRemainingMenuItems = mutableListOf<@Composable () -> Unit>(

                            )
                            var availableHeight = maxHeightInLine
                            Log.d(TAG, "Available height: $availableHeight for item ${it.header.title}")
                            Log.d(TAG, "Available height: $lineIndex for item ${it.header.title}")
                            Log.d(TAG, "Available height: $indexInLine for item ${it.header.title}")

                            SubcomposeLayout(modifier = Modifier.width(width = with(LocalDensity.current) { maxWidthOfColumn.toDp() }) ) { constraints ->
                                Log.d(TAG, "Available height: $availableHeight for item ${it.header.title}")
                                Log.d(TAG, "lineIndex: $lineIndex for item ${it.header.title}")
                                Log.d(TAG, "inder: $indexInLine for item ${it.header.title}")


                                var canItemFitCompletely = false

                                // The minimum height of an item
                                var minHeight = 0
                                val headerPlaceable = subcompose("header${headerCount++}") {
                                    OrderHeaderComponent(
                                        headerData = it.header,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Black)
                                            .padding(8.dp)
                                    )
                                }.first().measure(constraints)
                                minHeight += headerPlaceable.height
                                val menuItemsPlaceables = subcompose("menuItems${menuItemCount++}") {
                                    menuItems()
                                }.map { it.measure(constraints) }
                                minHeight += menuItemsPlaceables.first().height

                                val cutOutBottomDecoratorBottom = subcompose("cutOutBottomDecoratorBottom") {
                                    CutOutOrderDecorator(drawableRes = R.drawable.ic_ticket_cutout_bottom)
                                }.first().measure(constraints)

                                minHeight += cutOutBottomDecoratorBottom.height
                                minHeight += 16.dp.roundToPx()
                                var itemsHeight = minHeight


                                // the position of the last menu item that was added to the list
                                var lastMenuItemIndex = 0

                                if (availableHeight.roundToPx() > minHeight) {
                                    for (index in 1 until menuItemsPlaceables.size) {

                                        if (menuItemsPlaceables[index].height + itemsHeight < availableHeight.roundToPx()) {
                                            itemsHeight += menuItemsPlaceables[index].height
                                            lastMenuItemIndex = index
                                        } else {
                                            Log.d(TAG, "Item $index can't fit")
                                            break
                                        }
                                    }
                                }
                                Log.d(TAG, "Available height: $availableHeight for item ${it.header.title}")

                                (lastMenuItemIndex + 1 until menuItemsPlaceables.size).count().let {
                                    if (it > 0) {
                                        Log.d(TAG, "There are $it items that can't fit")
                                    }
                                }

                                val completeButtonPlaceable = subcompose("completeButton") {
                                    CompleteButton(onOrderCompletionCLick = {})
                                }.first().measure(constraints)

                                if (menuItemsPlaceables.lastIndex == lastMenuItemIndex) {
                                    if (completeButtonPlaceable.height + itemsHeight < availableHeight.roundToPx()) {
                                        itemsHeight += completeButtonPlaceable.height - cutOutBottomDecoratorBottom.height
                                    }
                                }
                                layout(maxWidthOfColumn, itemsHeight) {
                                    var currentY = 0

                                    // header and the first menu item are always placed together
                                    headerPlaceable.place(0, currentY)
                                    currentY += headerPlaceable.height
                                    menuItemsPlaceables.first().place(0, currentY)
                                    currentY += menuItemsPlaceables.first().height


                                    for (index in 1..lastMenuItemIndex) {
                                        menuItemsPlaceables[index].place(0, currentY)
                                        currentY += menuItemsPlaceables[index].height
                                    }

                                    if (menuItemsPlaceables.lastIndex == lastMenuItemIndex) {
                                        completeButtonPlaceable.place(0, currentY)
                                    } else {
                                        cutOutBottomDecoratorBottom.place(0, currentY)
                                    }
                                    Log.d(TAG, "Available height after layout: $availableHeight for item ${it.header.title}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CutOutOrderDecoratorTop(modifier: Modifier = Modifier) {
    CutOutOrderDecorator(
        modifier = modifier,
        drawableRes = R.drawable.ic_ticket_cutout_top
    )
}

@Composable
fun CutOutOrderDecoratorBottom(modifier: Modifier = Modifier) {
    CutOutOrderDecorator(
        modifier = modifier,
        drawableRes = R.drawable.ic_ticket_cutout_bottom
    )
}

@Composable
private fun CutOutOrderDecorator(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int
) {
    Image(
        modifier = modifier,
        painter = painterResource(drawableRes),
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun OrderHeaderComponent(
    modifier: Modifier = Modifier,
    headerData: OrderHeaderData
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = headerData.title,
                style = MaterialTheme.typography.displaySmall,
                color = Color.Cyan,
            )
            Row {
                Text(
                    text = headerData.identifier,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More ticket options",
                    tint = Color.White,
                )
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Cyan,
        )
        Text(
            text = headerData.subtitle,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun MenuItemComponent(
    menuItemData: MenuItemData,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = menuItemData.quantity,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = menuItemData.name,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

private const val MAX_ITEM_HEIGHT = 100

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomFlowColumnTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Gray,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OrderHeaderComponent(
                    headerData = generateOrderHeaderData(),
                )
                MenuItemComponent(menuItemData = generateMenuItemDataA())
                CompleteButton {

                }
            }
        }

    }
}