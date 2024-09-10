package com.example.customflowcolumn

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Do experiment with LazyHorizontalGrid and LazyHorizontalStaggeredGrid
 */
@Composable
fun CustomHorizontalLazyGrid(modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Adaptive(minSize = 20.dp)
    ) {
        itemsIndexed(generateListOfOrderItemData()) { index, orderItemData ->


        }
    }
}

@Composable
fun CustomHorizontalStaggeredGrid(modifier: Modifier = Modifier) {
    LazyHorizontalStaggeredGrid(
        modifier = modifier,
        rows = StaggeredGridCells.Adaptive(minSize = 20.dp)
    ) {
        itemsIndexed(generateListOfOrderItemData()) { index, orderItemData ->


        }
    }
}