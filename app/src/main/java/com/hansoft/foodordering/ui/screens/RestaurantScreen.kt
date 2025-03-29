package com.hansoft.foodordering.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.FirebaseFirestore
import com.hansoft.foodordering.data.model.Restaurant
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RestaurantScreen() {
    val db = FirebaseFirestore.getInstance()
    val restaurants = remember { mutableStateListOf<Restaurant>() }

    LaunchedEffect(Unit) {
        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    restaurants.add(Restaurant(name, image))
                }
            }
    }

    LazyColumn {
        items(restaurants) { restaurant ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                //elevation = 4.dp
            ) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(restaurant.image),
                        contentDescription = null,
                        modifier = Modifier.height(150.dp)
                    )
                    Text(text = restaurant.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}