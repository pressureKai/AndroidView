package github.stefanji.jumpwater

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator

/*
* created by yang.ji 2018-10-02
*/
class AnimatorHelper(val target: JumpWater) {

    // Jump Animator
    private var animDuration = 300L
    private var animStartDown: ValueAnimator? = null
    private var animStartJump: ValueAnimator? = null
    private var animJump: ValueAnimator? = null
    private var animDown: ValueAnimator? = null
    private var animTail: ValueAnimator? = null
    private var animTailReconver: ValueAnimator? = null

    private var mOnAnimEndListener: OnAnimEndListener? = null
    private var animSet: AnimatorSet? = null
    internal fun startJump(tailMove: Float, jumpH: Float) {
        endJump()
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
        animTailReconver = ValueAnimator.ofFloat(tailMove, 0f).apply {
            duration = animDuration
            addUpdateListener {
                target.updateTail(it.animatedValue as Float)
            }
        }

        val tailSet = AnimatorSet().apply {
            playTogether(animJump, animTail)
        }

        val tailSetReconver = AnimatorSet().apply {
            playTogether(animDown, animTailReconver)
        }

        animSet = AnimatorSet().apply {
            playSequentially(animStartDown, animStartJump, tailSet, tailSetReconver)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    mOnAnimEndListener?.onAnimEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            start()
        }
    }

    internal fun endJump() {
        animStartDown?.apply {
            if (isRunning) {
                cancel()
            }
        }
        animStartJump?.apply {
            if (isRunning) {
                cancel()
            }
        }
        animDown?.apply {
            if (isRunning) {
                cancel()
            }
        }
        animTail?.apply {
            if (isRunning) {
                cancel()
            }
        }
    }

    interface OnAnimEndListener {
        fun onAnimEnd();
    }
}