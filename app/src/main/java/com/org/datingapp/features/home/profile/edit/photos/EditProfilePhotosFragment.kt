package com.org.datingapp.features.home.profile.edit.photos

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.org.datingapp.R
import com.org.datingapp.core.platform.EqualSpacingItemDecoration
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentEditProfilePhotosBinding
import com.org.datingapp.features.onboarding.photos.PhotosAdapter
import com.org.datingapp.features.onboarding.photos.PhotosOptionsViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfilePhotosFragment : Fragment(), EasyPermissions.PermissionCallbacks  {

    private var _binding : FragmentEditProfilePhotosBinding? = null
    private val binding get() = _binding!!

    private val photosViewModel by hiltNavGraphViewModels<PhotosViewModel>(R.id.profile_graph)
    private val optionsViewModel by navGraphViewModels<PhotosOptionsViewModel>(R.id.profile_graph)

    private val layoutManager by lazy {
        GridLayoutManager(requireActivity().applicationContext, 3)
    }
    private val adapter by lazy {
        PhotosAdapter({onAddPhotoClick()}, { position, localUri -> onPhotoClick(position, localUri) })
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                val resultUri = data!!.data
                photosViewModel.photoUri = resultUri!!
                showUploadPhotoDialog()
            }
            ImagePicker.RESULT_ERROR -> {
                val error = ImagePicker.getError(data)
                Log.e("TAG1", error)
            }
            else -> {
                Log.d("TAG1", "Task Cancelled")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfilePhotosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        setPhotosRecycler()
        optionsViewModel.events.observe(viewLifecycleOwner, this::handleOptionsEvents)
        photosViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        photosViewModel.getPhotos()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        when(requestCode) {
            REQUEST_MEDIA_PERMISSIONS_CODE -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    requestMediaPermissionsRationale()
                }
                else {
                    requestMediaPermissions()
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    private fun showUploadPhotoDialog() {
        val action = EditProfilePhotosFragmentDirections.actionEditProfilePhotosFragmentToUploadPhotoDialogFragment2()
        findNavController().navigate(action)
    }

    private fun showRemoveDialog() {
        val action = EditProfilePhotosFragmentDirections.actionEditProfilePhotosFragmentToRemovePhotoDialogFragment()
        findNavController().navigateUp()
        findNavController().navigate(action)
    }

    private fun setPhotosRecycler() {
        binding.photosRecycler.adapter = adapter
        binding.photosRecycler.layoutManager = layoutManager
        binding.photosRecycler.addItemDecoration(EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.GRID))
    }

    private fun refreshPhotosRecycler(localUris : List<Uri>) {
        adapter.refreshPhotos(localUris)
    }

    private fun onAddPhotoClick() {
        if (checkMediaPermissions()) {
            photosViewModel.option = PhotosViewModel.Options.Add
            startCropActivity()
            return
        }
        requestMediaPermissions()
    }

    private fun onPhotoClick(position : Int, localUri : String) {
        if (checkMediaPermissions()) {
            val action = EditProfilePhotosFragmentDirections.actionEditProfilePhotosFragmentToPhotosBottomSheetDialogFragment2(localUri, position)
            findNavController().navigate(action)
            return
        }
        requestMediaPermissions()
    }

    private fun startCropActivity() {
        ImagePicker.with(this)
            .compress(256)
            .maxResultSize(640, 480)
            .cropSquare()
            .saveDir(File(requireActivity().filesDir, "profileImages" ))
            .createIntent {
                startForProfileImageResult.launch(it)
            }
    }

    private fun handleEvents(event : Event<PhotosViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is PhotosViewModel.Navigation.ShowGetLocalUris -> {
                    refreshPhotosRecycler(navigation.localUris)
                }
            }
        }
    }

    private fun handleOptionsEvents(event : Event<PhotosOptionsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is PhotosOptionsViewModel.Navigation.ShowEditPhoto -> {
                    photosViewModel.option = PhotosViewModel.Options.Edit
                    photosViewModel.position = navigation.position
                    startCropActivity()
                }
                is PhotosOptionsViewModel.Navigation.ShowRemovePhoto -> {
                    photosViewModel.position = navigation.position
                    showRemoveDialog()
                }
            }
        }
    }


    private fun getPermissions() : Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            )
        }
        else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }

    private fun checkMediaPermissions() : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)  {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA)
        }
        else {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
        }
    }

    private fun requestMediaPermissions() {
        val permissions = getPermissions()

        val request = PermissionRequest.Builder(requireActivity())
            .code(REQUEST_MEDIA_PERMISSIONS_CODE)
            .perms(permissions)
            .rationale(R.string.request_media_permissions_rationale)
            .positiveButtonText(R.string.request_permission_rationale_positive)
            .negativeButtonText(R.string.request_permission_rationale_negative)
            .build()

        EasyPermissions.requestPermissions(this, request)
    }

    private fun requestMediaPermissionsRationale() {

        val stringRes =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            R.string.denied_media_permissions_rationale_33_plus
        }
        else {
            R.string.denied_media_permissions_rationale
        }

        SettingsDialog.Builder(requireActivity())
            .title(R.string.permission_required_title)
            .rationale(stringRes)
            .positiveButtonText(R.string.rationale_permissions_positive_button)
            .negativeButtonText(R.string.rationale_permissions_negative_button)
            .build()
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_MEDIA_PERMISSIONS_CODE = 2000
        @JvmStatic
        fun newInstance() = EditProfilePhotosFragment()
    }
}