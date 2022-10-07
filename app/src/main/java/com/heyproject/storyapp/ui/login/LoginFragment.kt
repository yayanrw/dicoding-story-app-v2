package com.heyproject.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.heyproject.storyapp.R
import com.heyproject.storyapp.core.MIN_PASSWORD_LENGTH
import com.heyproject.storyapp.databinding.FragmentLoginBinding
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.model.dataStore
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.RequestState

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private lateinit var userPreference: UserPreference
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(userPreference)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeActionBar()
        userPreference = UserPreference(requireContext().dataStore)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            loginFragment = this@LoginFragment
        }

        viewModel.getUser().observe(viewLifecycleOwner) {
            if (it.isLogin) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        viewModel.requestState.observe(viewLifecycleOwner) {
            when (it) {
                RequestState.LOADING -> {
                    setLoading(true)
                }
                RequestState.ERROR -> {
                    setLoading(false)
                    Snackbar.make(view, getString(R.string.oops), Snackbar.LENGTH_SHORT).show()
                }
                RequestState.INVALID_CREDENTIALS -> {
                    setLoading(false)
                    Snackbar.make(
                        view,
                        getString(R.string.invalid_credentials),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                RequestState.NO_CONNECTION -> {
                    setLoading(false)
                    Snackbar.make(view, getString(R.string.no_connection), Snackbar.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    setLoading(false)
                }
            }
        }

        playAnimation()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.linearProgressIndicator?.visibility = View.VISIBLE
            binding?.btnSignIn?.isEnabled = false
        } else {
            binding?.linearProgressIndicator?.visibility = View.GONE
            binding?.btnSignIn?.isEnabled = true
        }
    }

    private fun removeActionBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun goToRegisterScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun signIn() {
        if (formValidation()) {
            viewModel.signIn(
                binding!!.edLoginEmail.text.toString(),
                binding!!.edLoginPassword.text.toString()
            )
        }
    }

    private fun formValidation(): Boolean {
        var isValid = true
        if (binding?.edLoginEmail?.text.isNullOrEmpty()) {
            binding?.loginEmail?.error = getString(R.string.required)
            isValid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding?.edLoginEmail?.text.toString()).matches()) {
                binding?.loginEmail?.error = getString(R.string.not_valid_email)
                isValid = false
            } else {
                binding?.loginEmail?.error = null
            }
        }

        if (binding?.edLoginPassword?.text.isNullOrEmpty()) {
            binding?.loginPassword?.error = getString(R.string.required)
            isValid = false
        } else if (binding?.edLoginPassword?.text?.length!! < MIN_PASSWORD_LENGTH) {
            binding?.loginPassword?.error = getString(R.string.minlength, MIN_PASSWORD_LENGTH)
            isValid = false
        } else {
            binding?.loginPassword?.error = null
        }
        return isValid
    }

    private fun playAnimation() {
        val appName = ObjectAnimator.ofFloat(binding?.tvAppName, View.ALPHA, 1f).setDuration(300)
        val loginEmail =
            ObjectAnimator.ofFloat(binding?.loginEmail, View.ALPHA, 1f).setDuration(300)
        val loginPassword =
            ObjectAnimator.ofFloat(binding?.loginPassword, View.ALPHA, 1f).setDuration(300)
        val btnSignIn = ObjectAnimator.ofFloat(binding?.btnSignIn, View.ALPHA, 1f).setDuration(300)
        val lnrNotRegistered =
            ObjectAnimator.ofFloat(binding?.lnrNotRegistered, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(appName, loginEmail, loginPassword, btnSignIn, lnrNotRegistered)
            start()
        }
    }
}