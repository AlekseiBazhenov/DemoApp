package uz.ucell.appmetrica.api.useCase

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.AppMetricProfile
import uz.ucell.appmetrica.utils.MetricMapper
import uz.ucell.appmetrica.utils.ProfileMapper
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import javax.inject.Inject

interface SendProfileMetricUseCase {
    suspend fun sendProfileMetric()
}

@ViewModelScoped
class SendProfileMetricUseCaseImpl @Inject constructor(
    private val api: NetworkingInterface,
    private val profileMetric: AppMetricProfile,
    private val metricMapper: MetricMapper,
    private val coreStorage: CoreStorage,
    private val profileMapper: ProfileMapper
) : SendProfileMetricUseCase {
    override suspend fun sendProfileMetric() {
        when (val response = api.getUserProfile()) {
            is ApiResponse.Error -> {}
            is ApiResponse.Success -> {
                profileMetric.sendProfileMetric(
                    gender = metricMapper.mapGender(response.value.personal.gender),
                    age = metricMapper.mapAge(response.value.personal.birthday),
                    ratePlanId = response.value.ratePlanId,
                    lang = coreStorage.getSelectedLanguage().first(),
                    auth = true,
                    linkedCard = response.value.hasLinkedCards,
                    userType = metricMapper.mapUserType(response.value.segment),
                    push = false,
                    statusId = response.value.lcStatus
                )

                coreStorage.setUserProfile(
                    profileMapper.mapResponseToDomain(response.value)
                )
            }
        }
    }
}
