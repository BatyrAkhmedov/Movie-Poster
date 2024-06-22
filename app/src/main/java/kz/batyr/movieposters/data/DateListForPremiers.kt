package kz.batyr.movieposters.data

import android.util.Log
import java.time.LocalDateTime

class DateListForPremiers {
    private val allDateList = mutableListOf<String>()
    private val currentTime = LocalDateTime.now()
    private var year = currentTime.year
    private var month = currentTime.monthValue
    private var day = currentTime.dayOfMonth
    private var iteration = 16

    fun getDateList(): List<String> {
        Log.d("TAG", "$year, $month, $day")
        if (currentTime.dayOfMonth <= 14) {
            for (i in 1..14) {
                addToList()
            }
        } else {
            while (day != 31) {
                Log.d("TAG", "$iteration")
                iteration--
                addToList()
            }
            if (month != 12) month++
            else {
                year++
                month = 1
            }
            day = 1
            while (iteration != 0) {
                Log.d("TAG", "$iteration")
                iteration--
                addToList()
            }
        }
        return allDateList.toList()
    }

    private fun addToList() {
        if (month < 10 && day >= 10) {
            allDateList.add("$year-0$month-$day")
            day++
        } else if (month >= 10 && day >= 10) {
            allDateList.add("$year-$month-$day")
            day++
        } else if (month < 10 && day < 10) {
            allDateList.add("$year-0$month-0$day")
            day++
        } else {
            Log.e("TAG", "$day && $month")
            allDateList.add("$year-$month-0$day")
            day++
        }
    }
}