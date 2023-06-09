package com.example.bbyak
import java.util.*

fun getPossibleTime(users: ArrayList<User>): ArrayList<PossibleTimeZone>{
    val finalPossibleTime = ArrayList<PossibleTimeZone>()
    finalPossibleTime.addAll(calculateSchedule(users, null))

    val exclude = ArrayList<PossibleTimeZone>()
    if (finalPossibleTime.size < 5) {
        for (i in users) {
            val new = ArrayList(users).apply { remove(i) }
            exclude.addAll(
                calculateSchedule(new, i.name)
            )
        }
        exclude.sortWith(compareBy({ -it.interval }, { it.year }, { it.month }, { it.day }))
        val pivot = exclude[0].interval
        for (item in exclude) {
            if (item.interval == pivot) finalPossibleTime.add(item)
        }
    }

    return finalPossibleTime
}

private fun calculateSchedule(list: ArrayList<User>, name: String?): ArrayList<PossibleTimeZone> {
    val calcul = ArrayList<PossibleSchedule>()
    for (item in list[0].schedules) {
        val time = item.time.toCharArray().map { it == '1' } as ArrayList<Boolean>
        calcul.add(
            PossibleSchedule(
                item.year,
                item.month,
                item.day,
                ArrayList(time),
                list.size,
                name
            )
        )
    }

    for (i in 1 until list.size) {
        for (j in 0 until list[0].schedules.size) {
            val time = list[i].schedules[j].time.toCharArray().map { it == '1' } as ArrayList<Boolean>
            calcul[j].calculate(time)
        }
    }

    val allPossibles = ArrayList<PossibleTimeZone>()
    for (item in calcul) allPossibles.addAll(item.getPossibleTime())

    allPossibles.sortWith(compareBy({ -it.interval }, { it.year }, { it.month }, { it.day }))
    return allPossibles
}
