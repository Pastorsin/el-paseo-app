package laboratorio.app.models;

import java.util.List;

public class Pagination<T> {
    private String totalElements;
    private List<T> page;

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getPage() {
        return page;
    }

    public void setPage(List<T> page) {
        this.page = page;
    }
}
