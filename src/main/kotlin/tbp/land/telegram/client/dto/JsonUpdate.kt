package tbp.land.telegram.client.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class JsonUpdate(
    val updateId: Long,
    @JsonProperty("message")
    @JsonAlias("channel_post", "edited_channel_post")
    val message: JsonMessage?
)

data class JsonMessage(
    val messageId: Long,
    val date: ZonedDateTime,
    val chat: JsonChat
) {
    var text: String? = null
    override fun toString(): String {
        return "MessageJson(messageId=$messageId, date=$date, chat=$chat, text=$text)"
    }
    // var from: User? = null // not needed probably
}

data class JsonChat(val id: Long, val username: String?)
