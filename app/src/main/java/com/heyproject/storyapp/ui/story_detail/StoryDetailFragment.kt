package com.heyproject.storyapp.ui.story_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.FragmentStoryDetailBinding
import com.heyproject.storyapp.model.UserPreference
import com.heyproject.storyapp.model.dataStore
import com.heyproject.storyapp.network.response.ListStoryItem
import com.heyproject.storyapp.ui.ViewModelFactory

class StoryDetailFragment : Fragment() {
    private var binding: FragmentStoryDetailBinding? = null
    private lateinit var userPreference: UserPreference
    private val args: StoryDetailFragmentArgs by navArgs()
    private val viewModel: StoryDetailViewModel by viewModels {
        ViewModelFactory(userPreference)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeActionBar()
        userPreference = UserPreference(requireContext().dataStore)

        val storyItem = ListStoryItem(
            name = args.name,
            description = args.description,
            photoUrl = args.photoUrl
        )
        binding?.apply {
            story = storyItem
        }
        binding?.executePendingBindings()

        viewModel.getUser().observe(viewLifecycleOwner) {
            if (!it.isLogin) {
                findNavController().navigate(R.id.action_storyDetailFragment_to_loginFragment)
            }
        }
    }

    private fun removeActionBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}