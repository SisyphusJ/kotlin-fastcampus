# FCM

FCM은 FirebaseCloudMessaging을 말한다.

<br>

<img src="https://i.ytimg.com/vi/sioEY4tWmLI/maxresdefault.jpg">

<br>

FCM은 무료로 메시지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션으로서 <br>
FCM을 사용하면 새 이메일이나 기타 데이터를 동기화할 수 있음을 클라이언트 앱에 알릴 수 있다.

<br>

<br>

## Token

토큰은 사용자 인증을 위한 정보를 서명한 것이다. 세션 기반의 인증은 클라이언트의 상태를 서버 내의 저장하며,

요청마다 세션 스토리지에 유효한 세션인지 확인해야 한다. 반면 토큰 기반의 인증은 토큰에 사용자 인증을 위한 정보가 담겨있기 때문에 <br>
서버에 사용자 정보를 저장하지 않고, 전달받은 토큰의 서명과 데이터를 검증하는 것만으로도 인증이 가능하다.

<br>

<img src="https://media.vlpt.us/images/kingth/post/10d2beae-96a9-478d-bfd9-258d06941946/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202021-09-06%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2011.31.33.png">

[이미지 출처](https://media.vlpt.us/images/kingth/post/10d2beae-96a9-478d-bfd9-258d06941946/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202021-09-06%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2011.31.33.png)

<br>

사용자 인증이 완료되면 서버는 비밀키 또는 공개/개인 키를 이용해 서명한 토큰을 클라이언트에 전달한다. 그리고 데이터 요청 시 클라이언트는 토큰을 포함한다.(주로 헤더) 서버는 토큰의 서명 값을 이용하여 토큰이 유효한지 검증한다.<br> 유효한 토큰일 경우 요청에 응답한다. 토큰 발급 시 토큰 내 권한 정보를 추가하여 권한에 맞는 데이터 응답도 가능하다.

<br>

<br>

# FCM 사용하기

<br>

## 1. 종속성 추가(gradle)

```kotlin
    // 푸시 라이브러리
    implementation 'com.google.firebase:firebase-messaging-ktx'
    // JSON의 자바 오브젝트의 직렬화, 역직렬화
    implementation 'com.google.code.gson:gson:2.8.5'
    // okhttp3
    implementation 'com.squareup.okhttp3:okhttp:3.4.1'

```

<br>

## 2. Manifest에 서비스 추가

```kotlin
<service
            android:name=".MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
```

<br>

## 3. FirebaseMessagingService 구현

<br>

### onMessageReceived

```kotlin
// part02/PushAlarm/MyFirebaseMessagingService.kt

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    // 메시지가 수신되면 호출된다.
    // remoteMessage = 받은 데이터
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 버전 예외처리
        createNotificationChannel()

        // type이 null이 아닐 경우 해당 type의 값을 가져온다.
        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }

        val title = remoteMessage.data["title"]

        val message = remoteMessage.data["message"]

        // null이면 return
        type ?: return

        // 알림 호출
        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }
```

<br>

### PendingIntent

PendingIntent는 기본적으로 Intent를 가지고 있는 클래스이다.

**'Pending'** 은 '보류','임박'과 같은 의미를 가지고 있는 단어이다.

PendingIntent는 **Intent를 당장 수행하지 않고, 특정 시점에 수행하도록 하는** 특징을 가지고 있다. 이 **특정 시점**은 보통 해당 앱이 구동되고 있지 않을 때이다.

<br>

PushAlarm에서 PendingIntent를 사용하는 핵심은 앱이 구동되고 있지 않을 때 다른 프로세스에게 권한을 허가하여 Intnet를 마치 본인 앱에서 실행되는 것처럼 사용하게 하는 것이다.

따라서 시스템의 notificationManager가 Intent를 실행하는데 이는 해당 앱이 아닌 다른 프로세스에서 수행하는 것이므로 PendingIntent를 사용하는 것이다.

```kotlin

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?,
    ): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // 액티비티 중복 방지
        }

        // 인텐트의 패턴을 체크
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID) // channel ID
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title) // 제목
            .setContentText(message) // 내용
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 오레오 이하 버전에서는 지정 필요
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        when (type) {
            // 일반형 알람
            NotificationType.NORMAL -> Unit // no Return

            // 확장형 알람인 경우
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            // 이모티콘 코드 생략
                        )
                )

            }

            // 커스텀 알람인 경우
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification // 커스텀한 layout 파일 지정
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )

            }
        }

        return notificationBuilder.build()
    }
```

<br>

```kotlin

    // 채널을 생성해주지 않으면 앱에서는 푸시알림을 받지않음
    // Oreo 버전 예외처리
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "channel Id"
    }
}
```
