# Room Database

<br>

안드로이드 에서 내부 저장소를 사용해야 할 때 2가지를 사용한다. <br>

-   SQLite : 파일형식으로 데이터를 저장하며, 소규모 데이터를 관리하는데 적합한 관계형 DB <br> 보통 임베디드 기기, IoT, 응용프로그램 파일 형식, 웹사이트 등에 사용된다. <br><br>
-   Room : 스마트폰 내장 DB에 데이터를 저장하기 위해 사용하는 ORM(Object Relational Mapping)라이브러리

Room은 ORM 라이브러리로 DB 데이터를 Java 또는 kotlin 객체로 맵핑해준다.

Room은 SQLite를 내부적으로 사용하고 있지만, DB를 구조적으로 분리하여 데이터 접근의 편의성을 높여주고 유지보수에 편리하다.

Annotation : 데이터를 위한 데이터를 의미하며, 데이터에 의한 설명을 의미하는 데이터다.

<br>

<br>

<img src="https://developer.android.com/images/training/data-storage/room_architecture.png?hl=ko">

[이미지 출처]("https://developer.android.com/images/training/data-storage/room_architecture.png?hl=ko")

<br>

Room 라이브러리는 Entity, DAO(데이터 접근 객체), DB로 구성되어 있다.

<br>

## Entity

<br>

DB 내의 저장할 데이터 형식으로 class의 변수들의 column이 되어 table이 된다.

<br>

### Annotation

<br>

**Entity(tableName = StudentEntity.TABLE_NAME)** : Table 이름을 선언한다. (entity class 이름을 database table 이름으로 인식)

<br>

**@PrimaryKey** : 각 entity는 1개의 primary key를 가진다.

<br>

**ColumnInfo** : Table 내 column을 변수와 매칭한다.

<br>

<br>

## DAO

<br>

데이터베이스에 접근하여 수행할 작업을 메소드 형태로 정의한다.(SQL 쿼리(DB에 정보를 요청하는 것) 지정 가능)

<br>

### Annotation

<br>

**@insert** : Entity set 삽입. @Entity로 정의된 class만 인자로 받거나, 그 class의 collection 또는 array만 인자로 받을 수 있다. 인자가 하나인 경우 long type을 받을 수 있고, 여러 개인 경우 long[], List로 받을 수 있다.

<br>

**@update** : Entity set 업데이트. Return 값으로 업데이트 된 행 수를 받을 수 있다.

<br>

**@delete** : Entity set 삭제. Return 값으로 삭제된 행 수를 받을 수 있다.

<br>

**@query** : DB를 조회한다.

<br>

<br>

## Room DB

<br>

데이터베이스의 전체적인 소유자 역할, DB 생성 및 버전 관리

Entity만큼 정의된 Dao 객체들을 반환할 수 있는 함수들을 가지고 있는 추상 클래스 형태로 정의

(추상클래스 : 실체 클래스의 공통적인 부분(변수, 메소드)를 추출해서 선언한 클래스이다. (객체 생성 불가능.))

<br>

### Annotation

<br>

**@Database** : class가 Database임을 알려주는 어노테이션

-   entities : 이 DB에 어떤 테이블이 있는지 명시한다.
-   version : Scheme(데이터베이스의 구조와 제약조건에 관해 전반적인 명세를 기술한 것)가 바뀔 때 이 version도 바뀌어야한다.
-   exportSchema : Room의 Schema 구조를 폴더로 Export 할 수 있다.

<br>

<br>

<br>

## Room 사용

<br>

### 1. Gradle 설정

```kotlin
implementation 'androidx.room:room-runtime:[버전]'
annotationProcessor 'androidx.room:room-compiler:[버전]'
```

<br>

### 2. Entity class 생성

Room에서는 class로 Table을 명시할 수 있다. 따라서 데이터 모델인 Entity를 만들어야 한다.

Annotation으로 Entity를 추가해서 class를 만든 후, 해당 클래스에 들어갈 Entity들을 정의해준다.

여기서 각 **Entity들은 PrimaryKey가 반드시 필요한데**, autoGenerate = true를 하면 키가 자동으로 생성된다.

<br>

```kotlin

@Entity
data class History(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "expression") val expression: String?,
    @ColumnInfo(name = "result") val result: String?
)
```

<br>

### 3. DAO 생성

<br>

DB에 접근해 query, insert, delete 등을 수행할 메소드를 포함하여 만든다.

<br>

```kotlin
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

}
```

<br>

### 4.Database

데이터베이스의 정의는 @Database로 Database 클래스임을 명시하며, RoomDatabase을 상속받아 abstract로 선언하며 abstract의 Dao를 가지고 있다.

<br>

```kotlin
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}
```

<br>

### 5. Activity에서 Room 접근

<br>

```kotlin
lateinit var db: AppDatabase

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    .
    .
    .

    Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()
```

lateinit : 늦은 초기화. property 초기화를 미루고 이후 값을 대입해 사용하는 방법

[Room Databse 개념 출처]("https://velog.io/@ryalya/Android-DB-Room%EC%9D%B4%EB%9E%80")

<br>

<br>

<br>

<br>

# 확장 함수

<br>

코틀린은 클래스를 확장해서 새로운 기능을 개발할 수 있도록 지원한다.

이 확장 기능은 다른 클래스로부터 상속을 받지 않아도 되고, 어떤 디자인 패턴을 이용해서 확장하는 것도 아니다.

클래스 확장 개념을 사용하면 **외부 라이브러리가 제공하는 자체 클래스는 변경할 수 없지만 이를 확장하며 개발자가 원하는 새로운 함수들을 만들 수 있게 된다. <br> 또 클래스를 확장하면서 만든 새로운 함수들을 마치 외부 라이브러리 클래스가 제공하는 함수인것 처럼 사용할 수 있다.**

```kotlin
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
```

calculator 챕터의 확장 함수

<br>

<br>

<br>

---
