package co.tiagoaguiar.loteriacomposedev.compose.megasena

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.tiagoaguiar.loteriacomposedev.App
import co.tiagoaguiar.loteriacomposedev.R
import co.tiagoaguiar.loteriacomposedev.data.Bet
import co.tiagoaguiar.loteriacomposedev.ui.component.LoItemType
import co.tiagoaguiar.loteriacomposedev.ui.component.LoNumberTextField
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MegaScreen(onClick: (String) -> Unit) {

    val resultsToSave = mutableListOf<String>()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val db = (LocalContext.current.applicationContext as App).db

        var qtdeNumbers by remember { mutableStateOf("") }

        var qtdeBets by remember { mutableStateOf("") }

        var result by remember { mutableStateOf("") }

        val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
        val scope = rememberCoroutineScope()
        val keyBoardController = LocalSoftwareKeyboardController.current
        val errorBets = stringResource(id = R.string.error_bets)
        val errorNumbers = stringResource(R.string.errorNumbers)
        var showAlertDialog by remember { mutableStateOf(false) }

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
                        resultsToSave.clear()
                        for (i in 1..qtdeBets.toInt()) {
                            val res = generateRandomNumbers(qtdeNumbers.toInt())
                            resultsToSave.add(res)
                            result += "[$i] "
                            result += res
                            result += "\n\n"
                        }

                        keyBoardController?.hide()

                        showAlertDialog = true

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

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = {
                        showAlertDialog = false
                        onClick("megasena")
                    }) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        Thread {
                            for (res in resultsToSave) {
                                val bet = Bet(type = "megasena", numbers = res)
                                db.betDao().insert(bet)
                            }
                        }.start()
                        showAlertDialog = false
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                },
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                text = {
                    Text(text = stringResource(R.string.good_luck))
                }
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
        MegaScreen(onClick = { })
    }
}
