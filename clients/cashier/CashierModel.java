package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import clients.ModelBase;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CashierModel extends ModelBase
{
	private enum State { process, checked }

	private State       theState   = State.process;   // Current state
	private Product     theProduct = null;            // Current product
	private Basket      theBasket  = null;            // Bought items

	private IOrderProcessing theOrder     = null;

	/**
	 * Construct the model of the Cashier
	 * @param mf The factory to create the connection objects
	 */

	public CashierModel(MiddleFactory mf)
	{
		super(mf);

		try                                           //
		{
			theOrder = mf.makeOrderProcessing();        // Process order
		} catch ( Exception e )
		{
			DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
		}

		theState   = State.process;                  // Current state
	}

	/**
	 * Get the Basket of products
	 * @return basket
	 */
	public Basket getBasket()
	{
		return theBasket;
	}

	/**
	 * Check if the product is in Stock
	 * @param productNum The product number
	 */
	public void doCheck(String productNum )
	{
		String theAction = "";
		theState  = State.process;                  // State process

		var stockAmount = getStockAmount(productNum);
		var pr = getProduct(productNum);

		theAction = getAction(productNum);

		if (stockAmount > 0)
		{
			pr.setQuantity(1);
			theProduct = pr;                      //   Remember prod.
			theProduct.setQuantity(1);     //    & quantity
			theState = State.checked;             //   OK await BUY
		}

		setChanged();
		notifyObservers(theAction);
	}

	/**
	 * Buy the product
	 */
	public void doBuy()
	{
		String theAction = "";
		int    amount  = 1;                         //  & quantity
		try
		{
			if ( theState != State.checked )          // Not checked
			{                                         //  with customer
				theAction = "Check if OK with customer first";
			} else {
				boolean stockBought =                   // Buy
						theStock.buyStock(                    //  however
								theProduct.getProductNum(),         //  may fail
								theProduct.getQuantity() );         //
				if ( stockBought )                      // Stock bought
				{                                       // T
					makeBasketIfReq();                    //  new Basket ?
					theBasket.add( theProduct );          //  Add to bought
					theAction = "Purchased " +            //    details
							theProduct.getDescription();  //
				} else {                                // F
					theAction = "!!! Not in stock";       //  Now no stock
				}
			}
		} catch( StockException e )
		{
			DEBUG.error( "%s\n%s",
					"CashierModel.doBuy", e.getMessage() );
			theAction = e.getMessage();
		}
		theState = State.process;                   // All Done
		setChanged(); notifyObservers(theAction);
	}

	/**
	 * Customer pays for the contents of the basket
	 */
	public void doBought()
	{
		String theAction = "";
		int    amount  = 1;                       //  & quantity
		try
		{
			if ( theBasket != null &&
					theBasket.size() >= 1 )            // items > 1
			{                                       // T
				theOrder.newOrder( theBasket );       //  Process order
				theBasket = null;                     //  reset
			}                                       //
			theAction = "Next customer";            // New Customer
			theState = State.process;               // All Done
			theBasket = null;
		} catch( OrderException e )
		{
			DEBUG.error( "%s\n%s",
					"CashierModel.doCancel", e.getMessage() );
			theAction = e.getMessage();
		}
		theBasket = null;
		setChanged(); notifyObservers(theAction); // Notify
	}

	/**
	 * ask for update of view callled at start of day
	 * or after system reset
	 */
	public void askForUpdate()
	{
		setChanged(); notifyObservers("Welcome");
	}

	/**
	 * make a Basket when required
	 */
	private void makeBasketIfReq()
	{
		if ( theBasket == null )
		{
			try
			{
				int uon   = theOrder.uniqueNumber();     // Unique order num.
				theBasket = makeBasket();                //  basket list
				theBasket.setOrderNum( uon );            // Add an order number
			} catch ( OrderException e )
			{
				DEBUG.error( "Comms failure\n" +
						"CashierModel.makeBasket()\n%s", e.getMessage() );
			}
		}
	}

	/**
	 * return an instance of a new Basket
	 * @return an instance of a new Basket
	 */
	protected Basket makeBasket()
	{
		return new Basket();
	}
}
  
