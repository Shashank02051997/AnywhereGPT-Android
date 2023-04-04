package com.shashank.anywheregpt.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.data.models.ChatResponseBody
import com.shashank.anywheregpt.data.repositories.ChatRepository
import com.shashank.anywheregpt.utils.ChatRole
import com.shashank.anywheregpt.utils.Event
import com.shashank.anywheregpt.utils.State
import com.shashank.anywheregpt.utils.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) :
    ViewModel() {


    companion object {
        private const val GPT_MODEL = "gpt-3.5-turbo"
    }

    var textCopied = ""
    var copiedText = ""
    var maxTokensLength = 400

    var chatMessageList = ArrayList<ChatPostBody.Message>()

    private val _chatLiveData =
        MutableLiveData<Event<State<ChatResponseBody>>>()
    val chatLiveData: LiveData<Event<State<ChatResponseBody>>>
        get() = _chatLiveData

    private lateinit var chatResponse: ChatResponseBody

    init {
        textCopied = "Hi, How may I assist you today?"
        chatMessageList.add(
            ChatPostBody.Message(
                content = "You are a helpful, creative, clever, and very friendly assistant",
                role = ChatRole.SYSTEM.name.lowercase()
            )
        )
    }


    // Intro Docs: https://openai.com/blog/introducing-chatgpt-and-whisper-apis
    // API Docs: https://platform.openai.com/docs/guides/chat
    // Pricing: https://openai.com/api/pricing/

    fun postMessage() {
        _chatLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val result = tryCatch {
                // parameters docs: https://platform.openai.com/docs/api-reference/chat/create
                val chatPostBody = ChatPostBody(
                    // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far,
                    // decreasing the model's likelihood to repeat the same line verbatim.
                    frequencyPenalty = 1.0,
                    // The maximum number of tokens allowed for the generated answer.
                    // By default, the number of tokens the model can return will be (4096 - prompt tokens).
                    maxTokens = maxTokensLength,
                    messages = chatMessageList,
                    model = GPT_MODEL,
                    // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
                    // increasing the model's likelihood to talk about new topics.
                    presencePenalty = 0.6,
                    // Controls randomness: Lowering results in less random completions.
                    // As the temperature approaches zero, the model will become deterministic and repetitive
                    temperature = 0.9,
                    // An alternative to sampling with temperature, called nucleus sampling,
                    // where the model considers the results of the tokens with top_p probability mass.
                    // So 0.1 means only the tokens comprising the top 10% probability mass are considered
                    // OpenAI generally recommends altering this or temperature but not both.
                    topP = 1
                )
                chatResponse =
                    repository.sendMessage(chatPostBody)
            }
            if (result.isSuccess) {
                withContext(Dispatchers.Main) {
                    _chatLiveData.postValue(
                        Event(
                            State.success(
                                chatResponse
                            )
                        )
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    _chatLiveData.postValue(
                        Event(
                            State.error(
                                result.exceptionOrNull()?.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }
}
