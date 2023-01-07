package com.org.datingapp.features.home.profile.edit.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentDeletePhotoDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemovePhotoDialogFragment : DialogFragment() {

    private var _binding : FragmentDeletePhotoDialogBinding? = null
    private val binding get() = _binding!!

    private val photosViewModel by hiltNavGraphViewModels<PhotosViewModel>(R.id.profile_graph)
    private val removePhotoViewModel by viewModels<RemovePhotoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDeletePhotoDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.cancelButton.setOnClickListener {
            cancelPhotoUpload()
        }
        binding.photo.setImageURI(photosViewModel.getPhoto(photosViewModel.position))
        removePhotoViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }


    override fun onStart() {
        super.onStart()
        delete()
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    private fun handleEvents(event : Event<RemovePhotoViewModel.Navigation>) {
        event.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                RemovePhotoViewModel.Navigation.ShowRemovePhotoSuccess -> {
                    photosViewModel.getPhotos()
                    dismiss()
                }
                RemovePhotoViewModel.Navigation.ShowRemovePhotoFailure -> {
                    cancelPhotoUpload()
                }
            }
        }
    }

    private fun delete() {
        val position = photosViewModel.position
        removePhotoViewModel.remove(position)
    }

    private fun cancelPhotoUpload() {
        dismiss()
    }

    companion object {
        fun newInstance() = RemovePhotoDialogFragment()
    }
}