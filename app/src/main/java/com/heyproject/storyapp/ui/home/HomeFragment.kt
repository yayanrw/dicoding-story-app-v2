package com.heyproject.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.FragmentHomeBinding
import com.heyproject.storyapp.ui.SharedViewModel
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.ui.adapter.LoadingStateAdapter
import com.heyproject.storyapp.ui.adapter.StoryAdapter

class HomeFragment : Fragment(), MenuProvider {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: StoryAdapter

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val sharedViewModel: SharedViewModel by activityViewModels() {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Current Page", "HomeFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            homeFragment = this@HomeFragment
            rvStory.adapter = StoryAdapter()
            rvStory.setHasFixedSize(true)
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
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
    }

    private fun fetchStories() {
        storyAdapter = StoryAdapter()
        val token = sharedViewModel.user.value?.token ?: ""

        if (token != "") {
            viewModel.fetchStories(token).observe(viewLifecycleOwner) {
                storyAdapter.submitData(lifecycle, it)
            }
            storyAdapter.onItemClick = { selected ->
                val toDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(selected)
                findNavController().navigate(toDetailFragment)
            }

            binding.rvStory.adapter = storyAdapter.withLoadStateFooter(footer = LoadingStateAdapter {
                storyAdapter.retry()
            })
        }
    }

    fun goToStoryAddScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_storyAddActivity)
    }

    private fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_logout -> {
                sharedViewModel.logOut()
                true
            }
            R.id.action_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> {
                true
            }
        }
    }
}