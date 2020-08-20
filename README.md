# Duplicacy Utils Telegram Bot
A telegram bot used by my [duplicacy-utils](https://github.com/TheBestPessimist/duplicacy-utils) scripts.


# How to use with [duplicacy-utils](https://github.com/TheBestPessimist/duplicacy-utils)?

Update duplicacy-utils to latest version then just follow the instruction from [@DuplicacyUtilsTBPBot](https://t.me/DuplicacyUtilsTBPBot). There's only one step needed :^).


# How to use from other scripts

You can easily use this bot to notify you in your own scripts. 
The steps needed are as follows:
 
1. get the `telegramToken` from [@DuplicacyUtilsTBPBot](https://t.me/DuplicacyUtilsTBPBot)
2. send a `Post` request to `https://duplicacy-utils.tbp.land/userUpdate` with the `body` containing a `json` encoded string with 2 fields:
    - `content` containing a string with your message
    - `chat_id` containing the telegram token from before
3. telegram should show you a notification now

### Example of usage in powershell:
```powershell
$payload = @{
  content = "Write anything <code>>>lalalal &m_a5</code> $#@%&**&^% <i>you</i> may <b>want</b>"
  chat_id = 1234567890
  }

Invoke-WebRequest `
  -Body (ConvertTo-Json -Compress -InputObject $payload) `
  -ContentType 'application/json' `
  -Method Post `
  -Uri "https://duplicacy-utils.tbp.land/userUpdate"
```

# More usage details

More details and screenshots are found on the [Duplicacy Forum](https://forum.duplicacy.com/t/send-backup-notifications-via-telegram-using-duplicacy-utils-telegram-bot/1692)



