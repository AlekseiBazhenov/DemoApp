package uz.ucell.feature_refill_balance.mappers

import dagger.hilt.android.scopes.ViewModelScoped
import uz.ucell.networking.network.response.PaymentRecommendationsResponse
import uz.ucell.ui_kit.components.chips.ChipsData
import javax.inject.Inject

@ViewModelScoped
class PaymentRecommendationsMapper @Inject constructor() {

    fun recommendations(data: PaymentRecommendationsResponse): List<ChipsData> {
        val chips = mutableListOf<ChipsData>()
        chips.add(ChipsData(data.main.amount.toString()))
        data.additional.forEach {
            chips.add(ChipsData(it.amount.toString()))
        }
        return chips.toList()
    }
}
