package com.example.kdscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kdscompose.ui.theme.KDSComposeTheme
import com.example.kdscompose.ui.theme.st_blue

var tileGridColumns = 6

@OptIn(ExperimentalLayoutApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KDSComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray,
                ) {
                    FlowColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        generateListOfOrderGridData().forEach {
                            OrderGridDataComponent(
                                modifier = Modifier.width(400.dp),
                                orderGridData = it,
                                onOrderCompletionCLick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun OrderGridDataComponent(
    modifier: Modifier = Modifier,
    orderGridData: OrderGridData,
    onOrderCompletionCLick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        orderGridData.header?.let {
            OrderHeaderComponent(
                headerData = it,
            )
        } ?: run {
            if (orderGridData.hasTicketCutoutTop) {
                CutOutOrderDecorator(drawableRes = R.drawable.ic_ticket_cutout_top)
            }
        }
        orderGridData.menuItems.forEach {
            MenuItemComponent(
                menuItemData = it,
            )
        }
        if (orderGridData.hasCompleteButton) {
            CompleteButton(onOrderCompletionCLick)
        }
        if (orderGridData.hasTicketCutoutBottom) {
            CutOutOrderDecorator(drawableRes = R.drawable.ic_ticket_cutout_bottom)
        }
    }
}

@Composable
private fun CutOutOrderDecorator(@DrawableRes drawableRes: Int) {
    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painterResource(drawableRes),
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun OrderHeaderComponent(headerData: OrderHeaderData) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(Color.Black)
            .padding(8.dp)
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = headerData.employeeName,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = headerData.time,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Red,
            )
        }
    }
}

@Composable
fun MenuItemComponent(
    menuItemData: MenuItemData,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White)
            .padding(
                horizontal = 16.dp,
                vertical = 2.dp
            ),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteButton(onOrderCompletionCLick: () -> Unit) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOrderCompletionCLick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors().copy(
                disabledContainerColor = Color.Gray,
                containerColor = st_blue,
            ),
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = "COMPLETE",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KDSComposeTheme {
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