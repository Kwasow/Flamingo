package pl.kwasow.ui.widgets.daystogether

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kwasow.managers.UserManager
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DaysTogetherViewModel(
    userManager: UserManager,
) : ViewModel() {
    // ====== Fields
    val daysTogether: Flow<Long?> =
        userManager.userFlow.map { user ->
            val anniversaryDate = user?.coupleDetails?.getLocalAnniversaryDate() ?: return@map null
            ChronoUnit.DAYS.between(anniversaryDate, LocalDate.now())
        }
}
