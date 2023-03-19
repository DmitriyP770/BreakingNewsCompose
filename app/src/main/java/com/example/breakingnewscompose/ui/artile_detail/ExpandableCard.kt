package com.example.breakingnewscompose.ui.artile_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.breakingnewscompose.ui.theme.Shapes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(

) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Card(modifier = Modifier.animateContentSize(
        animationSpec = tween(
            durationMillis = 300 ,
            easing = LinearOutSlowInEasing
        )
    ) ,
        shape = Shapes.medium ,
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "TEXT" , fontWeight = FontWeight.Bold , modifier = Modifier.weight(6f))
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotationState) ,
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown ,
                        contentDescription = "Drop Down Error" ,
                    )
                }
            }

        }
    }
}