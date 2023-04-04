package com.shashank.anywheregpt.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.anywheregpt.R
import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.databinding.ActivityAnywhereDialogBinding
import com.shashank.anywheregpt.ui.adapters.ChatListAdapter
import com.shashank.anywheregpt.ui.base.BaseActivity
import com.shashank.anywheregpt.ui.viewmodels.ChatViewModel
import com.shashank.anywheregpt.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnywhereDialogActivity : BaseActivity() {

    private val chatListAdapter by lazy {
        ChatListAdapter(mViewModel)
    }

    private lateinit var mViewBinding: ActivityAnywhereDialogBinding
    private val mViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setFinishOnTouchOutside(false)
        super.onCreate(savedInstanceState)
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_anywhere_dialog)
        mViewModel.maxTokensLength =
            SharedPref.getStringPref(this, SharedPref.KEY_TOKEN_LENGTH).toInt()
        setupUI()
        observeAPICall()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        chatListAdapter.addItems(mViewModel.chatMessageList)
        mViewBinding.apply {
            if (intent.action == Intent.ACTION_PROCESS_TEXT) {
                intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.let { copiedText ->
                    mViewModel.copiedText = copiedText
                    mViewModel.textCopied =
                        "It appears that you have copied:\n$copiedText\n\nWhat would you like me to do with it"
                }
            }
            ivClose.setOnClickListener {
                finish()
            }
            rvChatList.apply {
                layoutManager = LinearLayoutManager(this@AnywhereDialogActivity)
                adapter = chatListAdapter
            }
            fabSend.setOnClickListener {
                dismissKeyboard(it)
                if (SharedPref.getStringPref(this@AnywhereDialogActivity, SharedPref.KEY_API_KEY)
                        .isNullOrBlank()
                ) {
                    this@AnywhereDialogActivity.showToast(getString(R.string.message_add_api_key))
                    return@setOnClickListener
                }
                if (etMessage.text.toString().isNullOrBlank()) {
                    showToast(getString(R.string.message_enter_some_text))
                    return@setOnClickListener
                }
                val content =
                    if (mViewModel.copiedText.isNotBlank()) "${mViewModel.copiedText}\n${etMessage.text.toString()}" else etMessage.text.toString()
                mViewModel.chatMessageList.add(
                    ChatPostBody.Message(
                        content = content,
                        role = ChatRole.USER.name.lowercase()
                    )
                )
                chatListAdapter.addItems(mViewModel.chatMessageList)
                rvChatList.scrollToPosition(
                    chatListAdapter.itemCount.minus(1)
                )
                mViewModel.postMessage()
                etMessage.text?.clear()
                mViewModel.copiedText = ""
            }
        }
    }

    private fun observeAPICall() {
        mViewModel.chatLiveData.observe(this, EventObserver { state ->
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
                    showToast(state.message)
                }
            }
        })
    }
}