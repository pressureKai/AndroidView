package github.stefanji.jumpwater

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator

/*
* created by yang.ji 2018-10-02
*/
class AnimatorHelper(val target: JumpWater) {

    // Jump Animator
    var animDuration = 300L
    private var isRunningAnim = false
    private var animStartDown: ValueAnimator? = null
    private var animStartJump: ValueAnimator? = null
    private var animJump: ValueAnimator? = null
    private var animDown: ValueAnimator? = null
    private var animTail: ValueAnimator? = null
    private var animTailRecover: ValueAnimator? = null

    private var mOnAnimEndListener: OnAnimEndListener? = null
    private var animSet: AnimatorSet? = null
    internal fun startJump(tailMove: Float, jumpH: Float) {
        if (isRunningAnim) {
            return
        }
        animStartDown = ValueAnimator.ofFloat(0f, jumpH).apply {
            duration = animDuration
            addUpdateListener {
                target.updateStartDown(it.animatedValue as Float)
            }
        }
        animStartJump = ValueAnimator.ofFloat(jumpH, 0f).apply {
            duration = animDuration
            addUpdateListener {
                target.updateStartDown(it.animatedValue as Float)
            }
        }
        animJump = ValueAnimator.ofFloat(0f, jumpH).apply {
            duration = animDuration
            addUpdateListener {
                target.updateJump(it.animatedValue as Float)
            }
        }
        animDown = ValueAnimator.ofFloat(jumpH, 0f).apply {
            duration = animDuration
            addUpdateListener {
                target.updateJump(it.animatedValue as Float)
            }
        }
        animTail = ValueAnimator.ofFloat(0f, tailMove).apply {
            duration = animDuration
            addUpdateListener {
                target.updateTail(it.animatedValue as Float)
            }
        }
        animTailRecover = ValueAnimator.ofFloat(tailMove, 0f).apply {
            duration = animDuration
            addUpdateListener {
                target.updateTail(it.animatedValue as Float)
            }
        }

        val tailSet = AnimatorSet().apply {
            playTogether(animJump, animTail)
        }

        val tailSetRecover = AnimatorSet().apply {
            playTogether(animDown, animTailRecover)
        }

        animSet = AnimatorSet().apply {
            playSequentially(animStartDown, animStartJump, tailSet, tailSetRecover)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isRunningAnim = false
                    mOnAnimEndListener?.onAnimEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    isRunningAnim = true
                }
            })
            start()
        }
    }

    internal fun endJump() {
        animSet?.apply {
            if (isRunning) {
                cancel()
            }
        }
    }

    interface OnAnimEndListener {
        fun onAnimEnd();
    }
}