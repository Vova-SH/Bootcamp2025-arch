package ru.sicampus.bootcamp.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.sicampus.bootcamp.R
import ru.sicampus.bootcamp.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {
    private var _viewBinding: FragmentListBinding? = null
    private val viewBinding: FragmentListBinding get() = _viewBinding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentListBinding.bind(view)

        viewBinding.refresh.setOnClickListener { TODO() }
        viewBinding.content.adapter = TODO()

        //Set content
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}