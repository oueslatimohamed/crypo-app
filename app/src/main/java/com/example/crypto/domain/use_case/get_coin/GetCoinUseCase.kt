package com.example.crypto.domain.use_case.get_coin

import com.example.crypto.data.remote.dto.toCoin
import com.example.crypto.data.remote.dto.toCoinDetail
import com.example.crypto.domain.model.Coin
import com.example.crypto.domain.model.CoinDetail
import com.example.crypto.domain.repository.CoinRepository
import com.example.crypto.shared.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coin = repository.getCoin(coinId).toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        } catch (e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "Error"))
        } catch (e: IOException) {
            emit(Resource.Error<CoinDetail>("No internet connexion"))
        }
    }
}