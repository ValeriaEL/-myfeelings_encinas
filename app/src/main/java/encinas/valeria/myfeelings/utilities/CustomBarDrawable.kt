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

class CustomBarDrawable(val context: Context, val emocion: Emociones) : Drawable() {

    private var coordenadas: RectF? = null

    override fun draw(canvas: Canvas) {
        val fondo = Paint()
        fondo.style = Paint.Style.FILL
        fondo.isAntiAlias = true
        fondo.color = ContextCompat.getColor(context, R.color.gray)

        val ancho: Float = (canvas.width - 10).toFloat()
        val alto: Float = (canvas.height - 10).toFloat()

        coordenadas = RectF(0.0F, 0.0F, ancho, alto)

        canvas.drawRect(coordenadas!!, fondo)

        val porcentajeAncho: Float = this.emocion.porcentaje * (canvas.width - 10) / 100
        val coordenadasSeccion = RectF(0.0F, 0.0F, porcentajeAncho, alto)

        val seccionPaint = Paint()
        seccionPaint.style = Paint.Style.FILL
        seccionPaint.isAntiAlias = true
        seccionPaint.color = ContextCompat.getColor(context, emocion.color)

        canvas.drawRect(coordenadasSeccion, seccionPaint)
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