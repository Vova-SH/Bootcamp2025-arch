package ru.sicampus.bootcamp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.sicampus.bootcamp.R
import ru.sicampus.bootcamp.databinding.FragmentAuthBinding
import ru.sicampus.bootcamp.ui.list.ListFragment
import ru.sicampus.bootcamp.utils.collectWithLifecycle

class AuthFragment: Fragment(R.layout.fragment_auth) {
    private var _viewBinding: FragmentAuthBinding? = null
    private val viewBinding: FragmentAuthBinding get() = _viewBinding!!

    private val viewModel by viewModels<AuthViewModel> { AuthViewModel.Factory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentAuthBinding.bind(view)

        viewBinding.next.setOnClickListener {
            viewModel.clickNext(
                viewBinding.login.text.toString(),
                viewBinding.password.text.toString(),
            )
        }

        viewBinding.login.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                viewModel.changeLogin()
            }
        })
        viewModel.action.collectWithLifecycle(this) { action ->
            when (action) {
                AuthViewModel.Action.GoToList -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main, ListFragment())
                        .commitAllowingStateLoss()
                }
            }
        }

        viewModel.state.collectWithLifecycle(this) { state ->
            viewBinding.next.isEnabled = state is AuthViewModel.State.Show
            viewBinding.login.isEnabled = state is AuthViewModel.State.Show
            viewBinding.password.isEnabled = state is AuthViewModel.State.Show

            if (state is AuthViewModel.State.Show) {
                viewBinding.title.text = state.titleText
                viewBinding.next.text = state.buttonText
                viewBinding.error.text = state.errorText
                viewBinding.error.visibility =
                    if (state.errorText != null) View.VISIBLE else View.GONE
                viewBinding.password.visibility =
                    if (state.showPassword) View.VISIBLE else View.GONE

            }

        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }
}
