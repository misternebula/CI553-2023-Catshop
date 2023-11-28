package clients;

public class ControllerBase<T extends ModelBase, V extends ViewBase> {
    protected T model = null;
    protected V view  = null;

    public ControllerBase( ModelBase model, ViewBase view )
    {
        this.view  = (V)view;
        this.model = (T)model;
    }
}
