package uz.ucell.feature_my_tariff.my_tariff

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.feature_my_tariff.my_tariff.contract.MyTariffSideEffect
import uz.ucell.feature_my_tariff.my_tariff.contract.MyTariffState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.INTERNAL_ERROR
import javax.inject.Inject

@HiltViewModel
class MyTariffViewModel @Inject constructor(
    private val api: NetworkingInterface,
    private val mapper: MyTariffMapper
) : ContainerHost<MyTariffState, MyTariffSideEffect>, ViewModel() {

    override val container =
        container<MyTariffState, MyTariffSideEffect>(MyTariffState())

    init {
        getMyTariff()
    }

    fun onRetryRequest() {
        getMyTariff()
    }

    private fun getMyTariff() = intent {
        loading(true)
        when (val response = api.myTariff()) {
            is ApiResponse.Success -> {
                showError(false)
                val data = response.value
                reduce {
                    state.copy(
                        isFinancialBlock = data.isFinancialBlock,
                        tariffInfo = mapper.tariffInfo(data),
                        tariffLimits = data.includedLimits?.let {
                            mapper.limits(data.isFinancialBlock, it)
                        },
                        pricesOverLimit = mapper.additionalInfo(
                            data.pricesOverLimit.isHidden,
                            data.pricesOverLimit.prices
                        ),
                        internationalCommunication = data.internationalCommunication?.let {
                            mapper.additionalInfo(it.isHidden, it.communications)
                        },
                        faq = data.faq?.let { mapper.faq(it) },
                        tariffInfoLink = data.externalInfo?.pdfUrl
                            ?: data.externalInfo?.websiteLink
                            ?: ""
                    )
                }
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private suspend fun SimpleSyntax<MyTariffState, MyTariffSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<MyTariffState, MyTariffSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            INTERNAL_ERROR -> showError(true)
            else -> postSideEffect(MyTariffSideEffect.ShowError(error.message, error.code))
        }
    }

    private suspend fun SimpleSyntax<MyTariffState, MyTariffSideEffect>.showError(
        show: Boolean
    ) {
        reduce {
            state.copy(isServerError = show)
        }
    }
}
