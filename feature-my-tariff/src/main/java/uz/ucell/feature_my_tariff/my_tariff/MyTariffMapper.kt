package uz.ucell.feature_my_tariff.my_tariff

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import uz.ucell.feature_my_tariff.R
import uz.ucell.feature_my_tariff.my_tariff.views.Faq
import uz.ucell.feature_my_tariff.my_tariff.views.TariffInfoData
import uz.ucell.feature_my_tariff.my_tariff.views.TariffLimit
import uz.ucell.feature_my_tariff.my_tariff.views.TariffTerm
import uz.ucell.networking.network.response.TariffResponse
import uz.ucell.utils.formatToDate
import javax.inject.Inject

@ViewModelScoped
class MyTariffMapper @Inject constructor(@ApplicationContext val context: Context) {

    fun tariffInfo(data: TariffResponse) = TariffInfoData(
        title = data.name,
        description = data.tariffDescription,
        ratePlanInfo = data.ratePlanPrice?.let { "${it.value} ${it.unit}/${it.unitPeriod}" },
        nextPaymentDate = data.nextChargeDate?.let { formatToDate(it) }
    )

    fun limits(isFinancialBlock: Boolean, data: List<TariffResponse.IncludedLimit>) = data.map {
        TariffLimit(
            name = it.name,
            description = it.limitDescription,
            isDescriptionExpanded = !it.isHiddenDescription,
            remain = if (it.isUnlimited)
                context.getString(R.string.label_unlimited)
            else if (isFinancialBlock)
                "${it.totalValue} ${it.unit}"
            else
                "${it.availableValue} / ${it.totalValue} ${it.unit}",
            totalValue = if (it.isUnlimited || isFinancialBlock) 1f else it.totalValue.toFloat(),
            availableValue = if (it.isUnlimited || isFinancialBlock) 1f else it.availableValue.toFloat()
        )
    }

    fun additionalInfo(isHidden: Boolean, data: List<TariffResponse.AdditionalTariffInfo>) =
        TariffTerm(
            isExpanded = !isHidden,
            items = data.map {
                TariffTerm.Item(
                    name = it.name,
                    description = it.description,
                    value = it.value,
                    isDescriptionExpanded = !it.isHiddenDescription
                )
            }
        )

    fun faq(faq: List<TariffResponse.Faq>) = faq.map {
        Faq(question = it.question, answer = it.answer)
    }
}
