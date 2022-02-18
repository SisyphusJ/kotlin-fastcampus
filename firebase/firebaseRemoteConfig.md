# RemoteConfig

with Part02 DailyQuotes

<br>

<img src="https://i.ytimg.com/vi/_CXXVFPO6f0/maxresdefault.jpg" width = 400px>

<br>

RemoteConfig는 **앱에서 별도의 업데이트를 하지 않아도 앱의 동작과 모양을 변경할 수 있는** 클라우드 서비스이다.

<br>

## 종속성 추가

```kotlin
implementation platform('com.google.firebase:firebase-bom:29.0.4')

// remoteconfig를 위한 종속성
implementation 'com.google.firebase:firebase-config-ktx'


// Firebase에서 대쉬보드를 통해 Firebase가 제공하는
// 서비스 상태를 확인할 수 있는 종속성이다.
implementation 'com.google.firebase:firebase-analytics-ktx'
```

<br>

## 앱에서 적용

```kotlin
private fun initData() {
        val remoteConfig = Firebase.remoteConfig

        // remoteConfig에 세팅
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                // Fetch에 대한 인터벌을 준다.
                minimumFetchIntervalInSeconds = 0
            }
        )
        // 비동기 방식으로 데이터를 가져온다.
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 로딩 화면 종료
            progressBar.visibility = View.GONE

            if (it.isSuccessful) {

                // response가 들어오면 viewPager에 연결
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }
```

<br>

위의 코드에서 remoteConfig를 세팅하는 과정이 있는데 여기서 **minimumFetchIntervalInSeconds()** 를 사용해야 한다.

**minimumFetchIntervalInSeconds()** 는 remoteConfig을 통해서 한번 가져온 데이터는 인터벌로 정한 시간동안 다시 가져오지 않게 하는 함수이다.

Firebase는 앱에서 단기간에 연속해서 fetch를 하면 호출이 제한된다. 혹시 서비스를 할 때 호출이 block되는 것을 방지하기 위해 위의 **minimumFetchIntervalInSeconds()** 를 사용한다.

(**fetch**는 api를 불러오고, 정보를 내보내는 함수이다. 안에는 get과 put이 있다.)

<br>
