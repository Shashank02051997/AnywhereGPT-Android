package com.shashank.anywheregpt.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shashank.anywheregpt.BuildConfig
import com.shashank.anywheregpt.R
import com.shashank.anywheregpt.databinding.FragmentSettingsBinding
import com.shashank.anywheregpt.ui.base.BaseFragment
import com.shashank.anywheregpt.ui.viewmodels.EmptyViewModel
import com.shashank.anywheregpt.utils.SharedPref
import com.shashank.anywheregpt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<EmptyViewModel, FragmentSettingsBinding>() {

    override val mViewModel: EmptyViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            (activity as? AppCompatActivity)?.setSupportActionBar(layoutToolbar.toolbar)
            layoutToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white)
            layoutToolbar.tvToolbarTitle.text = getString(R.string.toolbar_title_settings)
            layoutToolbar.toolbar.setNavigationOnClickListener {
                onBackNavigation()
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                onBackNavigation()
            }
            tvAppVersion.text = getString(R.string.label_app_version, BuildConfig.VERSION_NAME)
            etApiKey.setText(
                SharedPref.getStringPref(
                    requireContext(),
                    SharedPref.KEY_API_KEY
                )
            )
            etTokenLength.setText(
                SharedPref.getStringPref(
                    requireContext(),
                    SharedPref.KEY_TOKEN_LENGTH
                )
            )
            btnUpdateSettings.setOnClickListener {
                if (etApiKey.text.toString().isNullOrBlank()) {
                    requireContext().showToast(getString(R.string.message_enter_api_key))
                    return@setOnClickListener
                }
                if (etTokenLength.text.toString().isNullOrBlank()) {
                    requireContext().showToast(getString(R.string.message_enter_token))
                    return@setOnClickListener
                }
                SharedPref.setStringPref(
                    requireContext(),
                    SharedPref.KEY_API_KEY,
                    etApiKey.text.toString()
                )
                SharedPref.setStringPref(
                    requireContext(),
                    SharedPref.KEY_TOKEN_LENGTH,
                    etTokenLength.text.toString()
                )
                requireContext().showToast(getString(R.string.message_settings_updated))
            }
        }
    }

    private fun onBackNavigation() {
        findNavController().popBackStack()
    }
}