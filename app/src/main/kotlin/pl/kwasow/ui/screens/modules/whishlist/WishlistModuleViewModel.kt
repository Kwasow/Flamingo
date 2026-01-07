package pl.kwasow.ui.screens.modules.whishlist

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.kwasow.R
import pl.kwasow.data.types.TabItem
import pl.kwasow.flamingo.types.user.MinimalUser
import pl.kwasow.flamingo.types.wishlist.Wish
import pl.kwasow.managers.UserManager
import pl.kwasow.managers.WishlistManager

class WishlistModuleViewModel(
    private val applicationContext: Context,
    private val userManager: UserManager,
    private val wishlistManager: WishlistManager,
) : ViewModel() {
    // ====== Fields
    private var wishlist: Map<Int, MutableList<Wish>> by mutableStateOf(emptyMap())

    val tabs = userManager.userFlow.map { TabItem.getWishlistTabs(it) }

    var isWishlistLoading: Boolean by mutableStateOf(true)
        private set

    var inputWishContent: String by mutableStateOf("")

    var isAddingWish: Boolean by mutableStateOf(false)
        private set
    var isDeletingWish: Boolean by mutableStateOf(false)
        private set
    var deleteError: Boolean by mutableStateOf(false)
        private set

    var wishToEdit: Wish? by mutableStateOf(null)
        private set
    var wishToDelete: Wish? by mutableStateOf(null)
        private set
    var wishToUpdate: Wish? by mutableStateOf(null)
        private set

    // ====== Constructors
    init {
        viewModelScope.launch {
            if (tabs.first() == null) {
                userManager.refreshUser()
            }
        }
    }

    // ====== Public methods
    fun refreshWishlist() {
        viewModelScope.launch {
            isWishlistLoading = true
            silentRefresh()
            isWishlistLoading = false
        }
    }

    fun getPersonsWishes(user: MinimalUser): List<Wish> =
        wishlist.getOrDefault(
            user.id,
            emptyList(),
        )

    fun addWish(user: MinimalUser) {
        if (inputWishContent.isBlank()) {
            return
        }

        viewModelScope.launch {
            isAddingWish = true

            if (wishlistManager.addWish(user.id, inputWishContent)) {
                inputWishContent = ""
            } else {
                Toast
                    .makeText(
                        applicationContext,
                        R.string.module_wishlist_add_failed,
                        Toast.LENGTH_SHORT,
                    ).show()
            }

            silentRefresh()
            isAddingWish = false
        }
    }

    fun startEditingWish(wish: Wish) {
        wishToEdit = wish
        inputWishContent = wish.content
    }

    fun editWish() {
        val wish = wishToEdit ?: return

        viewModelScope.launch {
            isAddingWish = true

            if (wishlistManager.updateWish(wish.update(newContent = inputWishContent))) {
                inputWishContent = ""
                wishToEdit = null
            } else {
                Toast
                    .makeText(
                        applicationContext,
                        R.string.module_wishlist_update_failed,
                        Toast.LENGTH_SHORT,
                    ).show()
            }

            silentRefresh()
            isAddingWish = false
        }
    }

    fun finishEditWishAction() {
        inputWishContent = ""
        wishToEdit = null
    }

    fun changeWishState(wish: Wish) {
        viewModelScope.launch {
            wishToUpdate = wish
            wishlistManager.updateWish(wish.update(newDone = !wish.done))
            silentRefresh()
            wishToUpdate = null
        }
    }

    fun startDeletingWish(wish: Wish) {
        wishToDelete = wish
    }

    fun deleteWish(wish: Wish) {
        viewModelScope.launch {
            isDeletingWish = true
            deleteError = false

            if (wishlistManager.deleteWish(wish.id)) {
                closeDeleteWishDialog()
                silentRefresh()
            } else {
                deleteError = true
            }

            isDeletingWish = false
        }
    }

    fun closeDeleteWishDialog() {
        deleteError = false
        wishToDelete = null
    }

    // ====== Private methods
    private suspend fun silentRefresh() {
        wishlist = wishlistManager.getWishlist()?.mapValues { it.value.toMutableList() }
            ?: emptyMap()
    }
}
