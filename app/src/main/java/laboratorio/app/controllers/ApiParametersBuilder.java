package laboratorio.app.controllers;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiParametersBuilder {

    private Gson gson = new Gson();

    private List<ApiProperty> list = null;
    private String range = null;
    private String fieldsToSort = null;

    public ApiParametersBuilder() {
    }

    public void addProperty(String key, String value) {
        if (list == null)
            list = new ArrayList<>();

        list.add(new ApiProperty(key, value));
    }

    public void addRange(int fromIndex, int toIndex) {
        this.range = String.format("%s,%s", fromIndex, toIndex);
    }

    public void addSort(List<String> fields) {
        this.fieldsToSort = fields.stream().collect(Collectors.joining(","));
    }

    public String getRange() {
        return range;
    }

    public String getProperties() {
        return gson.toJson(list);
    }

    public String getFieldsToSort() {
        return fieldsToSort;
    }
}
