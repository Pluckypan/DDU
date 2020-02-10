package engineer.echo.yi.producer.ui.main

import androidx.lifecycle.LiveData
import engineer.echo.yi.producer.cmpts.zip.ZipState

object MainContract {

    interface IView {

    }

    interface IViewModel {
        fun unzip()
        val unzipData: LiveData<ZipState>
    }

    interface IModel {

    }
}