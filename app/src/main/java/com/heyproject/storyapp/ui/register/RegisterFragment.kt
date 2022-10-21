package com.heyproject.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.FragmentRegisterBinding
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.MIN_PASSWORD_LENGTH
import com.heyproject.storyapp.util.Result

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            registerFragment = this@RegisterFragment
        }

        playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playAnimation() {
        val appName = ObjectAnimator.ofFloat(binding.tvAppName, View.ALPHA, 1f).setDuration(300)
        val registerName =
            ObjectAnimator.ofFloat(binding.registerName, View.ALPHA, 1f).setDuration(300)
        val registerEmail =
            ObjectAnimator.ofFloat(binding.registerEmail, View.ALPHA, 1f).setDuration(300)
        val registerPassword =
            ObjectAnimator.ofFloat(binding.registerPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(appName, registerName, registerEmail, registerPassword, btnRegister)
            start()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.linearProgressIndicator.visibility = View.VISIBLE
            binding.btnRegister.isEnabled = false
        } else {
            binding.linearProgressIndicator.visibility = View.GONE
            binding.btnRegister.isEnabled = true
        }
    }

    fun register() {
        if (formValidation()) {
            with(viewModel) {
                register(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                ).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            setLoading(true)
                        }
                        is Result.Success -> {
                            setLoading(false)
                            Snackbar.make(
                                binding.root,
                                getString(R.string.success_register),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        is Result.Error -> {
                            setLoading(false)
                            Snackbar.make(
                                binding.root,
                                getString(R.string.oops),
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun formValidation(): Boolean {
        var isValid = true
        if (binding.edRegisterName.text.isNullOrEmpty()) {
            binding.registerName.error = getString(R.string.required)
            isValid = false
        } else {
            binding.registerName.error = null
        }

        if (binding.edRegisterEmail.text.isNullOrEmpty()) {
            binding.registerEmail.error = getString(R.string.required)
            isValid = false
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.edRegisterEmail.text.toString())
                    .matches()
            ) {
                binding.registerEmail.error = getString(R.string.not_valid_email)
                isValid = false
            } else {
                binding.registerEmail.error = null
            }
        }

        if (binding.edRegisterPassword.text.isNullOrEmpty()) {
            binding.registerPassword.error = getString(R.string.required)
            isValid = false
        } else if (binding.edRegisterPassword.text!!.length < MIN_PASSWORD_LENGTH) {
            binding.registerPassword.error = getString(R.string.minlength, MIN_PASSWORD_LENGTH)
            isValid = false
        } else {
            binding.registerPassword.error = null
        }
        return isValid
    }
}