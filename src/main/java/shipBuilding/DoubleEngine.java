package shipBuilding;

import java.util.List;

public class DoubleEngine extends Engine implements Activatable{
    private boolean active;

    public DoubleEngine(List<Connector> connectors) {
        super(connectors);
        this.active = false;
    }

    @Override
    public int getEnginePower() {
        if (this.active) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void activate(ActivatableVisitor visitor) {
        this.active = true;
        visitor.activate(this);
    }

    @Override
    public void deactivate(ActivatableVisitor visitor) {
        visitor.deactivate(this);
        this.active = false;
    }
}
