package com.pgcoding.githubrepoexplorer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pgcoding.githubrepoexplorer.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(letterSpacing = 0.5.sp),
        )
        Button(
            modifier = Modifier
                .clip(RoundedCornerShape(100f))
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorResource(R.color.colorBlue),
                contentColor = Color.White
            ),
            onClick = onRefresh
        ) {
            Text(
                text = stringResource(R.string.retry).uppercase(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}