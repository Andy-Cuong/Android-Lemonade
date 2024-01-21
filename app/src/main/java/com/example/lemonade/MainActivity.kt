package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LemonContainer()
                }
            }
        }
    }
}

enum class LemonAppState {
    TREE,
    SQUEEZE,
    DRINK,
    RESTART
}

@Composable
fun LemonContainer(modifier: Modifier = Modifier) {
    var appState by remember { mutableStateOf(LemonAppState.TREE) }
    val lemonImage = when (appState) {
        LemonAppState.TREE -> R.drawable.lemon_tree
        LemonAppState.SQUEEZE -> R.drawable.lemon_squeeze
        LemonAppState.DRINK -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }
    val instruction = when (appState) {
        LemonAppState.TREE -> R.string.lemon_tree_instruction
        LemonAppState.SQUEEZE -> R.string.lemon_squeeze_instruction
        LemonAppState.DRINK -> R.string.lemon_drink_instruction
        else -> R.string.lemon_restart_instruction
    }
    val imageDes = when (appState) {
        LemonAppState.TREE -> R.string.lemon_tree_content_description
        LemonAppState.SQUEEZE -> R.string.lemon_content_description
        LemonAppState.DRINK -> R.string.lemon_glass_content_description
        else -> R.string.empty_glass_content_description
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var rangeMax by remember { mutableIntStateOf(5) }

        Button(
            onClick = { // Change the app state on click
                appState = when (appState) {
                    LemonAppState.TREE -> LemonAppState.SQUEEZE
                    // Take a random number between 1 and rangeMax, only proceed to the next
                    // app state if the number is 1, otherwise reduce rangeMax by 1.
                    // Make sure the lemon is squeezed at max 5 times before proceeding.
                    LemonAppState.SQUEEZE ->
                        if ((1..rangeMax--).random() == 1) {
                            // Reset the rangeMax before proceeding
                            rangeMax = 5
                            LemonAppState.DRINK
                        } else LemonAppState.SQUEEZE

                    LemonAppState.DRINK -> LemonAppState.RESTART
                    else -> LemonAppState.TREE
                }
            },
            shape = RoundedCornerShape(size = 50.dp), // Define the button shape
            border = BorderStroke( // Draw the border for the button
                width = 5.dp,
                color = Color(0xFF20EE7F)
            )
        ) {
            Image(
                painter = painterResource(id = lemonImage),
                contentDescription = stringResource(id = imageDes),
                modifier = Modifier.padding(24.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = instruction),
            fontSize = 20.sp
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LemonadePreview() {
    LemonadeTheme {
        LemonContainer()
    }
}