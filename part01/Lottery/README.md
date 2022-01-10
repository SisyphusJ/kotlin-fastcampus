# kotlin collection 개념

## immnutable(불변) VS mutable(가변)

<br>

Collection는 대부분의 프로그래밍 언어에 있는 자료구조이다. 자바의 List, Map, Set 등을 Collection이라고 한다.

<br>

Collection은 Generic으로 구현이 되어 다양한 타입과 함께 사용될 수 있다.

<br>

Collection은 기본적으로 **Mutable(변할 수 없는)** 과 **Immutable(불변의)** 을 별개로 지원한다. Mutable로 생성하면 추가, 삭제가 가능하지만, Immutable로 생성하면 수정이 안된다.

<br>

<img src="https://codechacha.com/static/a8a4ee897d3ad2694f914c10d6aad695/efc66/collections-diagram.png" width = "500"/>

<br>

Collection의 상속 구조

## List

<br>

List는 데이터가 저장하거나 삭제될 때 순서를 지키는 Collection이다. List는 mutable, immnutable 모두 지원한다.

<br>

### ImmutableList<T>

<br>

listOf<타입>(아이템,)로 Immutable List를 생성 및 초기화를 할 수 있다. 타입은 생략해도 된다.

Immutable이기 때문에 **get**만 가능하다. List의 getter는 자바처럼 get(index)도 지원하고 배열처럼 [index]도 지원한다.

<br>

### MutableList<T>

<br>

mutableListOf로 선언한다. listOf와 대부분 비슷하지만 추가 및 삭제가 가능하다.

remove,add,addAll,removeAt 등의 메소드를 사용할 수 있다.

<br>

## Set

<br>

Set은 동일한 아이템이 없는 Collection이다. Set의 아이템들의 순서는 특별히 정해져 있지 않다.

Set는 null 객체를 갖고 있을 수 있다. **동일한 객체는 추가될 수 없기 때문에** null도 1개만 갖고 있을 수 있다. 타입은

Immutable과 Mutable을 별개로 지원한다.

<br>

### Set : Immutable

<br>

setOf<타입>(아이템들)로 객체를 생성할 수 있다.

forEach 또는 Iterator 등으로 모든 객체를 탐색할 수도 있다.

<br>

### Set : Mutable

Mutable은 mutableSetOf<타입>(아이템들)로 생성할 수 있다. Mutable이기 때문에 추가, 삭제가 가능하다.

List와 비슷한 메소드들을 지원한다.

<br>

## Map

<br>

Map은 key와 value를 짝지어 저장하는 Collection이다. Map의 key는 유일하기 때문에 중복은 허용되지 않는다.

Map 또한 Immutable과 Mutable을 별개로 지원한다.

<br>

### Map : Immutable

<br>

Map은 mapOf<key type, value type>(아이템)으로 생성한다.

아이템은 Pair객체로 표한하며, Pair에 key와 value를 넣을 수 있다. **Pair(A, B)** 는 **A to B** 로 간단히 표현이 가능하다.

getter는 get(index)와 [index]를 모두 지원한다. (코틀린은 배열 방식을 선호한다.)

keys와 values는 key와 value만으로 구성된 Set을 리턴해준다.

<br>

### Map : Mutable

<br>

mutableMapOf<key type, value type>(아이템)로 생성한다.

객체 추가는 put 메소드이며, Pair를 사용하지 않고 인자로 key와 value를 넣어주면 된다.

put도 배열 방식을 지원한다. 그 외에는 자바의 Map과 유사하다

<br>

<br>

<br>

---

<br>

### chainStyle

<br>

contraintLayout에서 각 뷰간의 상호 참조 연결을 할 때 뷰들을 어떠한 방식으로 표현할 지 정한다.

**뷰 사이의 간격을 조정할 때 유용하다**

<br>

### val, var

**val : immutable 변수로 지정한다. == final** <br>
**var : mutable 변수로 지정한다. 값을 변경할 수 있다.**

<br>

<br>

## lateinit, lazy by

<br>

코틀린에서는 변수 선언에서 Nullable 변수 선언부터 엄격하게 관리한다.

변수를 선언할 때도 Nuullsafe 지시자를 표시해야 하며 그렇지 않으면 컴파일 에러가 뜬다.

### lateinit

**lateinit**을 사용하면 변수의 값을 지정하는 작업을 뒤로 미룰 수 있다. **Nullable 하지 않은 변수를 선언하면서 Assign 하는 작업을 뒤로 미루고 싶을 때는 lateinit 키워드를 사용하면 가능하게 된다.**

lateinit은 mutable 변수만 가능하기 때문에 var 키워드를 가진 변수에서만 사용이 가능하다.

<br>

### by lazy

**by lazy** 키워드는 lateinit와 비슷하게 값을 지정하는 작업을 미루는 작업인데 **할당되는 시점이 변수를 호출하는 시점이다.**

by lazy는 immutable 변수에서만 적용이 가능해 val 키워드 변수에만 적용이 가능하다.

<br>

<br>

## 반환 지정

<br>

onClickListener와 같이 **람다식**을 사용할 경우는 return 이 클릭리스너에 대한 것인지 함수에 대한 것인지 구분하기가 어려운데

이럴때 return 의 대상을 명시하기 위해서 @(at) 을 붙힌다.
\*return@(어디)클릭리스너 이런식으로 반환 지정을 한다.

<br>

<br>

---
