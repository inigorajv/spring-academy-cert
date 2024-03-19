package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CashCardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;
	@Test
	void shouldReturnCashCardWhenDataIsSaved(){
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/cashcards/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

		DocumentContext doc = JsonPath.parse(response.getBody());
		Number id = doc.read("$.id");
		assertThat(id).isEqualTo(99);

	}

	@Test
	void shouldNotReturnCashCardWithUnknownId(){
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/cashcards/1000", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));

	}


	@Test
	void shouldReturnAllCashCardsWhenListIsRequested(){
		ResponseEntity<String> getResponse = restTemplate.getForEntity("/v1/cashcards", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext docContext = JsonPath.parse(getResponse.getBody());

		int cashCardCount = docContext.read("$.length()");
		assertThat(cashCardCount).isEqualTo(3);

		JSONArray ids = docContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

		JSONArray amounts = docContext.read("$..amount");
		assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.0, 150.00);

	}

	@Test
	void shouldReturnPageOfCashCards(){
		ResponseEntity<String> getResponse = restTemplate.getForEntity("/v1/cashcards?page=0&size=1", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		JSONArray page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(1);

	}

	@Test
	@DirtiesContext
	void shouldCreateNewCashCard(){
		CashCard cashCard = new CashCard(null, 1000.0);
		ResponseEntity<Void> response = restTemplate.postForEntity("/v1/cashcards", cashCard, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number amount = documentContext.read("$.amount");
		assertThat(amount).isEqualTo(1000.0);
	}

	@Test
	void shouldReturnSortedList(){
		ResponseEntity<String> getResponse = restTemplate.getForEntity("/v1/cashcards?page=0&size=1&sort=amount,desc", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read.size()).isEqualTo(1);

		double amount = documentContext.read("$[0].amount");
		assertThat(amount).isEqualTo(150.00);
	}

	@Test
	void shouldReturnListWithDefaultOrder(){
		ResponseEntity<String> getResponse = restTemplate.getForEntity("/v1/cashcards", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		JSONArray page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(3);

		JSONArray amounts = documentContext.read("$..amount");
		assertThat(amounts).containsExactly(1.00, 123.45, 150.00);


	}
}
