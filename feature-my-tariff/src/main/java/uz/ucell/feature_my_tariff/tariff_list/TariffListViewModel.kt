package uz.ucell.feature_my_tariff.tariff_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.feature_my_tariff.tariff_list.model.TariffListSideEffect
import uz.ucell.feature_my_tariff.tariff_list.model.TariffListState
import uz.ucell.feature_my_tariff.tariff_list.ui.TariffCell
import uz.ucell.feature_my_tariff.tariff_list.utils.TariffListMapper
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.INTERNAL_ERROR
import javax.inject.Inject

@HiltViewModel
class TariffListViewModel @Inject constructor(
    private val api: NetworkingInterface,
    private val mapper: TariffListMapper,
) : ContainerHost<TariffListState, TariffListSideEffect>, ViewModel() {
    override val container: Container<TariffListState, TariffListSideEffect> =
        container(TariffListState())

    init {
        getTariffListInfo()
    }

    private fun getTariffListInfo() = intent {
        loading(true)

        when (val response = api.getAvailableTariffs()) {
            is ApiResponse.Error -> {
                response.error?.let { notNullError -> processError(error = notNullError) }
            }
            is ApiResponse.Success -> {
                val tariffList = response.value
                if (tariffList.isEmpty()) {
                    error(true)
                } else {
                    content(mapper.mapDomainToUi(tariffList))
                }
            }
        }
        loading(false)
    }

    private suspend fun SimpleSyntax<TariffListState, TariffListSideEffect>.loading(
        loading: Boolean
    ) {
        reduce { state.copy(isLoading = loading) }
    }

    private suspend fun SimpleSyntax<TariffListState, TariffListSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            INTERNAL_ERROR -> error(true)
            else -> postSideEffect(TariffListSideEffect.Error(error.message, error.code))
        }
    }

    private suspend fun SimpleSyntax<TariffListState, TariffListSideEffect>.content(
        list: List<TariffCell>
    ) = reduce { state.copy(isError = false, tariffCells = list) }

    private suspend fun SimpleSyntax<TariffListState, TariffListSideEffect>.error(
        show: Boolean
    ) = reduce { state.copy(isError = show) }
}
