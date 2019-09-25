package com.apps.kim.todo.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

/**
Created by KIM on 25.09.2019
 **/
@Entity
class TodoDB (
    var title: String? = null,
    var timePerDay: Int? = null,
    var interval: Int? = null,
    var startDate: Long? = null,
    var requestTag: String? = null,
    var timeHour: Int? = null,
    var timeMinute: Int? = null,
    var isPause: Boolean? = null,
    var isSpecificDays: Boolean? = null,
    var daySunday: Int? = null,
    var dayMonday: Int? = null,
    var dayTuesday: Int? = null,
    var dayWednesday: Int? = null,
    var dayThursday: Int? = null,
    var dayFriday: Int? = null,
    var daySaturday: Int? = null,
    var instruction: String? = null,
    var pendingId: Int? = null,
    var finishDate: Long? = null
) {
    @Id
    var id: Long? = 0
    lateinit var history: ToMany<HistoryDb>
}

@Entity
class HistoryDb(
    var title: String? = null,
    var done: String? = null
) {
    @Id
    var id: Long? = 0
}