# BobFinder

`밥차장`은 현재 위치를 기준으로, 사용자가 선택한 인원 수, 음식 카테고리, 예산에 맞는 식당과 메뉴를 추천해주는 Android 앱입니다. 사용자는 간단한 선택만으로 오늘 먹을 식당을 추천받고, 추천된 식당의 대표 메뉴와 가격대를 확인한 뒤 지도에서 위치까지 볼 수 있습니다.

## 프로젝트 개요

- 앱 이름: 밥차장
- 플랫폼: Android
- 언어: Java
- 최소 SDK: 23
- 타깃 SDK: 31
- 빌드 시스템: Gradle
- 주요 기술: Google Maps SDK, 위치 권한 기반 GPS, `HttpURLConnection`, AsyncTask

## 핵심 기능

- 현재 위치 권한 요청 및 GPS 기반 위치 확인
- 인원 수 선택
- 음식 카테고리 선택
- 1인 기준 예산 선택
- 조건에 맞는 식당 추천
- 추천 식당의 메뉴와 가격 정보 표시
- 사용자 예산 범위에 맞는 메뉴 우선 노출
- 지도에서 내 위치와 식당 위치 함께 확인

## 사용자 흐름

1. 앱 실행 후 시작 버튼 선택
2. 함께 먹는 인원 수 선택
3. 음식 카테고리 선택
4. 예산 선택
5. 추천 식당 1곳 확인
6. `메뉴보기`에서 해당 식당의 대표 메뉴와 가격 확인
7. `맵 보기`에서 식당 위치와 현재 위치 확인

## 추천 기준

코드 기준으로 추천 흐름은 아래처럼 동작합니다.

- 사용자가 선택한 값
  - 인원 수: `1`~`4`
  - 카테고리: `한식`, `양식`, `중식`, `일식`, `아시안`, `비건/샐러드`
  - 예산: `6,000`, `8,000`, `10,000`, `12,000`, `15,000`
- 앱이 위치 정보를 읽은 뒤 백엔드에 카테고리/예산/인원 정보를 전송
- 백엔드가 반환한 식당 중 1곳을 `오늘의 메뉴는` 화면에 표시
- 식당 상세 화면에서는 메뉴 가격이 아래 범위에 맞으면 우선 노출
  - 1인 예산의 `80% ~ 100%`
  - 또는 `인원 수 x 예산` 기준 `80% ~ 100%`

## 화면 구성

- `MainActivity`
  - 앱 시작 화면과 위치 권한 요청을 담당합니다.
- `menuselect`
  - 인원, 카테고리, 금액대를 `NumberPicker`로 선택합니다.
- `todaymenu`
  - 조건에 맞는 식당 1곳을 추천받아 보여줍니다.
- `menuboard`
  - 추천 식당의 메뉴 3개와 가격을 표시합니다.
- `openmap`
  - 식당 위치와 현재 위치를 지도에 마커로 표시합니다.

## 지도 및 위치 처리

- 앱은 `ACCESS_FINE_LOCATION` 권한을 요청합니다.
- GPS 기반 위치를 읽어 사용자의 현재 좌표를 확보합니다.
- 식당 주소는 Geocoding API 응답을 통해 위도/경도로 변환됩니다.
- 지도 화면은 현재 코드 기준 `Naver Map`이 아니라 `Google Maps SDK`를 사용합니다.

## 네트워크 구조

앱은 두 종류의 외부 연동을 사용합니다.

- 식당/메뉴 추천용 백엔드 API
- Google Geocoding API

다만 현재 저장소의 코드에는 백엔드 요청 주소가 `something` 으로 남아 있습니다. 따라서 백엔드 실제 URL을 복원하거나 교체하지 않으면 추천 기능은 그대로 실행되지 않습니다.

또한 지도 관련 메타데이터는 Google Maps 키를 사용하도록 설정되어 있습니다.

## 프로젝트 구조

```text
BobFinder/
├── README.md
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/edu/skku/testapp/bobfinder/
│       │   ├── MainActivity.java
│       │   ├── menuselect.java
│       │   ├── todaymenu.java
│       │   ├── menuboard.java
│       │   ├── openmap.java
│       │   ├── Category.java
│       │   ├── Price.java
│       │   └── People.java
│       └── res/
├── build.gradle
└── settings.gradle
```

## 실행 방법

### Android Studio에서 실행

1. Android Studio로 프로젝트 루트를 엽니다.
2. Gradle Sync를 진행합니다.
3. 위치 권한을 사용할 수 있는 Android 기기 또는 에뮬레이터를 준비합니다.
4. `app` 모듈을 실행합니다.

## 개발 메모

- 앱 이름은 리소스 기준 `밥차장`입니다.
- 시작 화면과 선택 화면은 비교적 가벼운 단일 액티비티 흐름으로 구성되어 있습니다.
- 네트워크 요청은 Retrofit 같은 라이브러리 대신 `HttpURLConnection`과 `AsyncTask`로 처리합니다.
- 지도 화면은 식당 위치와 내 위치를 각각 마커로 표시합니다.
- `Conscrypt`를 등록해 보안 프로바이더를 사용하도록 구성했습니다.
- `google-services.json` 파일이 포함되어 있어 Firebase 또는 Google 서비스 설정 흔적이 남아 있습니다.

## 확인한 한계 사항

- 설명과 달리 현재 구현은 네이버지도 기반이 아니라 Google Maps 기반입니다.
- 핵심 백엔드 URL이 `something` 으로 비워져 있어, 현재 상태 그대로는 추천 API 호출이 동작하지 않습니다.
- Google Geocoding API 키가 코드에 직접 포함된 흔적이 있어, 실제 배포 전에는 키 관리 방식 정리가 필요합니다.
- `AsyncTask`와 일부 구형 Android API 사용에 의존하고 있어 최신 Android 구조와는 거리가 있습니다.
- 테스트 코드는 기본 템플릿 수준만 존재합니다.
- 현재 작업 환경에서는 Java Runtime이 없어 `./gradlew :app:assembleDebug` 빌드 확인은 수행하지 못했습니다.

## 참고 파일

- `/app/src/main/java/edu/skku/testapp/bobfinder/MainActivity.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/menuselect.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/todaymenu.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/menuboard.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/openmap.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/Category.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/Price.java`
- `/app/src/main/java/edu/skku/testapp/bobfinder/People.java`
