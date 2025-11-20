# Interval Training App

Приложение для интервальных тренировок с GPS-трекингом.

## Архитектура

Приложение построено по принципам **Clean Architecture** и **MVVM**:

- **Domain** - бизнес-логика, entities, use cases
- **Data** - источники данных, репозитории, API
- **Presentation** - UI, ViewModels, навигация

## Технологии

- Kotlin
- Jetpack Compose
- Material 3
- Retrofit + OkHttp
- Google Maps
- Navigation Compose
- Coroutines + Flow
- ViewModel

## Функциональность

1. **Экран загрузки** - ввод ID тренировки (по умолчанию 68)
2. **Экран тренировки**:
   - Вкладка "Таймер" - отображение интервалов, обратный отсчет
   - Вкладка "Карта" - GPS-трек в реальном времени
   - Звуковые сигналы (1 пик - начало, 2 пика - конец)
   - Работа в фоне через Foreground Service

## Настройка

### Google Maps API Key

Необходимо добавить ваш Google Maps API ключ в `AndroidManifest.xml`:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY" />
```

## Сборка APK

### Debug APK

```bash
./gradlew assembleDebug
```

APK будет в: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK

```bash
./gradlew assembleRelease
```

APK будет в: `app/build/outputs/apk/release/app-release.apk`

Для подписи release APK создайте `keystore.properties`:

```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=your_key_alias
storeFile=path/to/keystore.jks
```

## Git

### Инициализация репозитория

```bash
git init
git add .
git commit -m "Initial commit: Interval Training App with Clean Architecture"
```

### Добавление remote

```bash
git remote add origin <your-repository-url>
git push -u origin main
```

## Структура проекта

```
app/src/main/java/com/example/testforwork/
├── data/              # Data слой
│   ├── api/          # API интерфейсы
│   ├── datasource/   # Источники данных
│   ├── model/        # DTO модели
│   └── repository/   # Реализации репозиториев
├── domain/           # Domain слой
│   ├── entity/       # Бизнес-сущности
│   ├── repository/   # Интерфейсы репозиториев
│   └── usecase/      # Use cases
├── presentation/     # Presentation слой
│   ├── navigation/  # Навигация
│   ├── screen/       # UI экраны
│   └── viewmodel/    # ViewModels
├── di/               # Dependency Injection
└── service/          # Foreground Service
```

## API

- **Endpoint**: `GET https://sr111.05.testing.place/api/v2/interval-timers/{id}`
- **Authorization**: Bearer token (настроен в ApiClient)
- **Пример ID**: 68

## Разрешения

- `INTERNET` - для API запросов
- `ACCESS_FINE_LOCATION` - для GPS-трекинга
- `FOREGROUND_SERVICE` - для работы в фоне
- `POST_NOTIFICATIONS` - для уведомлений

## Скринкаст

Для создания скринкаста рекомендуется показать:

1. Экран загрузки с предзаполненным ID (68)
2. Нажатие "Загрузить" и процесс загрузки
3. Экран тренировки:
   - Вкладка "Таймер" - интервалы, обратный отсчет
   - Запуск тренировки кнопкой "Старт"
   - Переключение на вкладку "Карта" - отображение GPS-трека
   - Звуковые сигналы при переходах между интервалами
   - Остановка тренировки
4. Демонстрация работы в фоне (сворачивание приложения)

## Лицензия

MIT

