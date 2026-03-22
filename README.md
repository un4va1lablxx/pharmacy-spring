# 💊 Аптечный магазин

Кросс-платформенное веб-приложение для автоматизации работы аптеки.  
Бэкенд на Java + Spring Boot, фронтенд — HTML/CSS/JS (Thymeleaf).  
Проект позволяет управлять ассортиментом товаров, обрабатывать заказы, вести учёт пользователей и предоставляет административную панель.

---

## ⚠️ Дисклеймер

Проект является учебным (pet-проектом). Все названия товаров, аптек и прочие данные используются исключительно в демонстрационных целях и не связаны с реальными коммерческими организациями. Проект не преследует коммерческих целей и не нарушает авторские права.

---

## 🚀 Функциональность

### Пользовательская часть
- Просмотр каталога лекарственных средств с фильтрацией по категориям, наличию и рецептурному отпуску.
- Сортировка товаров по цене.
- Поиск по названию.
- Добавление товаров в корзину, изменение количества, удаление.
- Оформление заказа с выбором способа оплаты и адреса доставки (интеграция с картой Яндекса).
- Регистрация и авторизация пользователей.

### Административная часть
- Управление пользователями (CRUD).
- Управление товарами (CRUD).
- Управление заказами (просмотр, изменение статуса доставки, способа оплаты, адреса).

---

## 🛠 Технологии

- **Java 17**
- **Spring Boot** (Web, Data JPA, Security, Thymeleaf)
- **PostgreSQL**
- **Gradle**
- **Lombok**
- **Spring Security** (аутентификация, ролевая модель)
- **Thymeleaf** (шаблонизация)
- **Map API (Яндекс.Карты)** для выбора адреса доставки

---

## 📋 Требования

- Java 17+
- PostgreSQL 13+
- Gradle

---

## 🔧 Установка и запуск

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/un4va1lablxx/pharmacy-spring.git
   cd pharmacy-spring
   ```

2. **Создать базу данных PostgreSQL**
   ```sql
   CREATE DATABASE pharmacy;
   ```

3. **Настроить подключение к БД**  
   Отредактировать `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/pharmacy_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Инициализировать базу данных**  
   Выполнить скрипт `src/main/resources/init.sql` в pgAdmin.

5. **Собрать и запустить приложение**
   ```bash
   gradlew.bat bootRun
   ```

6. **Открыть в браузере**
   ```
   http://localhost:8080
   ```

> **Примечание:** для входа в административную панель используйте учётные данные:  
> логин: `admin@selderey.ru`, пароль: `admin`.

---

## 🌄 Внешний вид


| Главная страница | Каталог товаров |
|------------------|-----------------|
| <img width="772" height="704" alt="image" src="https://github.com/user-attachments/assets/6c1356be-758b-41ac-92f2-9d03cbacdcf0" /> | <img width="881" height="485" alt="image" src="https://github.com/user-attachments/assets/12d8da83-9682-45f0-b593-cf001a7f80ac" /> |

| Корзина | Оформление заказа |
|---------|-------------------|
| <img width="974" height="642" alt="image" src="https://github.com/user-attachments/assets/7f9d89dc-4990-4bd3-95d9-d355f0940ba6" /> | <img width="762" height="829" alt="image" src="https://github.com/user-attachments/assets/661bb49b-3d82-4239-8c7b-62cd57022f75" /> |

| Админ-панель (пользователи) | Админ-панель (товары) |
|-----------------------------|-----------------------|
| <img width="1179" height="768" alt="image" src="https://github.com/user-attachments/assets/9f95cb73-84ee-4f91-976d-6582a2de526d" /> | <img width="974" height="581" alt="image" src="https://github.com/user-attachments/assets/f59fca73-4379-4658-a38c-d9e08670847b" /> |

| Управление заказами | Редактирование заказа |
|---------------------|------------------------|
| <img width="974" height="409" alt="image" src="https://github.com/user-attachments/assets/29795852-b9ff-4c5e-86ac-daa679a8f309" /> | <img width="498" height="477" alt="image" src="https://github.com/user-attachments/assets/a2fc8644-c9b9-4c1a-88b3-b7225a0e7a2e" /> |

---
