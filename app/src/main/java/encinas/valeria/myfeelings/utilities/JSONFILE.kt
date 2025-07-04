package encinas.valeria.myfeelings.utilities

import android.content.Context
import android.util.Log
import java.io.IOException

class JSONFile {

    val MY_FEELINGS = "data.json"

    constructor(){

    }

    fun saveData(context: Context, json: String ) {
        try {
            context.openFileOutput(MY_FEELINGS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("GUARDAR", "Error in Writing: " + e.localizedMessage)
        }
    }

    fun getData(context: Context): String {
        try {
            context.openFileInput(MY_FEELINGS).bufferedReader().use { reader ->
                return reader.readLine() ?: ""
            }
        } catch (e: IOException) {
            Log.e("OBTENER", "Error in fetching data: " + e.localizedMessage)
            return ""
        }
    }
}