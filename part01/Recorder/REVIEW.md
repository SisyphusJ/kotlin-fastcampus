# CustomView

<br>

기존의 뷰를 가지고 사용자 정의대로 원하는 뷰를 만들 수 있다.

## **chapter Recorder/RecorderButton**

<br>

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

## **chapter Recorder/SoundVisualizerView**

### CustomView를 만들어서 직접 View를 그리기

<br>

```kotlin
// 들어온 값이 int로 변환
var onRequestCurrentAmplitude: (() -> Int)? = null

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

즉, 특정 객체를 생성하면서 함께 호출해야 하는 초기화 코드가 있는 경우 사용한다.

<br>

**Canvas**는 2차원적인 오브젝트를 화면에 그리는 것을 돕는 라이브러리이다. 그리고 **Paint**는 그리는 것을 어떻게 꾸밀 것인지 묘사한다.

<br>

<br>

```kotlin
private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            // Amplitude, Draw
            if (!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            invalidate()

            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }
```

<br>

**invoke**함수는 이름 없이 간편하게 호출될 수 있는 함수이다.
invoke 함수가 정의만 되어있으면 이름으로 호출하지 않고도 호출하여 사용할 수 있다.

# ENUM CLASS

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
