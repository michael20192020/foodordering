package com.hansoft.foodordering.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hansoft.foodordering.data.model.MenuItem
import com.hansoft.foodordering.utils.CustomTopAppBar
import com.hansoft.foodordering.viewmodel.MenuViewModel
import com.hansoft.foodordering.viewmodel.OrderViewModel


@Composable
fun AdminScreen(menuViewModel: MenuViewModel, orderViewModel: OrderViewModel,
                onBackClick: () -> Unit, modifier: Modifier = Modifier) {

        val navController = rememberNavController()

        NavHost(navController, startDestination = "admin_home",
            modifier = Modifier.padding()) {
            composable("admin_home") {AdminHomeContent(onBackClick,
                onNavigateToAddMenu = { navController.navigate("add_menu") },
                onNavigateToUpdateMenu = { navController.navigate("update_menu")},
                onNavigateToUpdateOrder = { navController.navigate("update_order")}
                )}
            composable("add_menu") { AddMenuItemScreen(menuViewModel,onBackClick = { navController.popBackStack() }) }
            composable("update_menu") { UpdateMenuItemScreen(menuViewModel,onBackClick = { navController.popBackStack() })}
            composable("update_order") { UpdateOrderScreen(orderViewModel,onBackClick = { navController.popBackStack() })}
        }

}

@Composable
fun AdminHomeContent(onBackClick: () -> Unit,modifier: Modifier = Modifier,
    onNavigateToAddMenu: () -> Unit,
    onNavigateToUpdateMenu: () -> Unit,
    onNavigateToUpdateOrder: () -> Unit
) {
    var screen by remember { mutableStateOf("add_menu_item") }
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Admin Screen",
                onNavigationClick = onBackClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Admin Screen",style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToAddMenu) { Text("Add Menu") }

            Button(onClick =  onNavigateToUpdateMenu) { Text("Update Menu") }

            Button(onClick = onNavigateToUpdateOrder) { Text("Update Order") }



        }
    }
}