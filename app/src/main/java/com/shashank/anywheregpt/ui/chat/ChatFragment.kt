package com.shashank.anywheregpt.ui.chat

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.anywheregpt.R
import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.databinding.FragmentChatBinding
import com.shashank.anywheregpt.ui.adapters.ChatListAdapter
import com.shashank.anywheregpt.ui.base.BaseFragment
import com.shashank.anywheregpt.ui.viewmodels.ChatViewModel
import com.shashank.anywheregpt.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    private val chatListAdapter by lazy {
        ChatListAdapter(mViewModel)
    }

    override val mViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.maxTokensLength =
            SharedPref.getStringPref(requireContext(), SharedPref.KEY_TOKEN_LENGTH).toInt()
        chatListAdapter.addItems(mViewModel.chatMessageList)
        allowOverlayPermission()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        addMenuProvider(R.menu.menu) { menuItem ->
            when (menuItem.itemId) {
                R.id.faq -> {
                    navigate(ChatFragmentDirections.actionChatFragmentToFaqFragment())
                    true
                }
                R.id.settings -> {
                    navigate(ChatFragmentDirections.actionChatFragmentToSettingsFragment())
                    true
                }
                else -> false
            }
        }
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            (activity as? AppCompatActivity)?.setSupportActionBar(layoutToolbar.toolbar)
            layoutToolbar.tvToolbarTitle.text = getString(R.string.toolbar_title_anywhere_gpt)
            rvChatList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = chatListAdapter
            }
            fabSend.setOnClickListener {
                requireContext().dismissKeyboard(it)
                if (SharedPref.getStringPref(requireContext(), SharedPref.KEY_API_KEY)
                        .isNullOrBlank()
                ) {
                    requireContext().showToast(getString(R.string.message_add_api_key))
                    return@setOnClickListener
                }
                if (etMessage.text.toString().isNullOrBlank()) {
                    requireContext().showToast(getString(R.string.message_enter_some_text))
                    return@setOnClickListener
                }
                mViewModel.chatMessageList.add(
                    ChatPostBody.Message(
                        content = etMessage.text.toString(),
                        role = ChatRole.USER.name.lowercase()
                    )
                )
                chatListAdapter.addItems(mViewModel.chatMessageList)
                rvChatList.scrollToPosition(
                    chatListAdapter.itemCount.minus(1)
                )
                mViewModel.postMessage()
                etMessage.text?.clear()
            }
        }
    }

    override fun observeAPICall() {
        mViewModel.chatLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    mViewBinding.apply {
                        pbLoading.show()
                        fabSend.invisible()
                    }
                }
                is State.Success -> {
                    if (state.data.choices.isNotEmpty()) {
                        mViewModel.chatMessageList.add(
                            ChatPostBody.Message(
                                content = state.data.choices.first().message.content,
                                role = ChatRole.ASSISTANT.name.lowercase()
                            )
                        )
                        chatListAdapter.addItems(mViewModel.chatMessageList)
                        mViewBinding.apply {
                            rvChatList.scrollToPosition(
                                chatListAdapter.itemCount.minus(1)
                            )
                            pbLoading.hide()
                            fabSend.show()
                        }
                    }
                }
                is State.Error -> {
                    mViewBinding.apply {
                        pbLoading.hide()
                        fabSend.show()
                    }
                    requireContext().showToast(state.message)
                }
            }
        })
    }

    private fun allowOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Permission Request for \"Draw Over Other Apps\"")
            builder.setMessage("In order to provide you with the best experience, our app requires the \"Draw over other apps\" permission. This allows us to display our app's features on top of other apps. Please grant this permission to continue using our app")
            builder.setCancelable(false)
                .setPositiveButton(
                    "Ok"
                ) { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${requireContext().packageName}")
                    )
                    startActivityForResult(intent, 0)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    dialog.dismiss()
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }


}