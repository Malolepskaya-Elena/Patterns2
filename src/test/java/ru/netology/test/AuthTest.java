package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should success login active registered user") // верный логин для активного зарег. пользователя
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[id=root]").should(text("Личный кабинет"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should error login not registered user") // неверный логин для незарег.пользователя
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should error login blocked registered user") // неверный логин для заблокированного пользователя
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Пользователь заблокирован"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should error login wrong") // неверный логин для активного зарегистрированного пользователя
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should error if login with wrong password") // неверный логин и пароль для активного пользователя
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $(".notification__content").should(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}