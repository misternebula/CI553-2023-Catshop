package clients.customer;

/**
 * The Customer Controller
 * @author M A Smith (c) June 2014
 */

public class CustomerController
{
  private CustomerModel model = null;
  private CustomerView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CustomerController( CustomerModel model, CustomerView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param search The text in the search bar
   */
  public void doCheck( String search )
  {
    model.doCheck(search);
  }
}

