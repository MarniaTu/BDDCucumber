package ru.netology.steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.generateValidTransferAmount;
import static ru.netology.data.DataHelper.getMaskedCardNumber;


public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static TransferPage transferPage;
    private int firstCardBalance;
    private int secondCardBalance;
    private int amount;
    private int expectedFirstCardBalance;

    private int expectedSecondCardBalance;
    private int actualFirstCardBalance;
    private int actualSecondCardBalance;

//    @BeforeEach
//    void setUp() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--start-maximized");
//        Map<String, Object> prefs = new HashMap<String, Object>();
//
//        prefs.put("credentials_enable_service", false);
//        prefs.put("password_manager_enabled", false);
//
//        options.setExperimentalOption("prefs", prefs);
//        Configuration.browserCapabilities = options;
//    }

    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();

        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;

        loginPage = Selenide.open(url, LoginPage.class);
    }

    @И("пользователь авторизуется с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(login, password);
    }

    @И("пользователь вводит на странице верификации проверочный код {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит допустимую сумму со второй карты на свою первую карту с главной страницы")
    public void transferValidAmountFromSecondToFirstCard() {
        var firstCardDetails = DataHelper.getFirstCardDetails();
        var secondCardDetails = DataHelper.getSecondCardDetails();
        firstCardBalance = dashboardPage.getCardBalance(getMaskedCardNumber(firstCardDetails.getCardNumber()));
        secondCardBalance = dashboardPage.getCardBalance(getMaskedCardNumber(secondCardDetails.getCardNumber()));

        amount = generateValidTransferAmount(secondCardBalance);
        expectedFirstCardBalance = firstCardBalance + amount;
        expectedSecondCardBalance = secondCardBalance - amount;
        transferPage = dashboardPage.selectCardToTransfer(firstCardDetails);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardDetails);
    }

    @Тогда("баланс его первой карты из списка на главной странице должен увеличиться на сумму перевода")
    public void getActualFirstCardBalanceValue() {
        var firstCardDetails = DataHelper.getFirstCardDetails();
        var secondCardDetails = DataHelper.getSecondCardDetails();
        actualFirstCardBalance = dashboardPage.getCardBalance(getMaskedCardNumber(firstCardDetails.getCardNumber()));
        actualSecondCardBalance = dashboardPage.getCardBalance(getMaskedCardNumber(secondCardDetails.getCardNumber()));

        assertAll(() -> assertEquals(expectedFirstCardBalance, actualFirstCardBalance),
                () -> assertEquals(expectedSecondCardBalance, actualSecondCardBalance));
    }

}
