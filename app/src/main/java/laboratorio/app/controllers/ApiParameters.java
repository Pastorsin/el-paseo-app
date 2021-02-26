package laboratorio.app.controllers;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ApiParameters {

    private Gson gson = new Gson();
    private List<ApiProperty> list = new ArrayList<>();

    public ApiParameters(){}

    public void addProperty(String key, String value){
        list.add(new ApiProperty(key,value));
    }

    public String toJsonProperties(){
        return gson.toJson(list);
    }

}
