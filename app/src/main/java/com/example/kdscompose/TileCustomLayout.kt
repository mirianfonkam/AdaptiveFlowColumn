package com.example.kdscompose

import androidx.compose.foundation.layout.ContextualFlowColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import kotlin.math.max


/**
 * A custom layout that arranges its children in a manner similar to that of the FlowColumn.
 * Item are composed and arranged lazily based on the available size. Uses a SubcomposeLayout.
 * The grid has a fixed number of columns and the children have equal width and have flexible height.
 * The children are placed in the order in which they are provided. This layout fills items from top
 * to bottom, and when it runs out of space on the bottom it splits the child component to place,
 * if it can, the header and a item in the current column and the remainder part of the item in the
 * next column and then continues filling items from top to bottom. If the item the header and
 * the item can not fit in the current column, the item is placed in the next column and split
 * sequentially if necessary.
 *
 * When an item is deleted, the children that follow the deleted item are moved up to fill the gap
 * and re-split if necessary.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param columns The number of columns in the grid.
 * @param spacingBetweenEachColumn The space between each column in dp. This is the horizontal spacing.
 * @param spacingBetweenEachRow The space between each row in dp. This is the vertical spacing.
 */
@Composable
fun TileFlowLayout(
    modifier: Modifier,
    columns: FontAndColumnConfiguration = FontAndColumnConfiguration.DEFAULT,
    spacingBetweenEachColumn: Dp,
    spacingBetweenEachRow: Dp,
    content: @Composable () -> Unit
) {
//    Layout(
//        modifier = modifier,
//        content = content // replace with contents which is a list of composables instead
//    ) { measurables, constraints ->
//
//        // All columns have the same fixed width
//        val totalPaddingWidth = (columns.numColumns - 1) * spacingBetweenEachColumn.roundToPx()
//        val columnWidth = (constraints.maxWidth - totalPaddingWidth) / columns.numColumns
//
//        val placeables = measurables.map { measurable ->
//            measurable.measure(Constraints.fixedWidth(columnWidth))
//        }
//
//        layout(constraints.maxWidth, constraints.maxHeight) {
//            placeables.forEach {
////                if (!placeable.isPlaced) {
////                it.placeRelative(position.first * columnWidth, position.second)
////                }
//            }
//        }
//    }



}




@Composable
fun CustomFlowLayout(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val columnWidth = constraints.maxWidth / columns
        val placeables = measurables.map { measurable ->
            measurable.measure(Constraints.fixedWidth(columnWidth))
        }

        val columnHeights = MutableList(columns) { 0 }
        val placeablePositions = mutableListOf<Pair<Placeable, Pair<Int, Int>>>()
        var currentColumn = 0

        placeables.forEach { placeable ->
            if (columnHeights[currentColumn] + placeable.height <= constraints.maxHeight) {
                // Fits in current column
                placeablePositions.add(placeable to Pair(currentColumn, columnHeights[currentColumn]))
                columnHeights[currentColumn] += placeable.height
            } else {
                // Split and place in next column (if possible)
                val remainingHeight = constraints.maxHeight - columnHeights[currentColumn]
                if (remainingHeight > 0) {
                    // Try to split: Assume first part is the header
                    val headerMeasurable = measurables[placeables.indexOf(placeable)]
                    // Measure the header only
                    val headerPlaceable = headerMeasurable.measure(
                        Constraints(
                            minWidth = columnWidth,
                            maxWidth = columnWidth,
                            minHeight = 0,
                            maxHeight = remainingHeight
                        )
                    )

                    if (headerPlaceable.height <= remainingHeight) {
                        // Header fits, place it
                        placeablePositions.add(
                            headerPlaceable to Pair(
                                currentColumn,
                                columnHeights[currentColumn]
                            )
                        )
                        columnHeights[currentColumn] += headerPlaceable.height

                        // Place the remaining content in the next column
                        currentColumn = (currentColumn + 1) % columns
                        val contentY =
                            columnHeights[currentColumn] // Calculate content's Y position
                        placeablePositions.add(
                            placeable to Pair(
                                currentColumn,
                                contentY
                            )
                        )
                        columnHeights[currentColumn] += placeable.height - headerPlaceable.height // Subtract header height

//                        placeable.placeRelative(currentColumn * columnWidth, contentY)

                    } else {
                        // Header doesn't fit, move the whole item to the next column
                        currentColumn = (currentColumn + 1) % columns
                        placeablePositions.add(
                            placeable to Pair(
                                currentColumn,
                                columnHeights[currentColumn]
                            )
                        )
                        columnHeights[currentColumn] += placeable.height
                    }
                } else {
                    // Can't fit at all, move to the next column
                    currentColumn = (currentColumn + 1) % columns
                    placeablePositions.add(
                        placeable to Pair(
                            currentColumn,
                            columnHeights[currentColumn]
                        )
                    )
                    columnHeights[currentColumn] += placeable.height
                }
            }
        }

        layout(constraints.maxWidth, columnHeights.maxOrNull() ?: 0) {
            placeablePositions.forEach { (placeable, position) ->
//                if (!placeable.isPlaced) {
                    placeable.placeRelative(position.first * columnWidth, position.second)
//                }
            }
        }
    }
}




//@Composable
//fun TileFlowLayout(
//    modifier: Modifier = Modifier,
//    columns: Int = 2,
//    content: @Composable () -> Unit
//) {
//    Layout(
//        modifier = modifier,
//        content = content
//    ) { measurables, constraints ->
//        val columnWidth = constraints.maxWidth / columns
//        val placeables = measurables.map { measurable ->
//            measurable.measure(Constraints.fixedWidth(columnWidth))
//        }
//
//        val columnHeights = MutableList(columns) { 0 }
//        val placeablePositions = mutableListOf<Pair<Placeable, Pair<Int, Int>>>()
//
//        placeables.forEach { placeable ->
//            val minHeightColumn = columnHeights.withIndex().minByOrNull { it.value }?.index ?: 0
//            if (columnHeights[minHeightColumn] + placeable.height <= constraints.maxHeight) {
//                // Fits in current column
//                placeablePositions.add(placeable to Pair(minHeightColumn, columnHeights[minHeightColumn]))
//                columnHeights[minHeightColumn] += placeable.height
//            } else {
//                // Split and place in next column
//                val remainingHeight = constraints.maxHeight - columnHeights[minHeightColumn]
//                if (remainingHeight > 0) { // Can place at least a part in the current column
//                    // TODO: Implement splitting logic (e.g., using a header and a part of the content)
//                    // For now, just place the whole item in the next column
//                    val nextColumn = (minHeightColumn + 1) % columns
//                    placeablePositions.add(placeable to Pair(nextColumn, columnHeights[nextColumn]))
//                    columnHeights[nextColumn] += placeable.height
//                } else {
//                    // Can't fit even a part, move to the next column entirely
//                    val nextColumn = (minHeightColumn + 1) % columns
//                    placeablePositions.add(placeable to Pair(nextColumn, columnHeights[nextColumn]))
//                    columnHeights[nextColumn] += placeable.height
//                }
//            }
//        }
//
//        layout(constraints.maxWidth, columnHeights.maxOrNull() ?: 0) {
//            placeablePositions.forEach { (placeable, position) ->
//                placeable.placeRelative(position.first * columnWidth, position.second)
//            }
//        }
//    }
//}