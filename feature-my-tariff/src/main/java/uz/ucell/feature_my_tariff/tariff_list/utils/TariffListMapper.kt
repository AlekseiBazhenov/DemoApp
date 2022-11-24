package uz.ucell.feature_my_tariff.tariff_list.utils

import android.content.Context
import android.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import uz.ucell.feature_my_tariff.R
import uz.ucell.feature_my_tariff.tariff_list.model.Badge
import uz.ucell.feature_my_tariff.tariff_list.model.LimitType
import uz.ucell.feature_my_tariff.tariff_list.model.TariffLimit
import uz.ucell.feature_my_tariff.tariff_list.model.TariffRatePlanPrice
import uz.ucell.feature_my_tariff.tariff_list.ui.Advantage
import uz.ucell.feature_my_tariff.tariff_list.ui.TariffCell
import uz.ucell.networking.network.response.TariffResponse
import javax.inject.Inject

@ViewModelScoped
class TariffListMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun mapDomainToUi(
        responseList: List<TariffResponse>
    ): List<TariffCell> {
        return responseList.map { item ->
            TariffCell(
                item.name,
                badge = mapBadge(item),
                ratePlanPrice = mapRatePlan(item),
                limits = mapLimits(item.includedLimits),
                advantages = mapAdvantages(item.advantages)
            )
        }
    }

    private fun mapRatePlan(response: TariffResponse): TariffRatePlanPrice? {
        return response.ratePlanPrice?.let { ratePlan ->
            TariffRatePlanPrice(
                ratePlan.value,
                ratePlan.unit,
                ratePlan.unitPeriod,
            )
        }
    }

    private fun mapBadge(response: TariffResponse): Badge? {
        return response.badge?.let { badge ->
            Badge(
                badge.title,
                Color.parseColor(badge.color)
            )
        }
    }

    private fun mapLimits(list: List<TariffResponse.IncludedLimit>?): List<TariffLimit>? {
        return list?.map { item ->
            if (item.isUnlimited) {
                TariffLimit.Unlimited(
                    context.getString(R.string.label_unlimited),
                    LimitType.CALLS
                )
            } else {
                TariffLimit.Limited(
                    item.totalValue,
                    item.unit,
                    LimitType.CALLS
                )
            }
        }
    }

    private fun mapAdvantages(list: List<TariffResponse.Advantage>?): List<Advantage>? {
        return list?.map { item ->
            Advantage(
                item.name,
                item.icon
            )
        }
    }
}
