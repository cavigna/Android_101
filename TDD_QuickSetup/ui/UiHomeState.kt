package com.example.myapplication.ui.state

import com.example.myapplication.model.models.Coins

sealed class UiHomeState{
    object Loading: UiHomeState()
    data class Success( val listCoins: List<Coins>): UiHomeState()

}
