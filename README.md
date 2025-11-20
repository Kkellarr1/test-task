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

## Лицензия

MIT

