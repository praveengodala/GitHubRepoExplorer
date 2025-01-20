package com.pgcoding.githubrepoexplorer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.ui.theme.GitHubExplorerTheme

@Composable
fun RepositoryItem(
    modifier: Modifier = Modifier,
    repoName: String,
    stars: Int,
    topContributorName: String?
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Row for repository icon and name
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.Folder,
                    contentDescription = null,
                    tint = colorResource(R.color.colorBlue)
                )
                Text(
                    text = repoName,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // Row for star count icon and name
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colorResource(R.color.colorDarkYellow)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stars.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Row for top contributor count icon and name only when available
        topContributorName?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = colorResource(R.color.colorGray1)
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(letterSpacing = 0.5.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryItemPreview() {
    GitHubExplorerTheme {
        RepositoryItem(
            modifier = Modifier.padding(16.dp),
            repoName = "Repository Name",
            stars = 354123,
            topContributorName = "Top Contributor"
        )
    }
}