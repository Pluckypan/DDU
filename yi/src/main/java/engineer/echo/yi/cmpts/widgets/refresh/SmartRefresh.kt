package engineer.echo.yi.cmpts.widgets.refresh

import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import engineer.echo.yi.R

object SmartRefresh {

    fun setup() {

    }

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorAccent, R.color.colorPrimaryDark)
            MaterialHeader(context)
        }
    }
}