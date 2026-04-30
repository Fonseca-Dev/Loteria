package co.tiagoaguiar.loteriacomposedev.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import co.tiagoaguiar.loteriacomposedev.App
import co.tiagoaguiar.loteriacomposedev.data.Bet
import co.tiagoaguiar.loteriacomposedev.data.BetRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class BetViewModel(
    private val betRepository: BetRepository
) : ViewModel() {

    // 1. USANDO COMPOSE STATE
    var qtdeNumbers by mutableStateOf("")
        private set
    var qtdeBets by mutableStateOf("")
        private set

    // 2. USANDO LIVE DATA
    private var _result = MutableLiveData("")
    val result: LiveData<String>
        get() = _result

    private var _showAlertDialog = MutableLiveData(false)
    val showAlertDialog: LiveData<Boolean>
        get() = _showAlertDialog

    val resultsToSave = mutableListOf<String>()


    fun updateQtdeNumbers(newNumber: String) {
        if (newNumber.length < 3) {
            qtdeNumbers = validateInput(newNumber)
        }
    }

    fun updateQtdeBets(newBet: String) {
        if (newBet.length < 3) {
            qtdeBets = validateInput(newBet)
        }
    }

    fun updateNumbers(rule: Int) {
        _result.value = ""
        resultsToSave.clear()

        for (i in 1..qtdeBets.toInt()) {
            val res = generateRandomNumbers(qtdeNumbers.toInt(), rule)
            resultsToSave.add(res)
            _result.value += "[$i] "
            _result.value += res
            _result.value += "\n\n"
        }

        _showAlertDialog.value = true
    }

    fun saveBet(type: String) {
        viewModelScope.launch {
            for (bet in resultsToSave) {
                val bet = Bet(type = type, numbers = bet)
                betRepository.insertBet(bet)
            }
        }
    }

    fun dismissAlert() {
        _showAlertDialog.value = false
    }

    private fun validateInput(input: String): String {
        return input.filter { it in "012346789" }
    }

    private fun generateRandomNumbers(qtde: Int, rule: Int): String {
        val numbers = mutableSetOf<Int>()
        while (numbers.size <= qtde) {
            val n = Random.nextInt(1, 61)
            if (rule == 1) {
                if (n % 2 != 0) continue
            } else if (rule == 2) {
                if (n % 2 == 0) continue
            }
            numbers.add(n)
        }
        return numbers.joinToString(" - ")
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY]
                val dao = (application as App).db.betDao()
                val repository = BetRepository.getInstance(dao)
                return BetViewModel(repository) as T
            }
        }
    }
}