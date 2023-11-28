package clients.customer;

import clients.ControllerBase;

/**
 * The Customer Controller
 * @author M A Smith (c) June 2014
 */

public class CustomerController extends ControllerBase<CustomerModel, CustomerView>
{
  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CustomerController( CustomerModel model, CustomerView view )
  {
    super(model, view);
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck( String pn )
  {
    model.doCheck(pn);
  }
}

