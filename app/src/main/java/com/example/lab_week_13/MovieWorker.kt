package com.example.lab_week_13

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieWorker(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val movieRepository = (appContext as MovieApplication).movieRepository

        return try {
            withContext(Dispatchers.IO) {
                movieRepository.fetchMoviesFromNetwork()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
