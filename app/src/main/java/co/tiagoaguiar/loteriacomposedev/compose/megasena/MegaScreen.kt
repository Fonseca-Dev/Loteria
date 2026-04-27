package co.tiagoaguiar.loteriacomposedev.compose.megasena

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.tiagoaguiar.loteriacomposedev.R
import co.tiagoaguiar.loteriacomposedev.ui.component.LoItemType
import co.tiagoaguiar.loteriacomposedev.ui.component.LoNumberTextField
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MegaScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        var qtdeNumbers by remember { mutableStateOf("") }

        var qtdeBets by remember { mutableStateOf("") }

        var result by remember { mutableStateOf("") }

        val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
        val scope = rememberCoroutineScope()
        val keyBoardController = LocalSoftwareKeyboardController.current
        val errorBets = stringResource(id = R.string.error_bets)
        val errorNumbers = stringResource(R.string.errorNumbers)


        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoItemType("Mega Sena")

            Text(
                text = stringResource(R.string.announcement),
                modifier = Modifier
                    .padding(8.dp)
            )

            LoNumberTextField(
                value = qtdeNumbers,
                label = R.string.mega_rule,
                placeholder = R.string.quantity,
                imeAction = ImeAction.Next
            ) {
                if (it.length < 3) {
                    qtdeNumbers = validateInput(it)
                }
            }

            LoNumberTextField(
                value = qtdeBets,
                label = R.string.bets,
                placeholder = R.string.bets_quantity,
                imeAction = ImeAction.Done,
            ) {
                if (it.length < 3) {
                    qtdeBets = validateInput(it)
                }
            }

            OutlinedButton(
                onClick = {
                    if (qtdeBets.toInt() !in 1..10) {
                        scope.launch {
                            snackBarHostState.showSnackbar(errorBets)
                        }
                    } else if (qtdeNumbers.toInt() !in 6..15) {
                        scope.launch {
                            snackBarHostState.showSnackbar(errorNumbers)
                        }
                    } else {
                        result = ""
                        for (i in 1..qtdeBets.toInt()) {
                            result += "[$i] "
                            result += generateRandomNumbers(qtdeNumbers.toInt())
                            result += "\n\n"
                        }

                        keyBoardController?.hide()

                    }
                },
                enabled = qtdeBets.isNotEmpty() && qtdeNumbers.isNotEmpty()
            ) {
                Text(stringResource(R.string.bets_generate))
            }

            Text(result)
        }

        Box {
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackBarHostState
            )
        }
    }
}

private fun validateInput(input: String): String {
    return input.filter { it in "012346789" }
}

private fun generateRandomNumbers(qtde: Int): String {
    val numbers = mutableSetOf<Int>()
    while (numbers.size <= qtde) {
        val n = Random.nextInt(1, 61)
        numbers.add(n)
    }
    return numbers.joinToString(" - ")
}

@Preview(showBackground = true)
@Composable
fun MegaPreview() {
    LoteriaTheme {
        MegaScreen()
    }
}
