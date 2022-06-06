package es.javier.cappcake.data.data_sources

import java.util.*
import javax.inject.Inject

class CalendarUtil @Inject constructor() {

    /**
     * Method to get the data of seven days ago
     *
     * @return The data of the last week
     */
    fun getLastWeekDate() : Date {

        val calendar = Calendar.getInstance()

        calendar.time = Date()

        calendar.add(Calendar.DATE, -7)

        return calendar.time

    }

}