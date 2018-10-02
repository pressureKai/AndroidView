package github.stefanji.jumpwater

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet
import android.view.View

/*
* created by yang.ji 2018-09-30
*/

const val TAG = "JumpWater"

class JumpWater @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var canvasW = 0
    private var canvasH = 0

    private var mainColor = Color.parseColor("#E55049")
    private val animatorHelper = AnimatorHelper(this)

    // For water
    private var xoff = 70f
    private var yoff = 60f
    private var startX = 0f
    private var startY = 0f
    private var endY = 0f
    private var endX = 0f
    private val startPoint = PointF()
    private val endPoint = PointF()
    private val control1 = PointF()
    private val control2 = PointF()
    private val control3 = PointF()
    private val control4 = PointF()
    private var circleX = 0f
    private var circleY = 0f
    private var circleR = 0f
    private val waterPath = Path()
    private val waterPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = mainColor
    }

    init {
        attrs?.let { set ->
            val typeArray = context.obtainStyledAttributes(set, R.styleable.JumpWater)
            typeArray.recycle()
        }
    }

    internal fun updateStartDown(yOff: Float) {
        startPoint.y = startY + yOff
        calculatePoint()
        control3.y += 2 * yOff
        control4.y = control3.y
        invalidate()
    }

    internal fun updateJump(yOff: Float) {
        startPoint.y = startY - yOff
        endPoint.y = endY - yOff
        calculatePoint()
        invalidate()
    }

    internal fun updateTail(x: Float) {
        endPoint.x = endX - x
        calculatePoint()
        invalidate()
    }

    @JvmOverloads
    fun jump(jumpH: Float = 50f, tailMove: Float = 100f) {
        post {
            animatorHelper.startJump(tailMove, jumpH)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasW = measureW(widthMeasureSpec)
        canvasH = measureH(heightMeasureSpec)
        setMeasuredDimension(canvasW, canvasH)
        initPoint()
        calculatePoint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWater(canvas)
//        drawControl(canvas)
    }

    private fun drawWater(canvas: Canvas) {
        waterPath.apply {
            reset()
            moveTo(startPoint)
            cubicTo(control1, control3, endPoint)
            cubicTo(control4, control2, startPoint)
        }

        canvas.save()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(Path().apply {
                addCircle(circleX, circleY, circleR, Path.Direction.CW)
            })
        } else {
            canvas.clipPath(Path().apply {
                addCircle(circleX, circleY, circleR, Path.Direction.CW)
            }, Region.Op.DIFFERENCE)
        }
        canvas.drawPath(waterPath, waterPaint)
        canvas.drawCircle(circleX, circleY, circleR, waterPaint)
        canvas.restore()
    }

    /**
     * draw bezier's controller
     */
    private fun drawControl(canvas: Canvas) {
        waterPaint.color = Color.BLACK
        waterPaint.strokeWidth = 20f
        canvas.drawPoint(control1, waterPaint)
        canvas.drawPoint(control2, waterPaint)
        canvas.drawPoint(control3, waterPaint)
        canvas.drawPoint(control4, waterPaint)
        canvas.drawPoint(startPoint, waterPaint)
        canvas.drawPoint(endPoint, waterPaint)
        waterPaint.color = mainColor
    }

    private fun measureW(space: Int): Int {
        val mode = MeasureSpec.getMode(space)
        return when (mode) {
            MeasureSpec.AT_MOST -> {
                0
            }

            MeasureSpec.EXACTLY -> {
                measuredWidth
            }

            MeasureSpec.UNSPECIFIED -> {
                0
            }
            else -> {
                0
            }
        }
    }

    private fun measureH(space: Int): Int {
        val mode = MeasureSpec.getMode(space)
        return when (mode) {
            MeasureSpec.AT_MOST -> {
                0
            }

            MeasureSpec.EXACTLY -> {
                measuredHeight
            }

            MeasureSpec.UNSPECIFIED -> {
                0
            }
            else -> {
                0
            }
        }
    }

    private fun initPoint() {
        xoff = canvasW / 3f
        yoff = 70f

        startPoint.x = canvasW / 2f
        startPoint.y = yoff

        endPoint.x = startPoint.x
        endPoint.y = canvasH - yoff

        startX = startPoint.x
        startY = startPoint.y
        endX = endPoint.x
        endY = endPoint.y
    }

    private fun calculatePoint() {

        control1.x = startPoint.x - xoff
        control1.y = startPoint.y

        control2.x = startPoint.x + xoff
        control2.y = control1.y

        control3.x = startPoint.x - xoff / 4
        control3.y = (endPoint.y - startPoint.y)

        control4.x = startPoint.x + xoff / 4
        control4.y = control3.y

        circleX = canvasW / 2f
        circleR = xoff / 4
        circleY = startPoint.y + circleR + circleR / 2
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animatorHelper.endJump()
    }

    //-----------extend functions----------------
    private fun Path.moveTo(p: PointF) {
        moveTo(p.x, p.y)
    }

    private fun Path.lineTo(p: PointF) {
        lineTo(p.x, p.y)
    }

    private fun Path.cubicTo(control1: PointF, control2: PointF, end: PointF) {
        cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
    }

    private fun Path.quadTo(control: PointF, end: PointF) {
        quadTo(control.x, control.y, end.x, end.y)
    }

    private fun Canvas.drawPoint(p: PointF, paint: Paint) {
        drawPoint(p.x, p.y, paint)
    }
}