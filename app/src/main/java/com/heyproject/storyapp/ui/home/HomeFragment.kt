package com.heyproject.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.heyproject.storyapp.R
import com.heyproject.storyapp.databinding.FragmentHomeBinding
import com.heyproject.storyapp.ui.ViewModelFactory
import com.heyproject.storyapp.ui.adapter.LoadingStateAdapter
import com.heyproject.storyapp.ui.adapter.StoryAdapter

class HomeFragment : Fragment(), MenuProvider {
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_logout -> {
                viewModel.logOut()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
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