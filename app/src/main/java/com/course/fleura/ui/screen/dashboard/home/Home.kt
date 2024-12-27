package com.course.fleura.ui.screen.dashboard.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.components.SearchBar
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.FleuraTheme

@Composable
fun Home(
    onSnackClick: (Long, String) -> Unit,
    modifier: Modifier
) {
    // call API in this section
    Home()
}

@Composable
private fun Home(
    modifier: Modifier = Modifier
) {

    var textState = ""
    FleuraSurface (
        modifier = modifier.fillMaxSize()
    ){
        Box (
            modifier = Modifier.fillMaxSize()
        ){
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                Header()
                Spacer(modifier = Modifier.height(12.dp))
                SearchBar(
                    query = textState,
                    onQueryChange = {
                        textState = it
                    },
                    onSearch = {
                        // call API
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                        append("Halo, ")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("name!")
                    }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "What are you looking for right now?",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
        }
        IconButton(onClick = { /* Handle notification click */ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
                contentDescription = "Notification",
                tint = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingHeaderPreview() {
    FleuraTheme {
        Header()
    }
}
