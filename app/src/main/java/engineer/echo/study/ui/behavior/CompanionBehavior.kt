package engineer.echo.study.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 *  CompanionBehavior.kt
 *  Info: CompanionBehavior.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/26 - 10:35 PM
 *  more about me: http://www.1991th.com
 */
class CompanionBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.x = dependency.x + 20
        child.y = dependency.y + 20
        return true
    }
}