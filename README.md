# AnimeOn TV

Нативный клиент сервиса **AnimeOn** для телевизоров и ТВ-приставок на базе **Android TV** и **Google TV**.

Разработан на **Jetpack Compose for TV (Kotlin)** и **Media3 ExoPlayer** с нативной поддержкой пульта (D-Pad).

---

## Возможности

- **100% нативная поддержка ТВ:** Управление стрелками пульта (вверх/вниз/влево/вправо/ОК), фокус с увеличением карточек и золотой рамкой.
- **Главный экран:**
  - Hero-баннер с актуальными онгоингами.
  - Карусель «Расписание новых серий» (обновляется в реальном времени с Shikimori-рейтингами).
  - Сетка «Популярные аниме».
- **Экран тайтла:**
  - Описание, жанры, год, статус.
  - Интерактивный переключатель студий озвучки (AniDUB, AniStar, OnWave, субтитры).
  - Горизонтальный список серий с кадрами-скриншотами.
- **Плеер для ТВ (Media3 ExoPlayer):**
  - Аппаратное декодирование потоков HLS / MP4.
  - Перемотка на 10 секунд стрелками влево/вправо.
  - Пауза / плей по центральной кнопке ОК.
- **Поиск:**
  - Удобный поиск по каталогу с адаптивной сеткой постеров.

---

## Архитектура проекта

```
animeon-tv/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml (Leanback Launcher, landscape, TV banners)
│   │   ├── java/fun/animeon/tv/
│   │   │   ├── data/
│   │   │   │   ├── api/ (AnimeOnApi, ApiClient, StreamResolver)
│   │   │   │   ├── model/ (AnimeModels.kt)
│   │   │   │   └── repository/ (AnimeRepository.kt)
│   │   │   ├── ui/
│   │   │   │   ├── components/ (AnimeCard, HeroBanner, SwimlaneRow)
│   │   │   │   ├── screens/
│   │   │   │   │   ├── home/ (HomeScreen, HomeViewModel)
│   │   │   │   │   ├── detail/ (DetailScreen, DetailViewModel)
│   │   │   │   │   ├── player/ (PlayerScreen, PlayerViewModel)
│   │   │   │   │   └── search/ (SearchScreen, SearchViewModel)
│   │   │   │   └── theme/ (Theme, Color)
│   │   │   └── MainActivity.kt (Navigation Compose)
│   │   └── res/ (баннеры, иконки, цвета)
│   └── build.gradle.kts
└── .github/workflows/build.yml (Автосборка APK в GitHub Actions)
```

---

## Как собрать APK без ручной настройки среды

Вам **не нужно** устанавливать Android Studio или настраивать переменные окружения:

1. Загрузите папку проекта `animeon-tv` в ваш репозиторий на **GitHub** (приватный или публичный).
2. Перейдите во вкладку **Actions**.
3. Запустится скрипт `Build Android TV APK`.
4. Через 2 минуты в разделе **Artifacts** появится готовый файл `animeon-tv-apk.zip` с готовым `.apk` файлом.
5. Скопируйте APK на флешку или передайте через приложение *Send Files to TV* и установите на телевизор.
