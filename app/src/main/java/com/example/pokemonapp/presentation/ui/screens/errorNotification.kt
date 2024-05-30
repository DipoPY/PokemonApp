import android.content.Context
import android.widget.Toast

fun errorNotification(context: Context) {
    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
}
