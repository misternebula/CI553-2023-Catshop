package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import middle.MiddleFactory;
import middle.RemoteMiddleFactory;

import javax.swing.*;

public class ClientBase<T extends ModelBase, U extends ViewBase, V extends ControllerBase<T, U>> {
    protected T getModel(MiddleFactory mf) { return null; }
    protected U getView(JFrame window, MiddleFactory mf) { return null; }
    protected V getCont(T model, U view) { return null; }

    protected MiddleFactory createFactory(String stockURL, String orderURL)
    {
        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo( stockURL );
        mrf.setOrderInfo  ( orderURL );
        return mrf;
    }

    protected void displayGUI(MiddleFactory mf, String title)
    {
        JFrame window = new JFrame();

        window.setTitle(title);
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        //BackDoorModel model = new BackDoorModel(mf);
       // BackDoorView view  = new BackDoorView( window, mf, 0, 0 );
        //BackDoorController cont  = new BackDoorController( model, view );
        var model = getModel(mf);
        var view = getView(window, mf);
        var cont = getCont(model, view);
        view.setController( cont );

        model.addObserver( view );       // Add observer to the model
        window.setVisible(true);         // Display Screen
    }
}
