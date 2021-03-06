package laboratorio.app.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import laboratorio.app.models.Product;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

public class API {
    public static final API instance = new API();
    private static final String API_URL = "http://ec2-3-227-239-131.compute-1.amazonaws.com/";
    private APIService service;

    private API() {
        service = buildRetrofit().create(APIService.class);
    }

    private Retrofit buildRetrofit() {
        ObjectMapper objectMapper = buildObjectMapper();

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    private ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT-12"));

        return objectMapper;
    }

    public APIService getService() {
        return service;
    }
}
