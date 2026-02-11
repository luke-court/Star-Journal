package com.lukecourt.starjournal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukecourt.starjournal.data.CargoItem

@Composable
fun CargoItemDisplay(cargoItem: CargoItem) {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),

    ) {
        Row (
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(cargoItem.type)
            }
            Column {
                if (cargoItem.quantity == 0) {
                    Text("Quantity Not Specified")
                } else {
                    Text("Quantity: " + cargoItem.quantity.toString() + " SCU")
                }


            }

        }
        Row (
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(cargoItem.origin)
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Forward"
                )

            }
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(cargoItem.destination)
            }

        }
    }
}