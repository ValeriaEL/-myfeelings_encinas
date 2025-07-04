package encinas.valeria.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import encinas.valeria.myfeelings.R
import android.graphics.Color

class CustomCircleDrawable(val context: Context, var emociones: ArrayList<Emociones>) : Drawable() {

    private var coordenadas: RectF? = null
    private var anguloBarrido: Float = 0.0F
    private var anguloInicio: Float = 0.0F
    private var grosorMetrica: Int
    private var grosorFondo: Int

    init {
        // Inicializa grosorMetrica y grosorFondo aquí, después de que 'context' esté disponible
        grosorMetrica = context.resources.getDimensionPixelSize(R.dimen.graphWith)
        grosorFondo = context.resources.getDimensionPixelSize(R.dimen.graphBackground) // Asumo que 15dp era graphBackground
    }

    override fun draw(canvas: Canvas) {
        val fondo = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosorFondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = ContextCompat.getColor(context, R.color.gray)

        val ancho: Float = (canvas.width - 25).toFloat()
        val alto: Float = (canvas.height - 25).toFloat()

        coordenadas = RectF(25.0F, 25.0F, ancho, alto)

        canvas.drawArc(coordenadas!!, 0.0F, 360.0F, false, fondo)

        if (emociones.isNotEmpty()) { // Usar isNotEmpty() es más idiomático
            anguloInicio = 0.0F // Reiniciar anguloInicio para cada dibujo
            for (e in emociones) {
                val degree: Float = (e.porcentaje * 360) / 100
                this.anguloBarrido = degree

                val seccion = Paint()
                seccion.style = Paint.Style.STROKE
                seccion.isAntiAlias = true
                seccion.strokeWidth = (this.grosorMetrica).toFloat()
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = ContextCompat.getColor(context, e.color)

                canvas.drawArc(coordenadas!!, this.anguloInicio, this.anguloBarrido, false, seccion)

                this.anguloInicio += this.anguloBarrido
            }
        }
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }
}