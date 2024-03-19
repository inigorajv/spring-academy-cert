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

}
