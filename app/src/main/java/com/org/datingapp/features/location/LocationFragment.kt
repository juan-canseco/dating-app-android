package com.org.datingapp.features.location

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentLocationBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding : FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val loadingViewModel by viewModels<LoadingViewModel>()
    private val locationRequestViewModelService by viewModels<LocationRequestViewModelService>()

    @Inject
    lateinit var locationService : LocationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationRequestViewModelService.events.observe(viewLifecycleOwner, this::handleEvents)
        binding.allowLocationButton.setOnClickListener {
            if (hasLocationPermission()) {
                locationRequestViewModelService.requestLocation()
                return@setOnClickListener
            }
            requestLocationPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                SettingsDialog.Builder(requireActivity())
                    .title(R.string.permission_required_title)
                    .rationale(R.string.denied_location_permission_rationale)
                    .positiveButtonText(R.string.rationale_permissions_positive_button)
                    .negativeButtonText(R.string.rationale_permissions_negative_button)
                    .build()
                    .show()
            }
            else {
                requestLocationPermission()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    private fun handleEvents(event : Event<LocationRequestViewModelService.Navigation?>) {
        event.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                LocationRequestViewModelService.Navigation.ShowStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.allowLocationButton)
                }
                LocationRequestViewModelService.Navigation.ShowLocationPermissionNotGranted -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.allowLocationButton)
                }
                LocationRequestViewModelService.Navigation.ShowSuccess -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.allowLocationButton)
                    goToFinishFragment()
                }
            }
        }
    }


    private fun showProgressBarCenter(button : Button) {
        button.showProgress {
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_CENTER
        }
    }

    private fun disableProgressBarCenter(button : Button) {
        button.hideProgress(R.string.action_allow_location)
    }

    private fun requestLocationPermission() {

        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)

        val request = PermissionRequest.Builder(requireActivity())
            .code(REQUEST_LOCATION_PERMISSION_CODE)
            .perms(permissions)
            .rationale(R.string.request_location_permission_rationale)
            .positiveButtonText(R.string.request_permission_rationale_positive)
            .negativeButtonText(R.string.request_permission_rationale_negative)
            .build()

        EasyPermissions.requestPermissions(this, request)
    }

    private fun goToFinishFragment() {
        val action = LocationFragmentDirections.actionLocationFragmentToFinishFragment()
        findNavController().navigate(action)
    }

    private fun hasLocationPermission() : Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LocationFragment()
        const val REQUEST_LOCATION_PERMISSION_CODE = 1000
    }

}