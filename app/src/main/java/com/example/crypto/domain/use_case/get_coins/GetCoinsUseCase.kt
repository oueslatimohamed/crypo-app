package com.example.crypto.domain.use_case.get_coins

import com.example.crypto.data.remote.dto.toCoin
import com.example.crypto.domain.model.Coin
import com.example.crypto.domain.repository.CoinRepository
import com.example.crypto.shared.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    private val _coinsStateFlow = MutableStateFlow<Resource<List<Coin>>>(Resource.Loading<List<Coin>>())
    val coinsStateFlow = _coinsStateFlow.asStateFlow()
    suspend fun  invoke() {
        try {
            _coinsStateFlow.value = Resource.Loading<List<Coin>>()
            val coins = repository.getCoins().map { it.toCoin() }
            println(coins)
            _coinsStateFlow.value = Resource.Success<List<Coin>>(coins)
        } catch (e: HttpException) {
            _coinsStateFlow.value = Resource.Error<List<Coin>>(e.localizedMessage ?: "Error")
        } catch (e: IOException) {
            _coinsStateFlow.value = Resource.Error<List<Coin>>("No internet connection")
        }
    }
    /*operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success<List<Coin>>(coins))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "Error"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Coin>>("No internet connexion"))
        }
    }*/
}