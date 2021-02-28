package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    // Custom view attributes
    private var bgColor = 0
    private var textColor = 0
    private var linearProgressBarColor = 0
    private var circularProgressBarColor = 0
    private var buttonText: String = ""

    private lateinit var valueAnimator: ValueAnimator

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Completed -> {
                buttonText = context.getString(R.string.button_name)
                if (valueAnimator.isStarted) {
                    valueAnimator.cancel()
                }

                // Reset current progress
                currentProgress = 0f

                // Re-enable button
                isClickable = true

                // Redraw the button
                invalidate()
            }
            else -> {
                buttonText = context.getString(R.string.button_loading)
                if (!valueAnimator.isStarted) {
                    valueAnimator.start()
                }
                isClickable = false
            }
        }
    }

    // Current progress
    @Volatile
    private var currentProgress: Float = 0f

    // Circular progress bar oval
    private val oval = RectF()
    private var isOvalConfigured = false

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColor = getColor(R.styleable.LoadingButton_bgColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            linearProgressBarColor = getColor(R.styleable.LoadingButton_linearProgressBarColor, 0)
            circularProgressBarColor =
                getColor(R.styleable.LoadingButton_circularProgressBarColor, 0)
        }

        configureAnimator()

        buttonState = ButtonState.Completed
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            // Draw background
            paint.color = bgColor
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            // Draw linear progress bar
            paint.color = linearProgressBarColor
            canvas.drawRect(0f, 0f, width * currentProgress / 100, height.toFloat(), paint)

            // Draw circular progress bar
            configureOval()
            paint.color = circularProgressBarColor
            canvas.drawArc(oval, 270f, 360 * currentProgress / 100, true, paint)

            // Draw button text
            paint.color = Color.WHITE
            canvas.drawText(
                buttonText,
                width / 2f,
                (height - (paint.descent() + paint.ascent())) / 2f,
                paint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun configureAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0f, 100f)

        valueAnimator.apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE

            addUpdateListener {
                currentProgress = it.animatedValue as Float
                invalidate()
            }
        }
    }

    private fun configureOval() {
        if (!isOvalConfigured) {
            oval.set(
                width - height * 0.75f,
                height * 0.25f,
                width - height * 0.25f,
                height * 0.75f
            )

            isOvalConfigured = true
        }
    }

    fun beginLoading() {
        buttonState = ButtonState.Loading
    }

    fun completeLoading() {
        buttonState = ButtonState.Completed
    }
}