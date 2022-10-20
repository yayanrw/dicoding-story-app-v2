package com.heyproject.storyapp.ui.story_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.heyproject.storyapp.databinding.FragmentStoryDetailBinding

class StoryDetailFragment : Fragment() {
    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!

    private val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            story = args.story
        }
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}