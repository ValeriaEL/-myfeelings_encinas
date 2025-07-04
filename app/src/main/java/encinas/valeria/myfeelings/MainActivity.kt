package encinas.valeria.myfeelings

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import encinas.valeria.myfeelings.utilities.CustomCircleDrawable
import encinas.valeria.myfeelings.utilities.Emociones
import encinas.valeria.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import encinas.valeria.myfeelings.utilities.CustomBarDrawable
import androidx.core.content.ContextCompat // Para getDrawable


class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    private lateinit var graph: ConstraintLayout
    private lateinit var icon: ImageView
    private lateinit var question: TextView
    private lateinit var options: android.widget.LinearLayout
    private lateinit var verySadButton: ImageButton
    private lateinit var sadButton: ImageButton
    private lateinit var neutralButton: ImageButton
    private lateinit var happyButton: ImageButton
    private lateinit var veryHappyButton: ImageButton
    private lateinit var guardarButton: Button
    private lateinit var bargraphs: android.widget.GridLayout
    private lateinit var graphVeryHappy: View
    private lateinit var graphHappy: View
    private lateinit var graphNeutral: View
    private lateinit var graphSad: View
    private lateinit var graphVerySad: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        graph = findViewById(R.id.graph)
        icon = findViewById(R.id.icon)
        question = findViewById(R.id.question)
        options = findViewById(R.id.options)
        verySadButton = findViewById(R.id.verySadButton)
        sadButton = findViewById(R.id.sadButton)
        neutralButton = findViewById(R.id.neutralButton)
        happyButton = findViewById(R.id.happyButton)
        veryHappyButton = findViewById(R.id.veryHappyButton)
        guardarButton = findViewById(R.id.guardarButton)
        bargraphs = findViewById(R.id.bargraphs)

        graphVeryHappy = findViewById(R.id.graphVeryHappy)
        graphHappy = findViewById(R.id.graphHappy)
        graphNeutral = findViewById(R.id.graphNeutral)
        graphSad = findViewById(R.id.graphSad)
        graphVerySad = findViewById(R.id.graphVerySad)

        // Configurar insets de ventana (si se usa enableEdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        jsonFile = JSONFile()

        fetchingData()

        if (!data) {
            val emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo

            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, verysad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        verySadButton.setOnClickListener {
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }
        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }
        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }
        veryHappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }
        guardarButton.setOnClickListener {
            guardar()
        }
    }


    fun fetchingData(){
        try {
            val json: String = jsonFile?.getData(this) ?: ""

            if (json.isNotEmpty()) {
                this.data = true
                val jsonArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista){
                    when (i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (exception: JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria(){

        if(happy > veryHappy && happy > neutral && happy > sad && happy > verysad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_happy))
        } else if(veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verysad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_veryhappy))
        } else if(neutral > veryHappy && neutral > happy && neutral > sad && neutral > verysad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_neutral))
        } else if(sad > happy && sad > neutral && sad > veryHappy && sad > verysad){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sad))
        } else if(verysad > happy && verysad > neutral && verysad > sad && verysad > veryHappy){
            icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_verysad))
        } else {
        }
    }

    fun actualizarGrafica(){
        val total = veryHappy + happy + neutral + sad + verysad

        if (total == 0f) {
            Log.w("Grafica", "Total de emociones es cero. No se pueden calcular porcentajes.")
            lista.clear()
            graph.background = CustomCircleDrawable(this, ArrayList<Emociones>()) // Dibujar círculo vacío
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, 0.0F))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, 0.0F))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, 0.0F))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, 0.0F))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, 0.0F))
            return
        }

        val pVH: Float = (veryHappy * 100 / total)
        val pH: Float = (happy * 100 / total)
        val pN: Float = (neutral * 100 / total)
        val pS: Float = (sad * 100 / total)
        val pVS: Float = (verysad * 100 / total)

        Log.d("porcentajes", "very happy " + pVH)
        Log.d("porcentajes", "happy " + pH)
        Log.d("porcentajes", "neutral " + pN)
        Log.d("porcentajes", "sad " + pS)
        Log.d("porcentajes", "very sad " + pVS)

        lista.clear() // Limpiar la lista antes de añadir los nuevos datos
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

        // Actualizar el fondo del gráfico circular
        val fondo = CustomCircleDrawable(this, lista)
        graph.background = fondo

        // Actualizar los fondos de las barras
        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

    }

    fun parseJson (jsonArray: JSONArray): ArrayList<Emociones> {
        val lista = ArrayList<Emociones>()
        for (i in 0 until jsonArray.length()){
            try {
                val jsonObject = jsonArray.getJSONObject(i)
                val nombre = jsonObject.getString("nombre")
                val porcentaje = jsonObject.getDouble("porcentaje").toFloat()
                val color = jsonObject.getInt("color")
                val total = jsonObject.getDouble("total").toFloat()

                val emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception : JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun guardar(){
        val jsonArray = JSONArray()
        for (i in lista){
            Log.d("objetos", i.toString())
            val j = JSONObject()

            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)
        }
        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}