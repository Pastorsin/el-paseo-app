package laboratorio.app.models;

import java.util.List;

public class General {
    private Integer id;
    private List<AvailableNode> activeNodes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<AvailableNode> getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(List<AvailableNode> activeNodes) {
        this.activeNodes = activeNodes;
    }
}
