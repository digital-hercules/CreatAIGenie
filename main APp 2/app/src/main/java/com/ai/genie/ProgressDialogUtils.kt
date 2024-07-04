package com.ai.genie

import android.app.ProgressDialog
import android.content.Context

// Define ProgressDialogUtils class
class ProgressDialogUtils(private val context: Context) {
    private var progressDialog: ProgressDialog? = null

    // Function to show a progress dialog with a specified message
    fun showProgress(message: String) {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    // Function to hide the progress dialog
    fun hideProgress() {
        progressDialog?.dismiss()
    }
}
