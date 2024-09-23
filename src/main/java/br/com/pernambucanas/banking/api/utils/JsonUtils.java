package br.com.pernambucanas.banking.api.utils;

import br.com.pernambucanas.banking.api.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {

	private JsonUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> Object convertJsonInStringToObject(String jsonInString, Class<T> clazz) {
		try {
			return JsonUtils.createObjectMapper().readValue(jsonInString, clazz);
		} catch (Exception e) {
			throw new BusinessException("Error convert json to object.", e);
		}
	}

	public static String converObjectToJsonInString(Object clazz) {
		try {
			return JsonUtils.createObjectMapper().writeValueAsString(clazz);
		} catch (Exception e) {
			throw new BusinessException("Error convert object to json.", e);
		}
	}

	public static ObjectMapper createObjectMapper() {
		var mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper;
	}
}
