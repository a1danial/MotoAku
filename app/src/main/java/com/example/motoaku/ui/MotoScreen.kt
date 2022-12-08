package com.example.motoaku.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.motoaku.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoScreen(
    vm: ViewModel = hiltViewModel()
) {
    LaunchedEffect(vm.MotoList) {
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 30.dp)
    ) {
        items(
            items = vm.MotoList,
        ) { moto -> SelectedMotoCard(moto) }
    }
}

/*
@ExperimentalMaterial3Api
@Composable
fun MotoCard(
    moto: Motorcycle,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (moto.imageRes != null) {
                Image(
                    painter = painterResource(id = moto.imageRes.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column {
                Text(text = moto.brand,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = moto.model,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

}*/
