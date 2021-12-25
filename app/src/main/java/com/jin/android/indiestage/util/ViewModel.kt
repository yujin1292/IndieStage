package com.jin.android.indiestage.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.ui.home.HomeViewModel


fun <VM : ViewModel> viewModelProviderFactoryOf(
    create: () -> VM
): ViewModelProvider.Factory = SimpleFactory(create)


private class SimpleFactory<VM : ViewModel>(
    private val create: () -> VM
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm = create()
        if ( modelClass.isInstance(HomeViewModel::class.java)){
            return HomeViewModel(ExhibitionRepo()) as T
        }
        if (modelClass.isInstance(vm)) {
            @Suppress("UNCHECKED_CAST")
            return vm as T
        }
        throw IllegalArgumentException("Can not create ViewModel for class: $modelClass")
    }
}
