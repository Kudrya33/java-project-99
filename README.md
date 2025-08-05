### Hexlet tests and linter status:
[![Actions Status](https://github.com/Kudrya33/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/Kudrya33/java-project-99/actions)
[![Java CI](https://github.com/Kudrya33/java-project-99/actions/workflows/build.yml/badge.svg)](https://github.com/Kudrya33/java-project-99/actions/workflows/build.yml)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Kudrya33_java-project-99&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Kudrya33_java-project-99)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Kudrya33_java-project-99&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Kudrya33_java-project-99)

🌐 [Открыть приложение](https://java-project-99-30le.onrender.com)

# Task Manager API

Проект представляет собой RESTful API для управления задачами (Task Manager) с использованием Spring Boot. Включает функционал для работы с пользователями, статусами задач, метками и самими задачами. Реализована аутентификация через JWT, валидация данных, фильтрация задач и обработка ошибок.

## Основные возможности

- Управление пользователями (регистрация, аутентификация, CRUD)
- Управление статусами задач (CRUD)
- Управление метками (CRUD)
- Управление задачами с возможностью фильтрации (CRUD)
- Автоматическая инициализация тестовых данных при запуске
- Логирование времени создания сущностей
- Обработка ошибок (404, 422)

## Запуск проекта

1. **Предварительные требования**:
    - Установите JDK 17+
    - Установите Gradle 7.6+

2. **Клонирование репозитория**:
3. **Сборка проекта**:
   - gradle build
4. **Запуск приложения**:
    - gradle bootRun
5. **Приложение будет доступно по адресу**:
    - http://localhost:8080

Начальные данные

При запуске автоматически создаются:

    Администратор: email hexlet@example.com, пароль qwerty

    Статусы задач: Draft, ToReview, ToBeFixed, ToPublish, Published

    Метки: feature, bug

API Endpoints

    POST /api/login - аутентификация (получение JWT токена)

    GET /api/users - список пользователей

    POST /api/users - создание пользователя

    GET /api/task_statuses - список статусов задач

    GET /api/labels - список меток

    GET /api/tasks - список задач (с фильтрацией)