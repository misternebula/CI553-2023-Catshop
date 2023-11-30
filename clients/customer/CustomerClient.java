package clients.customer;

import clients.ClientBase;
import clients.backDoor.BackDoorClient;
import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Customer Client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class CustomerClient extends ClientBase<CustomerModel, CustomerView, CustomerController>
{
  public static void main (String args[])
  {
    String stockURL = args.length < 1         // URL of stock R
                    ? Names.STOCK_R           //  default  location
                    : args[0];                //  supplied location

    var instance = new CustomerClient();
    var mrf = instance.createFactory(stockURL, null);
    instance.displayGUI(mrf, "Customer Client (MVC RMI)");
  }

  @Override
  protected CustomerModel getModel(MiddleFactory mf) {
    return new CustomerModel(mf);
  }

  @Override
  protected CustomerView getView(JFrame window, MiddleFactory mf) {
    return new CustomerView( window, mf, 0, 0 );
  }

  @Override
  protected CustomerController getCont(CustomerModel model, CustomerView view) {
    return new CustomerController(model, view);
  }
}
