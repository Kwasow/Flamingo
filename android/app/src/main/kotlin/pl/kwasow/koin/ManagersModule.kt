package pl.kwasow.koin

import org.koin.dsl.module
import pl.kwasow.managers.AudioManager
import pl.kwasow.managers.AudioManagerImpl
import pl.kwasow.managers.FlamingoDownloadManager
import pl.kwasow.managers.FlamingoDownloadManagerImpl
import pl.kwasow.managers.LocationManager
import pl.kwasow.managers.LocationManagerImpl
import pl.kwasow.managers.MemoriesManager
import pl.kwasow.managers.MemoriesManagerImpl
import pl.kwasow.managers.MessagingManager
import pl.kwasow.managers.MessagingManagerImpl
import pl.kwasow.managers.NotificationManager
import pl.kwasow.managers.NotificationManagerImpl
import pl.kwasow.managers.PermissionManager
import pl.kwasow.managers.PermissionManagerImpl
import pl.kwasow.managers.PlaybackManager
import pl.kwasow.managers.PlaybackManagerImpl
import pl.kwasow.managers.RequestManager
import pl.kwasow.managers.RequestManagerImpl
import pl.kwasow.managers.SettingsManager
import pl.kwasow.managers.SettingsManagerImpl
import pl.kwasow.managers.SystemManager
import pl.kwasow.managers.SystemManagerImpl
import pl.kwasow.managers.TokenManager
import pl.kwasow.managers.TokenManagerImpl
import pl.kwasow.managers.UserManager
import pl.kwasow.managers.UserManagerImpl
import pl.kwasow.managers.WishlistManager
import pl.kwasow.managers.WishlistManagerImpl

val managersModule =
    module {
        single<AudioManager> {
            AudioManagerImpl(get(), get())
        }

        single<FlamingoDownloadManager> {
            FlamingoDownloadManagerImpl(get(), get())
        }

        single<LocationManager> {
            LocationManagerImpl(get(), get(), get())
        }

        single<MemoriesManager> {
            MemoriesManagerImpl(get(), get())
        }

        single<MessagingManager> {
            MessagingManagerImpl(get(), get())
        }

        single<NotificationManager> {
            NotificationManagerImpl(get())
        }

        single<PermissionManager> {
            PermissionManagerImpl(get())
        }

        single<PlaybackManager> {
            PlaybackManagerImpl(get(), get())
        }

        single<RequestManager> {
            RequestManagerImpl(get())
        }

        single<SettingsManager> {
            SettingsManagerImpl(get())
        }

        single<SystemManager> {
            SystemManagerImpl(get())
        }

        single<TokenManager> {
            TokenManagerImpl()
        }

        single<UserManager> {
            UserManagerImpl(get(), get(), get(), get())
        }

        single<WishlistManager> {
            WishlistManagerImpl(get())
        }
    }
