package com.cdr.core.uiactions

import android.view.View

/**
 * Common actions that can be performed in the view-model.
 */
interface UiActions {

    /**
     * Display a simple toast message.
     */
    fun showToast(message: String)

    /**
     * Display a simple snackbar.
     */
    fun showSnackbar(view: View, message: String, backgroundColor: Int)

    /**
     * Get string resource.
     */
    fun getString(res: Int): String
}