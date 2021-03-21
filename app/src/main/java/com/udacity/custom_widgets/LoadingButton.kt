package com.udacity.custom_widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates
import android.view.animation.LinearInterpolator
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.withStyledAttributes
import com.udacity.R


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var _valueAnimator = ValueAnimator()
    private var _width = 0
    private var _height = 0
    private var _buttonText: String =""
    private var _buttonTextInProgress: String =""
    private var _currentButtonText: String = ""
    private var _backgroundColor = 0
    private var _backgroundColorInProgress = 0
    private var _currentBackgroundColor= 0
    private var _widthInProgress = 0f
    private val _paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                startAnimator()
            }
            ButtonState.Completed -> {
                stopAnimator()
            }
            else -> stopAnimator()
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton, block = {
            _backgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            _backgroundColorInProgress = getColor(R.styleable.LoadingButton_backgroundColorInProgress, 0)
            _buttonText = getStringOrThrow(R.styleable.LoadingButton_buttonText)
            _buttonTextInProgress =  getStringOrThrow(R.styleable.LoadingButton_buttonTextInProgress)
        })
        isClickable = true
        _currentButtonText = _buttonText
        _currentBackgroundColor = _backgroundColor
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw progress bar
        canvas.drawProgressBar(_paint)

        // Draw text
        canvas.drawTextInProgress(_currentButtonText, _paint)

        // Draw circle
        canvas.drawCircle( _paint, 50)
    }

    private fun Canvas.drawProgressBar(paint: Paint) {
        val progress = _widthInProgress * measuredWidth.toFloat()

        _paint.color = _currentBackgroundColor
        drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), _paint)
        drawRect(0f, measuredHeight.toFloat(), progress, 0f, _paint)
    }

    private fun Canvas.drawTextInProgress(buttonText: String, paint: Paint) {
        // Draw text
        paint.color = context.getColor(R.color.white)
        val xPos = measuredWidth / 2
        val yPos = (measuredHeight / 2 - (paint.descent() + paint.ascent()) / 2)
        drawText(buttonText, xPos.toFloat(), yPos, paint)
    }

    private fun Canvas.drawCircle(paint: Paint, radius: Int) {
        // Circle position
        paint.color = context.getColor(R.color.colorAccent)
        var positionXProgress = (measuredWidth / 2 + paint.measureText(_currentButtonText) / 1.5).toInt()
        positionXProgress -= radius
        var positionYProgress = measuredHeight / 2.toFloat()
        positionYProgress -= radius

        // Draw circle
        drawArc(
            positionXProgress.toFloat(),
            positionYProgress,
            (positionXProgress + radius * 2).toFloat(),
            positionYProgress + radius * 2,
            0f,
            360f * _widthInProgress,
            false,
            _paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val width: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val height: Int = resolveSizeAndState(
            MeasureSpec.getSize(width),
            heightMeasureSpec,
            0
        )
        _width = width
        _height = height
        setMeasuredDimension(width, height)

    }

    private fun startAnimator() {
        _currentButtonText = _buttonTextInProgress
        _currentBackgroundColor = _backgroundColorInProgress
        _valueAnimator =  ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            addUpdateListener { it ->
                _widthInProgress = it.animatedFraction
                postInvalidate()
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            duration = 1000
            start()
        }
    }

    private fun stopAnimator(){
        _valueAnimator.cancel()
        _currentButtonText = _buttonText
        _currentBackgroundColor = _backgroundColor
        _widthInProgress = 0f
    }
}