package ru.netology.page;


import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");
    private final SelenideElement transferHeader = $("h1");

    public TransferPage() {

        transferHeader.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Пополнение карты"));
    }

    public DashboardPage makeValidTransfer(String amountToTransfer, DataHelper.CardDetails cardDetails) {
        makeTransfer(amountToTransfer, cardDetails);
        return new DashboardPage();
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardDetails cardDetails) {
        amountField.setValue(amountToTransfer);
        fromField.setValue(cardDetails.getCardNumber());
        transferButton.click();
    }

    public void getErrorMessage(String expectedText) {
        errorMessage.shouldHave(text(expectedText), Duration.ofSeconds(15)).shouldBe(visible);

    }
}
