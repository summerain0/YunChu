package com.cxoip.yunchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateDocumentViewModel : ViewModel() {
}

class CreateDocumentViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateDocumentViewModel::class.java)) {
            return CreateDocumentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}