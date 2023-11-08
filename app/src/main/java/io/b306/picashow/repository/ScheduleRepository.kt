package io.b306.picashow.repository

import android.util.Log
import androidx.annotation.WorkerThread
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.dao.ScheduleDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    val allSchedule: Flow<List<Schedule>> = scheduleDao.getAll()

    @WorkerThread
    suspend fun insert(schedule: Schedule) : Long {
        return scheduleDao.insert(schedule)
    }

    // 특정 일자 일정 가져오기
    private fun getStartAndEndTimestamp(year: Int, month: Int, day: Int): Pair<Long, Long> {
        val startOfDay = LocalDateTime.of(year, month, day, 0, 0)
        val endOfDay = startOfDay.plusDays(1)
        return Pair(startOfDay.toInstant(ZoneOffset.UTC).toEpochMilli(), endOfDay.toInstant(ZoneOffset.UTC).toEpochMilli())
    }

    fun getSchedulesByDate(date: LocalDate): Flow<List<Schedule>> {
        val (startTimestamp, endTimestamp) = getStartAndEndTimestamp(date.year, date.monthValue, date.dayOfMonth)
        return scheduleDao.getSchedulesForDate(startTimestamp, endTimestamp)
    }

    fun getScheduleById(id: String): Schedule? {
        return scheduleDao.getScheduleById(id)
    }

    suspend fun updateSchedule(schedule: Schedule) {
        scheduleDao.update(schedule)
    }

    @WorkerThread
    suspend fun updateScheduleImgUrl(scheduleSeq: String, newImgUrl: String) {
        Log.e("여옵니다2", "레토리")
        scheduleDao.updateWallpaperUrl(scheduleSeq, newImgUrl)
    }
}