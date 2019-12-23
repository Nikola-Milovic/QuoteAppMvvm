package com.nikolam.simplyquotes.ui.util

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity


// NOT USED YET

/**
 * AlertDialog with positive button only
 */
fun FragmentActivity.alertDialogExtension(
    title: String,
    message: String,
    positiveButtonText: String,
    positiveButtonAction: (dialog: DialogInterface, which: Int) -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText, positiveButtonAction)
        .create()
        .show()
}

/**
 * AlertDialog with positive and negative button
 */
fun FragmentActivity.alertDialogExtension(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positiveButtonAction: (dialog: DialogInterface, which: Int) -> Unit,
    negativeButtonAction: (dialog: DialogInterface, which: Int) -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText, positiveButtonAction)
        .setNegativeButton(negativeButtonText, negativeButtonAction)
        .create()
        .show()
}