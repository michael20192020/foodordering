package com.hansoft.foodordering.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.MenuItem
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextLayoutResult
import com.google.firebase.Timestamp
import com.hansoft.foodordering.data.model.CartItem
import com.hansoft.foodordering.data.model.Order
import com.hansoft.foodordering.viewmodel.CartViewModel
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel

@Composable
fun MenuScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel,
               cartViewModel: CartViewModel, userId: String, onDineIn: () -> Unit,onTakeaway:() -> Unit) {
    // var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }


    val menuItems by menuViewModel.menuItems.collectAsState()
    val selectedItems by menuViewModel.selectedItems.collectAsState()
    var message by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("bacon") }
    val categories = menuItems.map { it.category.lowercase() }.distinct().sorted()
    var quantity by remember { mutableStateOf(1) }
    //val categories by menuViewModel.categoryItems.collectAsState()

   // Log.d("aaa", "MenuScreen: $categories")



  //  LaunchedEffect(selectedItems) {
  //      Log.d("aaa", "Current selected items: $selectedItems")
  //  }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Menu",fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedCategory = category },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (selectedCategory == category) Color.Blue else Color.Gray),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCategory == category) Color(0xFFE3F2FD) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier
                                    //.fillMaxWidth()
                                    .clickable { selectedCategory = category }
                                    .padding(),
                                fontSize = 20.sp,
                                fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedCategory == category) Color.Blue else Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }


            LazyColumn(
                modifier = Modifier
                    .weight(2f) // This allows LazyColumn to take available space but not push the button out
                    .padding(4.dp)
            )
            {
                val filteredItems = menuItems.filter { it.category.lowercase() == selectedCategory }
                items(filteredItems) { item ->
                    MenuItemCard(item, menuViewModel::toggleSelection, selectedItems.contains(item))
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

        }
    //    Log.d("aaa", "Menu screen : Selected items: ${selectedItems}")
    //    Log.d("aaa", "Menu screen : Selected items: ${selectedItems.isNotEmpty()}")
        if (selectedItems.isNotEmpty()) {

            Button(
                onClick = {
                    /*
                   // var newid = "0"
                    orderViewModel.getNewID { count ->
                        // Use the `count` string here, for example, to generate a new order ID
                        Log.d("aaa", "The new order ID is: $count")
                     //   newid = count

                        val order = Order(
                            orderId = count,
                            userId = userId,
                            //items = selectedItems.map { it.name + "   $" + it.price.toString() + " \n" },
                            items = selectedItems.map { "${it.name} x${it.quantity}" + "   $" + (it.quantity * it.price).toString() + " \n" },
                            totalPrice = selectedItems.sumOf { it.price },
                            timestamp = Timestamp.now()
                        )

                        orderViewModel.placeOrderNew(
                            order,
                            onSuccess = {
                                message = "Order placed successfully!"
                                onProceedToOrder()
                                        },
                            onError = { message = "Failed to place order: ${it.message}" })



                    }

                     */
                    selectedItems.forEachIndexed { index, item ->
                        val cartItem = CartItem(id = index + 1, name = item.name, price = item.price, quantity = 1, imageUrl = item.imageUrl)
                        cartViewModel.addToCart(cartItem)
                    }
                    onDineIn()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Dine In (${selectedItems.size} items)")
            }

            Button(
                onClick = {

                    selectedItems.forEachIndexed { index, item ->
                        val cartItem = CartItem(id = index + 1, name = item.name, price = item.price, quantity = 1, imageUrl = item.imageUrl)
                        cartViewModel.addToCart(cartItem)
                    }
                    onTakeaway()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Takeaway (${selectedItems.size} items)")
            }


            if (message.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(message, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItem, onItemSelected: (MenuItem) -> Unit,isSelected: Boolean) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onItemSelected(item) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {


            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = item.name, fontWeight = FontWeight.Bold)
            Text(text = item.description)
            Text(text = "$${item.price}",color = Color.Gray)
            Checkbox(checked = isSelected, onCheckedChange = { onItemSelected(item) })

        }



        /*
           Row(modifier = Modifier.padding(2.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween) {
           AsyncImage(
               model = item.imageUrl,
               contentDescription = item.name,
               modifier = Modifier.size(64.dp)
           )
               Column {
           Text(text = item.name, fontWeight = FontWeight.Bold)
           Text(text = item.description)
           Text(text = "$${item.price}", color = Color.Gray)

               }
               Checkbox(checked = isSelected, onCheckedChange = { onItemSelected(item) })
           }

         */



        /*
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "$${item.price}", fontSize = 16.sp, color = Color.Gray)
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

         */
    }
}









