package com.udacity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StatusViewModel(private val app: Application) : AndroidViewModel(app) {
    private val _file = MutableLiveData<String>()
    val file: LiveData<String>
        get() = _file

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        _file.value = ""
        _status.value = ""
    }

    fun setFile(file: String) {
        _file.value = file
    }

    fun setStatus(status: String) {
        _status.value = status
    }
}