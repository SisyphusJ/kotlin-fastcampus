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

## Handler와 Looper

<br>

루퍼(Looper) : Message Queue에서 메시지, Runnable 객체를 차례로 꺼내 핸들러가 처리하도록 전달한다.

핸들러(Handler) : 루퍼로부터 받은 메시지, Runnable 객체를 처리하거나 메시지를 받아서 Message Queue에 넣는 스레드 간의 통신장치이다.

<br>

**루퍼**는 MainActivity가 실행됨과 동시에 for문 하나가 무한루프 돌고 있는 서브 스레드라고 생각하면 된다. 이 무한루프는 대기하고 있다가 자긴의 큐에 쌓인 메시지를 핸들러에 전달한다.

**핸들러**는 루퍼를 통해 전달되는 메시지를 받아서 처리하는 일종의 명령어 처리기로 사용된다. 앱이 실행되면 자동으로 생성되어 무한루프를 도는 루퍼와 달리 핸들러는 개발자가 직접 생성해서 사용해야 한다.

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
