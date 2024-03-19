package com.example.cashcard;

import com.example.cashcard.entity.CashCard;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

}
