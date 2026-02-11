package com.lukecourt.starjournal.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lukecourt.starjournal.data.getLastEdited
import com.lukecourt.starjournal.ui.theme.SCJournalComposeTheme
import com.lukecourt.starjournal.viewModels.MissionsViewModel

// Data classes for our dashboard items
data class SummaryCardData(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val contentDescription: String? = null
)

data class ActivityItem(
    val id: String,
    val description: String,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, missionsVM: MissionsViewModel = viewModel()) {


    val summaryCards = listOf(
        SummaryCardData("Total Missions", missionsVM.getMissionsSize().toString(), Icons.AutoMirrored.Filled.List, "Total Mission entries"),
        SummaryCardData("Completed Missions", missionsVM.getCompletedMissionsTotal().toString(), Icons.Filled.CheckCircle, "Tasks marked as complete"),
        SummaryCardData("Pending Missions", missionsVM.getPendingMissionsTotal().toString(), Icons.AutoMirrored.Outlined.Send, "Items awaiting review"),
        SummaryCardData("aUEC Earned", missionsVM.gettotalEarned().toString(), Icons.Filled.AccountCircle, "Average mood rating")
    )

    val activities: MutableList<ActivityItem> = mutableListOf()

    missionsVM.getMissions().sortedByDescending { it.getLastEdited() }.forEach{
        activities.add(ActivityItem(
            id = it.id,
            description = it.title,
            timestamp = it.getLastEdited().toString()
        ))
    }

    val recentActivities = activities.takeLast(5) // Get the last 5 activities



    SCJournalComposeTheme {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards Section
            item {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                // Using a FlowRow for summary cards to wrap if they don't fit
                // For a simple 2x2 grid, you can also use nested Rows and Columns
                // or a custom layout.
                // For this basic example, let's just do two cards per row.
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DashboardSummaryCard(
                            modifier = Modifier.weight(1f),
                            data = summaryCards[0]
                        )
                        DashboardSummaryCard(
                            modifier = Modifier.weight(1f),
                            data = summaryCards[1]
                        )
                    }
                    if (summaryCards.size > 2) { // Handle cases with more cards
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            DashboardSummaryCard(
                                modifier = Modifier.weight(1f),
                                data = summaryCards[2]
                            )
                            if (summaryCards.size > 3) {
                                DashboardSummaryCard(
                                    modifier = Modifier.weight(1f),
                                    data = summaryCards[3]
                                )
                            } else {
                                Spacer(Modifier.weight(1f)) // Fill space if odd number
                            }
                        }
                    }
                }
            }

            // Recent Activity Section
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            items(recentActivities, key = { it.id }) { activity ->
                ActivityListItem(activity = activity)
            }
        }
    } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardSummaryCard(
    modifier: Modifier = Modifier,
    data: SummaryCardData
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.contentDescription,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = data.value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ActivityListItem(activity: ActivityItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = activity.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@PreviewLightDark
fun HomeScreenPreview() {
    SCJournalComposeTheme {
        HomeScreen(navController = rememberNavController())
    }
}
