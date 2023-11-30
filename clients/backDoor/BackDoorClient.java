package clients.backDoor;

import clients.ClientBase;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */


public class BackDoorClient extends ClientBase<BackDoorModel, BackDoorView, BackDoorController>
{
   public static void main (String args[])
   {
       String stockURL = args.length < 1     // URL of stock RW
               ? Names.STOCK_RW      //  default  location
               : args[0];            //  supplied location
       String orderURL = args.length < 2     // URL of order
               ? Names.ORDER         //  default  location
               : args[1];            //  supplied location

       var instance = new BackDoorClient();
       var mrf = instance.createFactory(stockURL, orderURL);
       instance.displayGUI(mrf, "");
  }

    @Override
    protected BackDoorModel getModel(MiddleFactory mf) {
        return new BackDoorModel(mf);
    }

    @Override
    protected BackDoorView getView(JFrame window, MiddleFactory mf) {
        return new BackDoorView( window, mf, 0, 0 );
    }

    @Override
    protected BackDoorController getCont(BackDoorModel model, BackDoorView view) {
        return new BackDoorController(model, view);
    }
}
