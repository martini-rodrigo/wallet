package br.com.pernambucanas.banking.api.exception.handler;

import br.com.pernambucanas.banking.api.handler.FeignExceptionHandler;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeignExceptionHandlerTest {

	private FeignExceptionHandler feignExceptionHandler;

	@BeforeEach
	public void setUp() {
		feignExceptionHandler = new FeignExceptionHandler();
	}

	@Test
	public void testNotFoundFeignException() {
		var response = feignExceptionHandler.decode("getCustomer",
				Response.builder()
						.status(404)
						.reason("message error")
						.request(Request.create(
								Request.HttpMethod.GET,
								"v1/customer",
								new HashMap<>(),
								null,
								null,
								null))
						.build());

		assertTrue(response.getMessage().contains("[404 message error] during [GET] to [v1/customer] [getCustomer]"));
	}

	@Test
	public void testBadRequestFeignException() {
		var response = feignExceptionHandler.decode("getCustomer",
				Response.builder()
						.status(404)
						.reason("message error")
						.request(Request.create(
								Request.HttpMethod.GET,
								"v1/customer",
								new HashMap<>(),
								null,
								null,
								null))
						.build());

		assertTrue(response.getMessage().contains("[404 message error] during [GET] to [v1/customer] [getCustomer]"));
	}

	@Test
	public void testUnauthorizedFeignException() {
		var response = feignExceptionHandler.decode("getCustomer",
				Response.builder()
						.status(401)
						.reason("message error")
						.request(Request.create(
								Request.HttpMethod.GET,
								"v1/customer",
								new HashMap<>(),
								null,
								null,
								null))
						.build());

		assertTrue(response.getMessage().contains("[401 message error] during [GET] to [v1/customer] [getCustomer]"));
	}

	@Test
	public void testDefaultFeignException() {
		var response = feignExceptionHandler.decode("getCustomer",
				Response.builder()
						.status(500)
						.reason("message error")
						.request(Request.create(
								Request.HttpMethod.GET,
								"v1/customer",
								new HashMap<>(),
								null,
								null,
								null))
						.build());

		assertTrue(response.getMessage().contains("[500 message error] during [GET] to [v1/customer] [getCustomer]"));
	}

}
