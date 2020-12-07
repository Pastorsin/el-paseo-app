package laboratorio.app.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Connection {
    public static final Connection conn = new Connection();
    private static final String API_URL = "http://ec2-3-235-40-183.compute-1.amazonaws.com/";
    private Retrofit retrofit;

    private Connection() {
        // Mapper JSON -> Java
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Service provider
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
