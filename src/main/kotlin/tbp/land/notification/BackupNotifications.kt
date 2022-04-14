package tbp.land.notification

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tbp.land.telegram.Telegram
import tbp.land.telegram.forwardBackupNotification


const val BACKUP_NOTIFICATION_ROUTE_1 = "/v2/userUpdate"
const val BACKUP_NOTIFICATION_ROUTE_2 = "userUpdate"

fun Routing.backupNotificationRoute(backupNotificationRoute: String, telegram: Telegram) {
    post(backupNotificationRoute) {
        val backupNotification: JsonBackupNotification = call.receive()
        telegram.forwardBackupNotification(backupNotification)
        call.respond(HttpStatusCode.OK)
    }
}

// BackupNotification stores a notification received from the backup script.
// It will be sent to the ChatId telegram chat.
data class JsonBackupNotification(val chatId: Long, val content: String)
