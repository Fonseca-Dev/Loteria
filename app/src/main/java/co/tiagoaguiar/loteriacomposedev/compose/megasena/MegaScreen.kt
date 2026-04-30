package co.tiagoaguiar.loteriacomposedev.compose.megasena

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.tiagoaguiar.loteriacomposedev.R
import co.tiagoaguiar.loteriacomposedev.ui.component.AutoTextDropDown
import co.tiagoaguiar.loteriacomposedev.ui.component.LoItemType
import co.tiagoaguiar.loteriacomposedev.ui.component.LoNumberTextField
import co.tiagoaguiar.loteriacomposedev.ui.theme.LoteriaTheme
import co.tiagoaguiar.loteriacomposedev.viewmodels.BetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MegaScreen(
    onBackClick: () -> Unit,
    betViewModel: BetViewModel = viewModel(factory = BetViewModel.Factory),
    onMenuClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Apostar") },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onMenuClick("megasena")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.List,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
        ) { contentPadding ->
            MegaSenaContentScreen(
                modifier = Modifier
                    .padding(top = contentPadding.calculateTopPadding()),
                betViewModel = betViewModel
            )
            { message ->
                scope.launch {
                    snackBarHostState.showSnackbar(message)
                }
            }
        }

        Box {
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackBarHostState
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MegaSenaContentScreen(
    modifier: Modifier = Modifier,
    betViewModel: BetViewModel,
    error:(String) -> Unit
) {


    val result = betViewModel.result.observeAsState("").value
    val keyBoardController = LocalSoftwareKeyboardController.current

    val qtdeNumbers = betViewModel.qtdeNumbers
    val qtdeBets = betViewModel.qtdeBets

    val errorBets = stringResource(id = R.string.error_bets)
    val errorNumbers = stringResource(R.string.errorNumbers)
    val showAlertDialog = betViewModel.showAlertDialog.observeAsState(false).value
    val rules = stringArrayResource(R.array.array_bet_rules)
    var selectedItem by remember { mutableStateOf(rules.first()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState),
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
            betViewModel.updateQtdeNumbers(it)
        }

        LoNumberTextField(
            value = qtdeBets,
            label = R.string.bets,
            placeholder = R.string.bets_quantity,
            imeAction = ImeAction.Done,
        ) {
            betViewModel.updateQtdeBets(it)
        }

        Column(
            modifier = Modifier.width(280.dp)
        ) {
            AutoTextDropDown(
                label = stringResource(R.string.bet_rule),
                initial = rules.first(),
                onItemChanged = { selectedItem = it },
                list = rules.toList()
            )
        }

        OutlinedButton(
            onClick = {
                if (qtdeBets.toInt() !in 1..10) {
                    error(errorBets)
                } else if (qtdeNumbers.toInt() !in 6..15) {
                    error(errorNumbers)
                } else {
                    val rule = rules.indexOf(selectedItem)
                    betViewModel.updateNumbers(rule)
                    keyBoardController?.hide()
                }
            },
            enabled = qtdeBets.isNotEmpty() && qtdeNumbers.isNotEmpty()
        ) {
            Text(stringResource(R.string.bets_generate))
        }

        Text(result)
    }

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    betViewModel.dismissAlert()
                }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    betViewModel.saveBet("megasena")
                    betViewModel.dismissAlert()
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


@Preview(showBackground = true)
@Composable
fun MegaPreview() {
    LoteriaTheme {
        MegaScreen(onBackClick = {}) {}
    }
}
