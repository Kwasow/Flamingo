package pl.kwasow.ui.screens.modules.whishlist

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kwasow.R
import pl.kwasow.data.types.TabItem
import pl.kwasow.data.types.Wish
import pl.kwasow.managers.UserManager
import pl.kwasow.managers.WishlistManager

class WishlistModuleViewModel(
    private val applicationContext: Context,
    userManager: UserManager,
    private val wishlistManager: WishlistManager,
) : ViewModel() {
    // ====== Fields
    val tabs = TabItem.getWishlistTabs(userManager.getCachedUser())

    var isWishlistLoading: Boolean by mutableStateOf(true)
        private set
    var wishlist: Map<String, MutableList<Wish>> by mutableStateOf(emptyMap())
        private set

    var editedWish: Wish? by mutableStateOf(null)
    var inputWishContent: String by mutableStateOf("")
    var sendingWish: Boolean by mutableStateOf(false)
        private set
    var deletingWish: Boolean by mutableStateOf(false)
        private set

    var wishToDelete: Wish? by mutableStateOf(null)
        private set
    var wishToUpdate: Wish? by mutableStateOf(null)
        private set

    // ====== Public methods
    fun refreshWishlist() {
        viewModelScope.launch {
            isWishlistLoading = true
            silentRefresh()
            isWishlistLoading = false
        }
    }

    fun getPersonsWishes(author: String): List<Wish> = wishlist.getOrDefault(author, emptyList())

    fun addWish(author: String) {
        if (inputWishContent.isBlank()) {
            return
        }

        viewModelScope.launch {
            sendingWish = true

            if (wishlistManager.addWish(author, inputWishContent)) {
                inputWishContent = ""
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.module_wishlist_add_failed,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            silentRefresh()
            sendingWish = false
        }
    }

    fun updateEditedWish() {
        val wish = editedWish ?: return

        viewModelScope.launch {
            sendingWish = true

            if (wishlistManager.updateWish(wish.update(newContent = inputWishContent))) {
                inputWishContent = ""
                editedWish = null
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.module_wishlist_update_failed,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            silentRefresh()
            sendingWish = false
        }
    }

    fun changeWishState(wish: Wish) {
        viewModelScope.launch {
            wishToUpdate = wish
            wishlistManager.updateWish(wish.update(newDone = !wish.done))
            silentRefresh()
            wishToUpdate = null
        }
    }

    fun askDeleteWish(wish: Wish) {
        wishToDelete = wish
    }

    fun cancelDeleteWish() {
        wishToDelete = null
    }

    fun confirmDeleteWish() {
        val wish =
            wishToDelete
                ?: throw IllegalStateException("Cannot confirm deletion of null wish")

        viewModelScope.launch {
            deletingWish = true
            if (!wishlistManager.removeWish(wish)) {
                Toast.makeText(
                    applicationContext,
                    R.string.module_wishlist_remove_failed,
                    Toast.LENGTH_SHORT,
                ).show()
            }
            wishToDelete = null
            silentRefresh()
            deletingWish = false
        }
    }

    // ====== Private methods
    private suspend fun silentRefresh() {
        wishlist = wishlistManager.getWishlist()?.mapValues { it.value.toMutableList() }
            ?: emptyMap()
    }
}
