package engineer.echo.yi.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import engineer.echo.yi.bean.weather.WeatherResp

object MainContract {

    interface IView {
        fun onSubmitClick(view: View)
    }

    interface IViewModel {
        var weatherData:LiveData<WeatherResp>?
    }

    interface IModel {

    }
}