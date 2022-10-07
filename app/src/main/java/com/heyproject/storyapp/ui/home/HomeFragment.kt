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
import com.heyproject.storyapp.adapter.StoryAdapter
import com.heyproject.storyapp.databinding.FragmentHomeBinding
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.model.dataStore
import com.heyproject.storyapp.network.response.ListStoryItem
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.util.RequestState


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var userPreference: UserPreference
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory(
            userPreference
        )
    }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()
        userPreference = UserPreference(requireContext().dataStore)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            homeFragment = this@HomeFragment
            rvStory.adapter = StoryAdapter(listOf())
            rvStory.setHasFixedSize(true)
            screenError.homeFragment = this@HomeFragment
        }

        fetchStories()

        viewModel.getUser().observe(viewLifecycleOwner) {
            if (!it.isLogin) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }

        viewModel.stories.observe(viewLifecycleOwner) {
            storyAdapter = StoryAdapter(listOf())
            storyAdapter = StoryAdapter(it)
            binding?.rvStory?.adapter = storyAdapter

            storyAdapter.setOnItemClickCallBack(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(storyItem: ListStoryItem) {
                    val toStoryDetailFragment =
                        HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(
                            storyItem.name!!,
                            storyItem.description!!,
                            storyItem.photoUrl!!
                        )
                    findNavController().navigate(toStoryDetailFragment)
                }
            })
        }

        viewModel.requestState.observe(viewLifecycleOwner) {
            when (it) {
                RequestState.LOADING -> {
                    binding?.circularProgressIndicator?.visibility = View.VISIBLE
                    binding?.rvStory?.visibility = View.GONE
                    binding?.screenError?.root?.visibility = View.GONE
                }
                RequestState.NO_DATA -> {
                    binding?.circularProgressIndicator?.visibility = View.GONE
                    binding?.rvStory?.visibility = View.GONE
                    binding?.screenError?.root?.visibility = View.VISIBLE
                    binding?.screenError?.tvError?.text = getString(R.string.no_data_available)
                }
                RequestState.ERROR -> {
                    binding?.circularProgressIndicator?.visibility = View.GONE
                    binding?.rvStory?.visibility = View.GONE
                    binding?.screenError?.root?.visibility = View.VISIBLE
                    binding?.screenError?.tvError?.text = getString(R.string.oops)
                }
                RequestState.NO_CONNECTION -> {
                    binding?.circularProgressIndicator?.visibility = View.GONE
                    binding?.rvStory?.visibility = View.GONE
                    binding?.screenError?.root?.visibility = View.VISIBLE
                    binding?.screenError?.tvError?.text = getString(R.string.no_connection)
                }
                else -> {
                    binding?.circularProgressIndicator?.visibility = View.GONE
                    binding?.rvStory?.visibility = View.VISIBLE
                    binding?.screenError?.root?.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchStories()
    }

    fun fetchStories() {
        viewModel.fetchStories()
    }

    fun goToStoryAddScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_storyAddActivity)
    }

    private fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        storyAdapter = StoryAdapter(listOf())
    }
}