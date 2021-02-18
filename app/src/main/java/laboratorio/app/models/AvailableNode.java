package laboratorio.app.models;

import java.util.Date;
import java.util.Objects;

public class AvailableNode {
    private Integer id;
    private String dateTimeFrom;
    private String dateTimeTo;
    private Date day;
    private Node node;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableNode that = (AvailableNode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dateTimeFrom, that.dateTimeFrom) &&
                Objects.equals(dateTimeTo, that.dateTimeTo) &&
                Objects.equals(day, that.day) &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeFrom, dateTimeTo, day, node);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateTimeFrom() {
        return dateTimeFrom;
    }

    public void setDateTimeFrom(String dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public String getDateTimeTo() {
        return dateTimeTo;
    }

    public void setDateTimeTo(String dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "AvailableNode{" +
                "dateTimeFrom='" + dateTimeFrom + '\'' +
                ", dateTimeTo='" + dateTimeTo + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
