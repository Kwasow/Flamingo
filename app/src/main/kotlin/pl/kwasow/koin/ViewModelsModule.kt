package pl.kwasow.koin

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.kwasow.ui.AppViewModel
import pl.kwasow.ui.screens.home.HomeScreenViewModel
import pl.kwasow.ui.screens.login.LoginScreenViewModel
import pl.kwasow.ui.screens.modules.location.LocationModuleViewModel
import pl.kwasow.ui.screens.modules.memories.MemoriesModuleViewModel
import pl.kwasow.ui.screens.modules.missingyou.MissingYouModuleViewModel
import pl.kwasow.ui.screens.modules.music.MusicModuleViewModel
import pl.kwasow.ui.screens.modules.whishlist.WishlistModuleViewModel
import pl.kwasow.ui.screens.settings.SettingsScreenViewModel
import pl.kwasow.ui.widgets.daystogether.DaysTogetherViewModel
import pl.kwasow.ui.widgets.memories.MemoriesWidgetViewModel
import pl.kwasow.ui.widgets.music.PlaybackWidgetViewModel

val viewModelsModule =
    module {
        viewModel {
            AppViewModel(get(), get())
        }

        viewModel {
            LoginScreenViewModel(get(), get())
        }

        viewModel {
            HomeScreenViewModel(get(), get(), get())
        }

        viewModel {
            LocationModuleViewModel(get(), get(), get())
        }

        viewModel {
            MemoriesModuleViewModel(get())
        }

        viewModel {
            MissingYouModuleViewModel(get(), get())
        }

        viewModel {
            MusicModuleViewModel(get(), get())
        }

        viewModel {
            WishlistModuleViewModel(get(), get(), get())
        }

        viewModel {
            DaysTogetherViewModel(get())
        }

        viewModel {
            MemoriesWidgetViewModel(get())
        }

        viewModel {
            PlaybackWidgetViewModel(get())
        }

        viewModel {
            SettingsScreenViewModel(get(), get(), get(), get(), get())
        }
    }
