package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45 );
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);

    }

    @Test
    void cashCardDeSerializationTest() throws IOException {
        String expectedCashCard = """
                {
                  "amount": 123.45,
                  "id": 99
                }
                """;
        CashCard cashcard = json.parse(expectedCashCard).getObject();

        assertThat(cashcard).isEqualTo(new CashCard(99L, 123.45));
        assertThat(cashcard.id()).isEqualTo(99);
        assertThat(cashcard.amount()).isEqualTo(123.45);

    }
}

