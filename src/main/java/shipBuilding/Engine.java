package shipBuilding;

import java.util.List;

public class Engine extends Component {
    public Engine(List<Connector> connectors) {
        super(connectors);
    }

    public boolean isValid() {
        return getOrientation() == 0;
    }

    public int getEnginePower() {
        return 1;
    }
}
