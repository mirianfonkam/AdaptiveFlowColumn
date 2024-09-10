package com.example.customflowcolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    @OptIn(ExperimentalFoundationApi::class)
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
                    val composableItems = remember { mutableStateListOf<@Composable @UiComposable () -> Unit>() }
                    val mutableListOfRemainingMenuItems = mutableListOf<@Composable () -> Unit>(

                    )
                    //CustomContextFlowColumn(composableItems, scrollState, maxWidthOfColumn)
//                    CustomLazyFlowColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        maxWidthOfColumn = maxWidthOfColumn,
//                        maxHeight = screenHeight
//                    )

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
fun CutOutOrderDecorator(
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