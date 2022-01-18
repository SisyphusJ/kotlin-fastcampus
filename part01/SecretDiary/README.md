### [runOnUiThread](#runonuithread)

### [handler & looper](#handler와-looper)

### [getMainLooper](#getmainlooper)

<br>

<br>

# SharedPreferences

<br>

안드로이드에서 데이터를 저장하는 방법으로는 여러가지가 있는데 그 중 하나인 **SharedPreferences**는 간단한 값을 저장할 때 주로 사용한다.

초기 설정 값이나 자동 로그인 여부 등 간단한 값을 저장할 때 DB를 사용하면 복잡하기 때문에 **SharedPreferences**를 사용하면 적합하다.

<br>

<br>

## getSharedPreferences 사용

<br>

```kotlin
val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
```

선언 방식은 다음과 같다

첫번째 파라미터에는 이름을 설정하고 두 번째 파라미터에는 모드를 설정한다. 모드 중 **PRIVATE**는 해당 액티비티 내에서만 사용가능하게 한다.

<br>

```kotlin
passwordPreferences.edit(true) {putString("password", passwordFromUser)}
```

<br>

다음은 Prefs에 데이터를 저장하는 코드이다. 위의 **true**는 commit을 설정하는 부분인데

Prefs를 수정할 때 edit를 사용하는데 edit에는 **commit**과 **aplly**가 있다.

**commit**은 동기적을 처리하는 것이고, **aplly**는 비동기적으로 처리한다.

(동기 방식은 요청을 보냈을 때 응답이 돌아와야 다음 동작을 수행할 수 있다. 따라서 다음 작업은 대기된다.<br> 비동기 방식은 요청을 보냈을 때 응답 상태와 상관없이 다음 동작을 수행할 수 있다.)

<br>

**commit에 경우 main Thread에서 사용하면 Thread를 block시키기 때문에 문제를 일으킬 수 있으니 유의해야 한다.**

<br>

동기와 비동기 방식을 설정한 후, String 데이터를 저장할 때는 **putString**을 사용하여 (key, value)를 통해 저장한다.

<br>

```kotlin
passwordPreferences.getString("password", "000")
```

<br>

prefs에서 데이터를 불러올 때는 저장할 때와 같이 getString에서 (key, value)를 통해 불러오면 된다.

<br>

<br>

<br>

# Thread

<br>

한 개의 프로세서에서는 많은 스레드를 동시에 처리할 수 있다. <br>
그리고 프로세스의 메모리 또한 공유한다. <br>
프로세서의 각 메모리를 할당 받은 스레드는 독립적으로 실행된다.

<br>

## UI Thread(Main Thread)

<br>

액티비티를 포함해 모든 컴포넌트가 실행되는 오직 1개만 존재하는 스레드.

따라서 UI 스레드에서는 제약사항이 몇가지 존재한다. <br>

-   화면의 UI를 그리는 처리를 담당한다.
-   UI와 상호작용하고, 이벤트 결과를 사용자에게 보여준다.
-   UI 이벤트 등 작업에 일정시간 동안 응답이 없으면 \*ANR 팝업을 표시한다.(ANR : 응용 프로그램이 응답하지 않는다.)

<br>

## Background Thread

<br>

UI 스레드가 UI 처리 및 사용자와 상호작용을 한다면,<br>
백그라운드 스레드는 그 나머지를 모두 맡아서 처리한다. <br>
예를 들어 네트워크 작업 및 파일 다운로드 등등 시간이 걸리는 작업에 해당한다.

<br>

## Runnable

<br>

Runnable은 Thread를 간소화한 형태이다. Thread는 클래스인 반면 Runnable은 **인터페이스**라서 다른 클래스를 상속하고 있는 클래스에 Runnable을 추가할 수 있고, 상속 형태 없이 익명 클래스 형태로 바로 써도 가능하다.

<br>

<br>

## runOnUiThread

<br>

**안드로이드는 UI 스레드가 아닌 스레드에서는 UI에 접근할 수 없다.** 따라서 스레드에서 처리한 작업을 반영하기 위해서는

runOnUiThread를 사용한다.

runOnUiThread는 현재 스레드가 UI 스레드이면 UI 자원을 사용하는 행동에 대해 즉시 실행하고,

현재 스레드가 UI 스레드가 아니라면 UI 스레드의 자원 사용 **이벤트 큐**에 들어간다.

```kotlin
 Thread(Runnable {

            db.historyDao().getAll().reversed().forEach {

                runOnUiThread {
                    val historyView =
                        LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = " = ${it.result}"

                    historyLinearLayout.addView(historyView)
                }


            }

        }).start()
```

<br>

## Handler와 Looper

<br>

**루퍼(Looper)** : Message Queue에서 메시지, Runnable 객체를 차례로 꺼내 핸들러가 처리하도록 전달한다.

<br>

**핸들러(Handler)** : 루퍼로부터 받은 메시지, Runnable 객체를 처리하거나 메시지를 받아서 Message Queue에 넣는 스레드 간의 통신장치이다.

UI Thread 외 다른 스레드들은 별도의 제어를 통해 UI부분을 다루어야 한다. <br>
따라서 UI Thread가 아닌 다른 스레드에서는 UI 부분을 다룰 때 핸들러를 통해 접근이 이루어진다.

핸들러는 각각의 스레드 안에 만들어질 수 있고, 다른 스레드에서 요청하는 정보를 메세지 큐를 통해 순서대로 실행시켜 줄 수 있기 때문에 리소스에 대한 동시 접근의 문제를 해결해 준다.

<br>

<br>

**메시지**는 루퍼의 큐에 값을 전달하기 위해 사용되는 클래스로 Message 객체에 미리 정해둔 코드를 입력하고, 큐에 담아두면 루퍼가 핸들러에 전달한다.

<br>

## getMainLooper

<br>

getMainLooper() 함수는 Main Thread(UI Thread)가 사용하는 Looper 즉 Main Looper를 반환한다.

이 함수는 호출하는 스레드가 메인 스레드이거나 메인 스레드가 아니어도 언제든지 Main Looper를 반환한다.

보통 이 함수는 3가지에 경우에 사용한다.

<br>

1. Handler를 생성할 때 Main Looper를 생성자의 인자로 전달하고 싶을 경우

    - 즉 작업 스레드에서 UI 스레드에게 **Runnable 작업 또는 메시지**를 보내고 싶을 때 사용한다.

    <br>

2. 현재 스레드의 루퍼가 Main Looper인지 아닌지 검사하고 싶을 경우

3. 현재 스레드가 Main thread인지 아닌지 검사하고 싶을 경우

<br>

```kotlin
private val handler = Handler(Looper.getMainLooper())
```

ScretDiary 챕터서는 UI에 접근하여 처리하기 위해 사용하였다.

<br>

<br>

<br>

<br>

```kotlin
val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }

            Log.d("DiaryActivity", "SAVE!!! ${diaryEditText.text.toString()}")
        }
```

```kotlin
handler.removeCallbacks(runnable)
handler.postDelayed(runnable, 500)
```

Runnable 객체로 스레드에서 작동할 기능을 구현하고, handler를 통해 진행되고 있는 runnable를 제거 후

0.5초 딜레이 후 다시 runnable를 실행한다.
