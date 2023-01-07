package com.org.datingapp.features.home.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.org.datingapp.databinding.FragmentChatsBinding

/***
 *
 * Reference project links for chat application
 * Let's Chat App :
 * https://github.com/a914-gowtham/LetsChat
 * Tutorial Links
 * https://medium.com/@gowtham6670/android-designing-a-chat-app-with-firebase-part-1-45c8d25106b4
 * https://medium.com/@gowtham6670/android-designing-a-chat-app-with-firebase-part-2-a89c60ca1826
 * https://medium.com/@gowtham6670/android-designing-a-chat-app-with-firebase-part-3-b70c1d9bc1e0
 *
 * **/

class ChatsFragment : Fragment() {

    private var _binding : FragmentChatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }
}