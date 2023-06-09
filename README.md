# Что это?
Это Телеграм бот, который уведомляет вас о новых изменениях на сайте. На текущий момент бот способен отслеживать репозитории в GitHub и вопросы на StackOverFlow, но вы не ограничены и можете самостоятельно добавить новые сайты. (это не трудно, ниже будет инструкция как это сделать)

## Что под капотом?
* Spring Boot 3
* [SDK Telegram Bot API](https://github.com/pengrad/java-telegram-bot-api "https://github.com/pengrad/java-telegram-bot-api")
* Hibernate, JdbcTemplate, JOOQ (для каждого написаны свои repositories и servises)
* PostgreSQL 15
* Liquibase
* RabbitMQ 3
* JUnit
* Mockito
* Prometheus
* Grafana

## Какой функционал?
Вот небольшое видео, как работает бот:

https://github.com/Neonik-dev/Update-Tracker-Bot/assets/64394145/7bfcd748-6c67-47e1-b5d1-ccd1caf00e2b


На текущий момент в репозиториях GitHub отслеживаются следующие изменения:
* Время последнего изменения
* Commits
* Branches
* Issue
* Комментарии в issue
* Pull requests

В StackOverFlow пока отслеживается только время последнего изменения.

## Хочу больше сайтов и больше подробностей об обновлении, что нужно сделать?
Я постарался сделать так, чтобы добавление новых доменов для отслеживания и новый сценариев у уже существующих ссылок, было безболезненно.

Для того, чтобы добавить **новый домен** для отслеживания выполняем следующее:
1. Создаем новый парсер для ссылки в модуле link-parser. (Парсер нужен чтобы забрать из ссылки нужные данные, например имя репозитория, которые в дальнейшем будут передаваться в API сайта)
2. Этот парсер нужно добавить в цепочку парсеров. Для этого нужно зайти в класс ParseChain и просто в конструктор добавить ваш парсер. (там используется паттер Chain of Responsibility)
3. Далее необходимо написать `NewDomainClient` в модуле scrapper в директории clients/site. Новый класс отвечает за сбор данных с нового сайта. Именно здесь вы пишите взаимодействие с API сайта. Ваш класс должен быть расширять BaseSiteClient, у которого всего 2 метода: `Map<String, String> getUpdates` и `String getUpdatedDate`.
4. После написания `NewDomainClient` идем в класс `SitesMap` и добавляем в метод initMap наш новый класс. (тут используется паттерн Command)
5. Добавляем новый домен в базу данных. Делается это так: `INSERT INTO domains(name) VALUES ('newDomainName');`

Ну и все, таким образом, чтобы добавить новый домен для отслеживания, нужно написать саму логику получения обновлений и парсинг ссылки. И все. Нигде не нужно изменять основной код.

Если же вы хотите добавить **новый отслеживающий сценарий** для уже существующего домена, то все еще проще:
1. Пишем логику получения новых данных с сайта
2. Вызываем этот новый метод в методе `getUpdates` и добавляем результат в Map. Например вот так: `dataUpdates.put("newTrackerField", getDataNewField());`

Теперь бот будет отслеживать новый сценарий.

## Что инетерсного я сделал в этом проекте?
1. Простое добавление новых доменов и новый сценариев
2. В RabbetMQ настроил Dead Letter Queue, в которую попадают запросы "бракованные". Но попадают они туда только после трёх неудачных попыток. Количество попыток можно изменить в `scrapper.properties` в поле `max-attempts`.
3. Сбор и отслеживание статистики. (благодаря этому мне удалось выявить баг в коде)
4. Тестирование. Покрытие пока не большое, но в свободное время буду дописывать (todo)
5. Везде соблюдал принципы SOLID
