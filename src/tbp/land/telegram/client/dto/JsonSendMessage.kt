package tbp.land.telegram.client.dto

/**
 * Reference: https://core.telegram.org/bots/api#sendmessage
 */
data class JsonSendMessage(
    val chatId: Long,
    val text: String,
    val replyToMessageId: Long? = null
) {
    val parseMode = "HTML"
    val disableWebPagePreview = true
}
