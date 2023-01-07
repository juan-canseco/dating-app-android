package com.org.datingapp.features.home.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.MarginItemDecoration
import com.org.datingapp.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    // https://www.youtube.com/watch?v=qeXFuOx0oJA
    private var _binding : FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val getUserProfilesViewModel by viewModels<GetUserProfilesViewModel>()
    private lateinit var adapter : UserProfilesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usersRecycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.usersRecycler.addItemDecoration(MarginItemDecoration(16))
        getUserProfilesViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getUserProfilesViewModel.getProfiles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleEvents(event : Event<GetUserProfilesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                GetUserProfilesViewModel.Navigation.ShowGetUserProfilesStart -> {
                    binding.loadingBar.visibility = View.VISIBLE
                }
                GetUserProfilesViewModel.Navigation.ShowGetUserProfilesEmptyList -> {
                    binding.loadingBar.visibility = View.GONE
                    binding.emptyListMessage.visibility = View.VISIBLE
                }
                is GetUserProfilesViewModel.Navigation.ShowGetUserProfilesSuccess -> {
                    setProfilesAdapter(navigation.userProfiles)
                    binding.loadingBar.visibility = View.GONE
                }
                GetUserProfilesViewModel.Navigation.ShowGetUserProfilesError -> {
                    binding.loadingBar.visibility = View.GONE
                }
            }
        }
    }

    private fun onProfileClick(userProfileId : String) {
        val action = ExploreFragmentDirections.actionExploreFragmentToUserProfileDetailsDialogFragment(userProfileId)
        findNavController().navigate(action)
    }

    private fun setProfilesAdapter(userProfiles : List<UserProfileView>) {
        adapter = UserProfilesAdapter(userProfiles) { userProfileId -> onProfileClick(userProfileId) }

        binding.usersRecycler.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExploreFragment()
    }

}