package ru.adminmk.kobbytest

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.adminmk.libmodel.GqlRepository

class MainViewModel: ViewModel() {
    private val gqlRepository = GqlRepository()

    suspend fun testGQL() {
        val result = gqlRepository.test()
        Log.d(TAG, "result: $result")
    }

    companion object{
        const val TAG = "KobbyTest"
    }
}
