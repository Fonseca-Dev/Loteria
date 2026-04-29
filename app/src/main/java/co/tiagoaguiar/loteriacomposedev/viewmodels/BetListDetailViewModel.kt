package co.tiagoaguiar.loteriacomposedev.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import co.tiagoaguiar.loteriacomposedev.App
import co.tiagoaguiar.loteriacomposedev.data.Bet
import co.tiagoaguiar.loteriacomposedev.data.BetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BetListDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val betRepository: BetRepository
) : ViewModel() {

    private val _bets = MutableStateFlow<List<Bet>>(emptyList())
    val bets = _bets.asStateFlow()

    init {
        //Atividades que precisam ser feitas em paralelo
        viewModelScope.launch {
            val type = savedStateHandle.get<String>("type") ?: throw Exception("Tipo não encontrado!")
            val result = betRepository.getBetsByType(type)
            _bets.value = result
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY]
                val dao = (application as App).db.betDao()
                val repository = BetRepository.getInstance(dao)
                val savedStateHandle = extras.createSavedStateHandle()
                return BetListDetailViewModel(savedStateHandle,repository) as T
            }
        }
    }
}