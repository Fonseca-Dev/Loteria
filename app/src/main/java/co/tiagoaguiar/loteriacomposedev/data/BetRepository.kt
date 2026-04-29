package co.tiagoaguiar.loteriacomposedev.data

class BetRepository(private val betDao: BetDao) {

    suspend fun getBetsByType(type: String): List<Bet> {
        return betDao.getNumbersByType(type)
    }

    suspend fun insertBet(bet: Bet) {
        betDao.insert(bet)
    }

    companion object {
        private var instance: BetRepository? = null
        fun getInstance(betDao: BetDao): BetRepository {
            return instance ?: BetRepository(betDao).also {
                instance = it
            }
        }
    }
}