package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp(){
        cashCards = Arrays.array(
                new CashCard(99L, 123.45, "stan"),
                new CashCard(100L, 1.00, "stan"),
                new CashCard(101L, 150.00, "stan"));
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45, "stan" );
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");

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
                  "id": 99,
                  "owner": "stan"
                }
                """;
        CashCard cashcard = json.parse(expectedCashCard).getObject();

        assertThat(cashcard).isEqualTo(new CashCard(99L, 123.45, "stan"));
        assertThat(cashcard.id()).isEqualTo(99);
        assertThat(cashcard.amount()).isEqualTo(123.45);

    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");

    }


    @Test
    void cashCardListDeSerializationTest() throws IOException {
        String expectedJsonList = """
                [
                  {"id": 99, "amount": 123.45, "owner": "stan" },
                  {"id": 100, "amount": 1.00, "owner": "stan" },
                  {"id": 101, "amount": 150.00, "owner": "stan" }
                ]
                """;
        assertThat(jsonList.parse(expectedJsonList)).isEqualTo(cashCards);
    }
}

