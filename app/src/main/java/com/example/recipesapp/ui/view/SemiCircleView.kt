package com.example.recipesapp.ui.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.Color.HSVToColor
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.example.recipesapp.R
import java.util.*


class SemiCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // View size in pixels
    var metrics: DisplayMetrics? = context.resources.displayMetrics
    private val weightPixel = metrics!!.widthPixels.toFloat()
    private val heightPixel = metrics!!.heightPixels.toFloat()
    private val DEFAULT_HEIGHT = 750f
    private val DEFAULT_COLOR = HSVToColor(floatArrayOf(26f,98f,66f))
    // Colors for the background
    private var _background = DEFAULT_COLOR
    private var image: Drawable? = null
    private var heightShape = DEFAULT_HEIGHT.toFloat()
    private val mPath = Path()

    private val paint = Paint()

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SemiCircleView,
            0, 0)

        // Extract custom attributes into member variables
        heightShape = typedArray.getDimensionPixelSize(R.styleable.SemiCircleView_height, DEFAULT_HEIGHT.toInt()).toFloat()
        _background = typedArray.getColor(R.styleable.SemiCircleView_color, DEFAULT_COLOR)
        image = typedArray.getDrawable(R.styleable.SemiCircleView_image_shape)

        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)

        canvas.save()
        drawBackgroundProfile(canvas)
        canvas.restore()

    }

    private fun drawBackgroundProfile(canvas: Canvas) {
        paint.color = _background

        mPath.moveTo(0f, 0f)
        mPath.lineTo(weightPixel, 0f)
        mPath.lineTo(weightPixel, heightShape)
        mPath.quadTo((weightPixel / 2), heightShape+150f, 0f, heightShape)
        mPath.lineTo(0f, 0f)
        canvas.clipPath(mPath)

        if (image != null){
            var bitmap = (image as BitmapDrawable).bitmap
            var xScale = weightPixel / bitmap.width
            var yScale = heightPixel / bitmap.height

            var scale =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (xScale <= yScale) xScale * 1.15f  else yScale
                } else {
                    if (xScale > yScale) xScale else yScale
                }

            var m = Matrix()
            m.postScale(scale , scale )
            canvas.drawBitmap(bitmap, m, paint)
        }else {
            canvas.drawColor(paint.color)
        }
    }


    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        var viewState = state
        if (viewState is Bundle) {
            viewState = viewState.getParcelable("superState")!!
        }
        super.onRestoreInstanceState(viewState)
    }

}

