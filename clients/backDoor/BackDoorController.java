package clients.backDoor;


import clients.ControllerBase;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;

/**
 * The BackDoor Controller
 * @author M A Smith (c) June 2014
 */

public class BackDoorController extends ControllerBase<BackDoorModel, BackDoorView>
{
  public BackDoorController(BackDoorModel model, BackDoorView view )
  {
    super(model, view);
  }

  /**
   * Query interaction from view
   * @param pn The product number to be checked
   */
  public void doQuery( String pn )
  {
    model.doQuery(pn);
  }
  
  /**
   * RStock interaction from view
   * @param pn       The product number to be re-stocked
   * @param quantity The quantity to be re-stocked
   */
  public void doRStock( String pn, String quantity )
  {
    model.doRStock(pn, quantity);
  }

  /**
   * Clear interaction from view
   */
  public void doClear()
  {
    model.doClear();
  }
}

