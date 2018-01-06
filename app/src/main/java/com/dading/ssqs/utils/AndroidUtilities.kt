package com.dading.ssqs.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.text.InputType
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.activity.LoginActivity
import com.dading.ssqs.bean.Constent

import java.lang.reflect.Method
import java.util.Hashtable

/**
 * Created by mazhuang on 2017/11/20.
 * 通用的工具类
 */

object AndroidUtilities {
    private val TAG = "AndroidUtilities"

    var density = 1f
    private val displaySize = Point()
    private val displayMetrics = DisplayMetrics()
    private var screenSize: Point? = null

    /**
     * 获取屏幕宽高
     */
    val realScreenSize: Point
        get() {
            if (screenSize != null) {
                return screenSize as Point
            }
            val size = Point()
            try {
                val windowManager = SSQSApplication.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    windowManager.defaultDisplay.getRealSize(size)
                } else {
                    try {
                        val mGetRawW = Display::class.java.getMethod("getRawWidth")
                        val mGetRawH = Display::class.java.getMethod("getRawHeight")
                        size.set(mGetRawW.invoke(windowManager.defaultDisplay) as Int, mGetRawH.invoke(windowManager.defaultDisplay) as Int)
                    } catch (e: Exception) {
                        size.set(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
                        Logger.e(TAG, e.toString() + "")
                    }

                }
            } catch (e: Exception) {
                Logger.e(TAG, e.toString() + "")
            }

            screenSize = size
            return screenSize as Point
        }

    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        density = SSQSApplication.getContext().resources.displayMetrics.density
        checkDisplaySize()
    }

    /**
     * 检查屏幕大小
     */
    fun checkDisplaySize() {
        try {
            val manager = SSQSApplication.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (manager != null) {
                val display = manager.defaultDisplay
                if (display != null) {
                    display.getMetrics(displayMetrics)
                    display.getSize(displaySize)
                    Logger.d(TAG, "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi)
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, e.toString() + "")
        }

    }

    /**
     * 主线程执行任务
     */
    //重载函数
    @JvmOverloads
    fun runOnUIThread(runnable: Runnable, delay: Long = 0) {
        if (delay == 0L) {
            SSQSApplication.getHandler().post(runnable)
        } else {
            SSQSApplication.getHandler().postDelayed(runnable, delay)
        }
    }

    /**
     * 取消任务
     */
    fun cancelRunOnUIThread(runnable: Runnable) {
        SSQSApplication.getHandler().removeCallbacks(runnable)
    }

    fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else Math.ceil((density * value).toDouble()).toInt()
    }

    //检查是否登录
    fun checkIsLogin(errorCode: Int, context: Context): Boolean {
        if (403 == errorCode) {
            UIUtils.SendReRecevice(Constent.LOADING_ACTION)
            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false)
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)

            return true
        } else {
            return false
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val manger = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manger.activeNetworkInfo
            return info?.isConnected ?: false
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 只显示光标,不让软键盘弹出
     */
    fun disableShowInput(editText: EditText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.inputType = InputType.TYPE_NULL
        } else {
            val cls = EditText::class.java
            var method: Method
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(editText, false)
            } catch (e: Exception) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(editText, false)
            } catch (e: Exception) {
            }

        }
    }

    /**
     * 获取程序版本名称u.
     *
     * @param context
     * @return
     * @throws Exception
     */
    fun getVersionName(context: Context): String {
        val manager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = manager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 隐藏软键盘
     */
    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 创建items按下背景
     */
    fun createListSelectorDrawable(context: Context): Drawable? {
        if (Build.VERSION.SDK_INT >= 21) {
            val attrs = intArrayOf(android.R.attr.selectableItemBackground)
            val ta = context.obtainStyledAttributes(attrs)
            val drawableFromTheme = ta.getDrawable(0)
            ta.recycle()
            return drawableFromTheme
        } else {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(), ColorDrawable(0x00000000))
            return stateListDrawable
        }
    }

    /**
     * 创建圆形按下背景
     */
    fun createBarSelectorDrawable(): Drawable {
        if (Build.VERSION.SDK_INT >= 21) {
            val maskDrawable: Drawable
            maskPaint.color = -0x1
            maskDrawable = object : Drawable() {
                override fun draw(canvas: Canvas) {
                    val bounds = bounds
                    canvas.drawCircle(bounds.centerX().toFloat(), bounds.centerY().toFloat(), AndroidUtilities.dp(18f).toFloat(), maskPaint)
                }

                override fun setAlpha(alpha: Int) {

                }

                override fun setColorFilter(colorFilter: ColorFilter?) {

                }

                override fun getOpacity(): Int {
                    return PixelFormat.UNKNOWN
                }
            }
            val colorStateList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(-0x1000000)
            )
            return RippleDrawable(colorStateList, null, maskDrawable)
        } else {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), ColorDrawable(0x0f000000))
            stateListDrawable.addState(intArrayOf(), ColorDrawable(0x00000000))
            return stateListDrawable
        }
    }
}
