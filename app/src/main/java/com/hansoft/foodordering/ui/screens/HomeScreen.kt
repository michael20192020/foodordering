package com.hansoft.foodordering.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.utils.CustomTopAppBar
import com.hansoft.foodordering.viewmodel.AuthViewModel
import com.hansoft.foodordering.viewmodel.CartViewModel
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val CART_TYPE_KEY = stringPreferencesKey("cart_type")
@Composable
fun HomeScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel,
               cartViewModel: CartViewModel, userId: String, onBackClick: () -> Unit,modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf("create_order") }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    var context = LocalContext.current
   // var cartType by rememberSaveable {
   //     mutableStateOf("dine in")
   // }
    val cartType by menuViewModel.readCartType(context).collectAsState(initial = "dine in")
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Online Food Ordering",
                onNavigationClick = onBackClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Text(text = "Home Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            // Spacer(modifier = Modifier.height(16.dp))
            // Text(text = "Online Food Ordering", style = MaterialTheme.typography.headlineMedium)
            // Spacer(modifier = Modifier.height(16.dp))
            //  Text(text = "User ID: $userId", style = MaterialTheme.typography.bodyMedium)
            // Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    screen = "create_order"
                    menuViewModel.clearSelection()
                })
                { Text("Menu") }
                Button(onClick = { screen = "view_orders" })
                { Text("Orders") }
                Button(onClick = { screen = "view_cart" })
                { Text("Cart") }
            }
            Spacer(modifier = Modifier.height(6.dp))
            when (screen) {
                "view_orders" -> OrderListScreen(orderViewModel, userId)
                "create_order" -> MenuScreen(menuViewModel, orderViewModel, cartViewModel, userId,
                    {
                        //cartType = "dine in"
                        menuViewModel.saveCartType(context, "dine in")
                        screen = "Dine In"
                    },
                    {
                        //cartType = "takeaway"
                        menuViewModel.saveCartType(context, "takeaway")
                        screen = "Takeaway"
                    })

                "Dine In" -> DineInScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }
                "Takeaway" -> TakeawayScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }
                "view_cart" -> {
                    if (cartType == "dine in")
                        DineInScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }
                    else if (cartType == "takeaway")
                        TakeawayScreen(cartViewModel,orderViewModel,userId) { screen = "view_orders" }

                }
            }
        }
    }
}

