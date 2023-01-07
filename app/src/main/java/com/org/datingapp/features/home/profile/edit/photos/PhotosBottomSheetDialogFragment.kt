package com.org.datingapp.features.home.profile.edit.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentPhotosBottomSheetDialogBinding
import com.org.datingapp.features.onboarding.photos.PhotosOptionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val optionsViewModel by navGraphViewModels<PhotosOptionsViewModel>(R.id.profile_graph)
    private val photosViewModel by navGraphViewModels<PhotosViewModel>(R.id.profile_graph)
    private val args : PhotosBottomSheetDialogFragmentArgs by navArgs()
    private var _binding : FragmentPhotosBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBottomSheetDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changePhoto.setOnClickListener {
            optionsViewModel.editPhoto(args.photoPosition)
            dismiss()
        }
        binding.removePhoto.setOnClickListener {
            dismiss()
            optionsViewModel.removePhoto(args.photoPosition)
        }
        binding.removePhoto.visibility = if (photosViewModel.hasOnlyOneItem) {
            View.GONE
        }
        else {
            View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhotosBottomSheetDialogFragment()
    }
}