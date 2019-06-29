package tbp.land.telegram.client.dto

import java.time.ZonedDateTime

data class UpdateJson(
    val updateId: Long,
    val message: MessageJson?
)

data class MessageJson(
    val messageId: Long,
    val date: ZonedDateTime,
    val chat: ChatJson
) {
    var text: String? = null
    override fun toString(): String {
        return "MessageJson(messageId=$messageId, date=$date, chat=$chat, text=$text)"
    }
    // var from: User? = null // not needed probably

}

data class ChatJson(val id: Long, val username: String?)
