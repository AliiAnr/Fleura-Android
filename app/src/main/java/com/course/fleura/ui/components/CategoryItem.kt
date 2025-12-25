package com.course.fleura.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.categ

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onCategoryClick: (String) -> Unit
) {
    FleuraSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    onCategoryClick(category.name)
                }
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
//                    .background(if (index == selectedIndex) Color(0xFFFFE4E1) else Color(0xFFFCE4EC)),
                    .background(categ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = category.image),
                    contentDescription = category.name,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.width(70.dp),
                text = category.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun CategoryItemPreview() {
//    CategoryItem(
//        category = Category(1, "All Variants", R.drawable.category_1),
//        onCategoryClick = {
//            id, name -> println("CategoryItemPreview: $id, $name")
//        }
//    )
//}