package engineer.echo.yi.common.cpmts.widgets.refresh

import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout

object SmartRefresh {

    fun setup() {

    }

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
    }
}