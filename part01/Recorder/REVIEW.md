# 1. Request Mic runtime permissions

음성 녹음을 위해 마이크 권한이 필요한데 이를 얻기 위해 Manifest에서 권한 요청 코드를 추가한다.

```kotlin
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

이후 MainActivity에서 배열에 음성 녹음 권한을 담는다.

```kotlin
private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)
```

companion object에서 상수로 오디오 권한의 코드를 따로 정의한다.

```kotiln
companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
```

<br>

사용자에게 요청할 권한을 리스트로 담아 **requestPermissions**으로 요청한다.

그리고 그에 대한 답변은 **onRequestPermissionsResult**를 재정의하여 처리한다.

```kotlin
private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }




 override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED


        // 권한 요청을 수락하지 않을 시 종료된다.
        if (!audioRecordPermissionGranted) {
            finish()
        }
    }

```

<br>

<br>

# 2. 상태변화에 따른 아이콘 변경

## ENUM CLASS

<br>

enum 클래스는 상수를 집합으로 관리할 때 사용한다. 이를 사용하면 코드에 가독성이 높아진다.

(굳이 인스턴스 생성과 상속을 신경쓸 필요가 없다.)

```kotlin
 enum class State {

    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
    ON_PLAYING

}
```

enum 클래스에서 상태를 정의한 후 RecorderButton 클래스에서 녹음 버튼을 구성한다.

<br>

## 커스텀 뷰 (**chapter Recorder/RecorderButton**)

```kotlin
class RecordButton(
    context: Context,
    attrs: AttributeSet,
) : AppCompatImageButton(context, attrs) {
```

<br>

생성자를 구성할 때 필요한 파라미터는 다음과 같다.

-   **Context** : Activity, application을 구별하는 정보
-   **AttributeSet** : View를 생성할 때 사용하는 속성들에 대한 정보가 있는 곳

<br>

그리고 ':'뒤에 정의되는 리턴타입은 AppCompatImageButton()으로 정의한다. (**AppCompat : 이전 버전의 안드로이드도 UI를 사용할 수 있게 하는 라이브러리**)

<br>

<br>

```kotlin
fun updateIconWithState(state: State) {
        when (state) {
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_record)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_stop)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop)
            }
        }
    }
```

커스텀뷰 클래스의 함수 updateIconWithState : 상태 조건에 따라 아이콘을 변경한다.

코틀린의 함수 선언에서 파라미터 안의 ':'뒤의 **State**는 타입을 명시한다.

<br>

<br>

MainActivity로 돌아와 state 변수를 생성한 후 정의한 enum 클래스로 초기화를 한다.

이와 함께 setter를 설정하여 값이 설정될 때마다 정의한 updateIconWithState를 호출한다.

```kotiln
private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
            recordButton.updateIconWithState(value)
        }
```

<br>

### **Getter, Setter**

<br>

코틀린에서 최상위 변수(함수나 클래스 외부에 정의된 변수)나 클래스 멤버 변수로 선언하면 속성으로 간주된다.

<br>

클래스의 멤버 변수는 모두 **private** 제한자로 지정된다. 따라서 속성에 접근하기 위해서는 getter와 setter를 사용해야 한다.

-   **getter** : 클래스 속성의 값을 가져온다. val로 변수를 선언했을 때만 사용할 수 있다.
-   **setter** : 클래스 속성의 값을 변경한다.

**field** 키워드는 get()와 set() 내부에서 속성값을 직접 가져오거나 변경할 수 있고, 또 내부에서 게터와 세터가 호출되지 않도록 해준다. **따라서 속성을 참조할 때 발생하는 재귀 호출을 막을 수 있다.**

[출처](https://bbaktaeho-95.tistory.com/27)

<br>

<br>

# 3. MediaRecorder 사용

음성녹음을 할 때 사용하는 MediaRecorder는 사용자의 마이크를 통해서 녹음이 되도록 도와준다.

```kotiln
 private var recorder: MediaRecorder? = null
```

MediaRecorder를 null로 선언하는 이유는 사용하지 않을 때도 활성화가 되어있으면 부족한 메모리에 의한 성능저하가 일어날 수 있기 때문이다.

## 초기화 및 녹음

<br>

```kotiln
private fun startRecording() {
        // 초기화
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)

            // 포맷 설정
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

            // 인코더 설정
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            // 로컬에 영구저장이 아닌 캐시에 저장한다.
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        soundVisualizerView.startVisualizing(false)
        recordTimeTextView.startCountUp()
        state = State.ON_RECORDING
    }



private fun stopRecording() {
        recorder?.run {
            stop()

            // 메모리에서 해제
            release()
        }
        recorder = null
        soundVisualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
        state = State.AFTER_RECORDING
    }
```

<br>

<br>

# 4. 커스텀 뷰로 음성 오디오 시각화

<br>

**Canvas**는 2차원적인 오브젝트를 화면에 그리는 것을 돕는 라이브러리이다. 그리고 **Paint**는 그리는 것을 어떻게 꾸밀 것인지 묘사한다.

일단 필요한 상수 값을 정의한다.

```kotlin
companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F

        // 오디오 레코더의 get max amplitude 음성의 최대값의 short 타입 최대값
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat() // Float으로 타입 변환
        private const val ACTION_INTERVAL = 20L // 20 밀리초
    }
