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
import com.org.datingapp.databinding.FragmentUploadPhotoDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPhotoDialogFragment : DialogFragment() {

    private var _binding : FragmentUploadPhotoDialogBinding? = null
    private val binding get() = _binding!!

    private val photosViewModel by hiltNavGraphViewModels<PhotosViewModel>(R.id.profile_graph)
    private val uploadPhotoViewModel by viewModels<UploadPhotoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadPhotoDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.cancelButton.setOnClickListener {
            cancelPhotoUpload()
        }
        binding.photo.setImageURI(photosViewModel.photoUri)
        uploadPhotoViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    override fun onStart() {
        super.onStart()
        upload()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    private fun upload() {
        val photoUri = photosViewModel.photoUri
        val position = photosViewModel.position
        when (photosViewModel.option) {
            is PhotosViewModel.Options.Add -> {
                uploadPhotoViewModel.add(photoUri)
            }
            is PhotosViewModel.Options.Edit -> {
                uploadPhotoViewModel.edit(photoUri, position)
            }
        }
    }

    private fun cancelPhotoUpload() {
        dismiss()
    }

    private fun handleEvents(event : Event<UploadPhotoViewModel.Navigation>) {
        event.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is UploadPhotoViewModel.Navigation.ShowUploadPhotoFailure -> {
                    cancelPhotoUpload()
                }
                is UploadPhotoViewModel.Navigation.ShowUploadPhotoSuccess -> {
                    photosViewModel.getPhotos()
                    dismiss()
                }
            }
        }
    }

    companion object {
        fun newInstance() = UploadPhotoDialogFragment()
    }

}