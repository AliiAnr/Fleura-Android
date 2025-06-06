import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleura.data.repository.CartRepository
import com.course.fleura.data.repository.HomeRepository
import com.course.fleura.data.repository.ProfileRepository
import com.course.fleura.di.Injection
import com.course.fleura.ui.screen.dashboard.cart.CartViewModel
import com.course.fleura.ui.screen.dashboard.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(private val profileRepository: ProfileRepository, private val homeRepository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ProfileViewModel(profileRepository = profileRepository, homeRepository = homeRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): ProfileViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileViewModelFactory(
                    Injection.provideProfileRepository(context),
                    Injection.provideHomeRepository(context)
                    ).also {
                    INSTANCE = it
                }
            }
    }
}