```

음성 진폭을 구성한다.

```kotlin

// 음향 진폭을 시각화 한다.
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        // 라인의 양 끝을 Round처리
        strokeCap = Paint.Cap.ROUND
    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0

    // 리스트로 진폭을 저장하고 드로잉에 쓰기위한 리스트
    private var drawingAmplitudes: List<Int> = emptyList()

    // 다시 플레이를 할 때도 시각화를 위해 선언한다.
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0
```

**apply**는 함수를 호출하는 객체를 이어지는 블록의 리시버로 전달하고, 객체 자체를 반환한다. 리시버란, 바로 이어지는 블록 내에서 메소드 및 속성에 바로 접근할 수 있도록 할 객체를 의미한다.

**즉, 특정 객체를 생성하면서 함께 호출해야 하는 초기화 코드가 있는 경우 사용한다.**

<br>

```kotlin
override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }
```

onSizeChanged를 재정의하여 사이즈가 변경되는 경우 해당 너비와 높이를 가져와 재설정한다.

<br>

```kotlin
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // canvas가 null 인경우 반환
        canvas ?: return

        val centerY = drawingHeight / 2f // view의 center 높이
        var offsetX = drawingWidth.toFloat() // right

        // 진폭값을 배열로 만들고 오른쪽부터 그려지게 한다.
        drawingAmplitudes
            .let { amplitudes ->
                if (isReplaying) {
                    amplitudes.takeLast(replayingPosition) // 뒤부터
                } else {
                    amplitudes
                }
            }

            .forEach { amplitude ->

                // 그릴려는 높이 대비 퍼센트로 draw
                val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F

                // offset 다시 계산
                offsetX -= LINE_SPACE

                // 진폭 배열이 뷰를 초과하는 경우 그리지 않는다.
                if (offsetX < 0) return@forEach


                // amplitude 그리기
                canvas.drawLine(
                    offsetX,
                    centerY - lineLength / 2F,
                    offsetX,
                    centerY + lineLength / 2F,
                    amplitudePaint
                )

            }

    }
```

<br>

onDraw에서는 Paint 객체를 이용하여 맞는 진폭 막대를 그리는데 우측부터 그려져야 하므로 우측 좌표를 offsetX로 가지고 있다가

하나씩 그리면서 LINE_SPACE 만큼 빼주면서 그리게 된다.

이때 뷰의 영역을 넘어가는 경우 return하여 그리지 않게 한다.

진폭을 그릴 때 drawingAmplitudes 리스트에 있는 진폭 값들을 하나씩 가져오는데 이 진폭 값은 콜백 함수를 통해 MainActivity에서 녹음하는 동안 계속 받아와야 한다.

<br>

<br>

```kotlin
// void (SoundVisualizerView.kt)
 var onRequestCurrentAmplitude: (() -> Int)? = null

// MainActivity
soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }
```

ViewBinding을 할 때 해당 콜백함수를 정의하는데 recorder가 null이 아니면 최대 amplitude를 return한다. 최대 진폭값이 0이면 null을 리턴한다.

<br>

```kotlin
private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            // Amplitude, Draw
            if (!isReplaying) {
                // amplitude 값 가져오기
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                // 오른쪽부터 draw를 그리기 위해 순차적으로 정보를 쌓는다/
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            // 뷰 갱신
            invalidate()

            // 지연 발생
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }
```

<br>

녹음을 시작하면 오디오 시각화를 실시간으로 해줄 runnable 객체를 post 해서 일정 인터벌 당 계속 호출되도록 설정한다.

이에 따라 진폭값을 실시간으로 가져와서 현재 리스트의 앞에 붙여 순차적으로 그리게 한다.

**invoke** 연산자는 함수타입의 인스턴스를 호출할 때 사용한다.

```kotlin
var onRequestCurrentAmplitude: (() -> Int)? = null
```

이렇게 함수의 파라미터에 람다가 포함되어 있는 함수를 **고차함수**라고 부르는데 invoke는 이 파라미터에게 호출에 대한 정보를 전달하게 된다.

<br>

<br>

# 5. 오디오 타임스탬프 구현

<br>

```kotlin
package com.sysiphusj.aop.part01.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {

    private var startTimeStamp: Long = 0L

    private val countUpAction: Runnable = object:Runnable {
        // 시작했을 때 타임 스탬프 계산
        override fun run() {
            val currentTimeStamp = SystemClock.elapsedRealtime()

            val countTimeSeconds = ((currentTimeStamp - startTimeStamp)/1000L).toInt() // 시간 차이 계산
            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }


    fun startCountUp() {
        startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp() {
        handler?.removeCallbacks(countUpAction)
    }

    fun clearCountTime() {
        updateCountTime(0)
    }

    @SuppressLint("SetTextI18n")
    private fun updateCountTime(countTimeSeconds: Int) {
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }
}

```

카운트 시작을 기준으로 얼마나 시간이 흘렀는데 1초간 반복하여 갱신하는 코드이다.

카운트를 하지 않는 경우에는 handler의 removeCallBacks를 통하여 콜백함수를 제거한다.(계속 실행되는 것을 방지한다.)

<br>

[내용 출처](https://whyprogrammer.tistory.com/584)

---
