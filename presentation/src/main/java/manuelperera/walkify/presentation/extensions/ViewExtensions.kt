package manuelperera.walkify.presentation.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import manuelperera.walkify.presentation.R

fun View.snackbar(
    title: String = "",
    action: String = "",
    length: Int = Snackbar.LENGTH_LONG,
    @ColorRes actionColor: Int = R.color.colorPrimary,
    actionResult: () -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, title, length)

    if (action.isNotEmpty()) {
        snackbar.setAction(action) { actionResult() }
        snackbar.setActionTextColor(ContextCompat.getColor(context, actionColor))
    }
    snackbar.show()

    return snackbar
}