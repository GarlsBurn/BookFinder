package com.bignerdranch.android.bookfinder.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookfinder.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {
    override fun onViewCreated(
        view: View,
        savedInstanceStateview: Bundle?
    ){
        return super.onViewCreated(view, savedInstanceStateview)

    }
}