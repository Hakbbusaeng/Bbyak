package com.example.bbyak

class User(
    val name: String,
    val schedules: ArrayList<Schedule>
)

class Schedule(
    val year: Int,
    val month: Int,
    val day: Int,
    val time: String
)

class PossibleSchedule(
    val year: Int,
    val month: Int,
    val day: Int,
    val time: ArrayList<Boolean>,
    var headCount: Int,
    val name: String? = null
) {
    fun calculate(time2: ArrayList<Boolean>) {
        for (i in time.indices) {
            time[i] = time[i] && time2[i]
        }
    }

    fun getPossibleTime(): ArrayList<PossibleTimeZone> {
        val result = ArrayList<PossibleTimeZone>()
        var p: PossibleTimeZone? = null
        for (i in time.indices) {
            if (time[i]) {
                if (p == null) p = PossibleTimeZone(year, month, day, i, i, headCount, name)
                else p.increaseEnd()
            } else if (p != null) {
                result.add(p)
                p = null
            }
        }
        if (p != null) result.add(p)
        return result
    }
}

class PossibleTimeZone(
    val year: Int,
    val month: Int,
    val day: Int,
    val start: Int,
    var end: Int,
    var headCount: Int,
    val name: String? = null
) {
    val interval: Int
        get() = end - start + 1

    fun increaseEnd() {
        end++
    }
}