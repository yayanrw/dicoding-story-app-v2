package com.heyproject.storyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.heyproject.storyapp.R
import com.heyproject.storyapp.adapter.LoadingStateAdapter
import com.heyproject.storyapp.adapter.StoryAdapter
import com.heyproject.storyapp.databinding.FragmentHomeBinding
import com.heyproject.storyapp.ui.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: StoryAdapter
    private var token: String = ""

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            homeFragment = this@HomeFragment
            rvStory.adapter = StoryAdapter()
            rvStory.setHasFixedSize(true)
            screenError.homeFragment = this@HomeFragment
        }

        setObserver()
        fetchStories()
    }

    override fun onResume() {
        super.onResume()
        fetchStories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObserver() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (!it.isLogin) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                this.token = it.token
            }
        }
    }

    private fun fetchStories() {
        storyAdapter = StoryAdapter()
        viewModel.fetchStories(this.token).observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }
        storyAdapter.onItemClick = { selected ->
            val toDetailFragment =
                HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(selected)
            findNavController().navigate(toDetailFragment)
        }

        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    fun goToStoryAddScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_storyAddActivity)
    }

    private fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}