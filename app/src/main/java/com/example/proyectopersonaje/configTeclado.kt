import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


object configTeclado {

    fun ocultarTeclado(context: Activity, view: android.view.View) {
        val metodoentrada =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        metodoentrada.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun ocultarTeclado(context: Activity) {
        try {
            ocultarTeclado(context, context.currentFocus!!)
        } catch (e: Exception) {
        }
    }
}