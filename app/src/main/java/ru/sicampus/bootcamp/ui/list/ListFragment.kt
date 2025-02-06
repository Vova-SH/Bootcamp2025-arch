package ru.sicampus.bootcamp.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import ru.sicampus.bootcamp.R
import ru.sicampus.bootcamp.databinding.FragmentListBinding
import ru.sicampus.bootcamp.utils.collectWithLifecycle

class ListFragment : Fragment(R.layout.fragment_list) {
    private var _viewBinding: FragmentListBinding? = null
    private val viewBinding: FragmentListBinding get() = _viewBinding!!

    private val viewModel by viewModels<ListViewModel> { ListViewModel.Factory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentListBinding.bind(view)

        val adapter = UserAdapter()
        viewBinding.refresh.setOnClickListener { adapter.refresh() }
        viewBinding.content.adapter = adapter

        viewModel.listState.collectWithLifecycle(this) { data ->
            adapter.submitData(data)
        }

        adapter.loadStateFlow.collectWithLifecycle(this) { data ->
            val state = data.refresh
            viewBinding.error.visibility =
                if (state is LoadState.Error) View.VISIBLE else View.GONE
            viewBinding.loading.visibility =
                if (state is LoadState.Loading) View.VISIBLE else View.GONE
            if (state is LoadState.Error) {
                viewBinding.errorText.text = state.error.message.toString()
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}
