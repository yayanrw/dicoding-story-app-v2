package com.heyproject.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.heyproject.storyapp.R
import com.heyproject.storyapp.core.MIN_PASSWORD_LENGTH
import com.heyproject.storyapp.databinding.FragmentLoginBinding
import com.heyproject.storyapp.domain.model.toLoggedInUser
import com.heyproject.storyapp.ui.SharedViewModel
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.Result

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val sharedViewModel: SharedViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Current Page", "LoginFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            loginFragment = this@LoginFragment
        }

        setObserver()
        playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObserver() {
        viewModel.loginState.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is Result.Loading -> {
                    setLoading(true)
                }
                is Result.Success -> loginResult.data?.let {
                    setLoading(false)
                    sharedViewModel.saveUser(it.toLoggedInUser())
                }
                is Result.Error -> {
                    setLoading(false)
                    Snackbar.make(binding.root, getString(R.string.oops), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.linearProgressIndicator.visibility = View.VISIBLE
            binding.btnSignIn.isEnabled = false
        } else {
            binding.linearProgressIndicator.visibility = View.GONE
            binding.btnSignIn.isEnabled = true
        }
    }

    fun goToRegisterScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun signIn() {
        if (formValidation()) {
            viewModel.signIn(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
        }
    }

    private fun formValidation(): Boolean {
        var isValid = true
        if (binding.edLoginEmail.text.isNullOrEmpty()) {
            binding.loginEmail.error = getString(R.string.required)
            isValid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.edLoginEmail.text.toString()).matches()) {
                binding.loginEmail.error = getString(R.string.not_valid_email)
                isValid = false
            } else {
                binding.loginEmail.error = null
            }
        }

        if (binding.edLoginPassword.text.isNullOrEmpty()) {
            binding.loginPassword.error = getString(R.string.required)
            isValid = false
        } else if (binding.edLoginPassword.text?.length!! < MIN_PASSWORD_LENGTH) {
            binding.loginPassword.error = getString(R.string.minlength, MIN_PASSWORD_LENGTH)
            isValid = false
        } else {
            binding.loginPassword.error = null
        }
        return isValid
    }

    private fun playAnimation() {
        val appName = ObjectAnimator.ofFloat(binding.tvAppName, View.ALPHA, 1f).setDuration(300)
        val loginEmail =
            ObjectAnimator.ofFloat(binding.loginEmail, View.ALPHA, 1f).setDuration(300)
        val loginPassword =
            ObjectAnimator.ofFloat(binding.loginPassword, View.ALPHA, 1f).setDuration(300)
        val btnSignIn = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(300)
        val lnrNotRegistered =
            ObjectAnimator.ofFloat(binding.lnrNotRegistered, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(appName, loginEmail, loginPassword, btnSignIn, lnrNotRegistered)
            start()
        }
    }
}