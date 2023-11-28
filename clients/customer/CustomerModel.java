package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import clients.ModelBase;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.IOrderProcessing;
import middle.StockException;
import middle.IStockReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends ModelBase
{

	private ImageIcon thePic = null;

	/*
	 * Construct the model of the Customer
	 * @param mf The factory to create the connection objects
	 */
	public CustomerModel(MiddleFactory mf)
	{
		super(mf);
	}

	/**
	 * Check if the product is in Stock
	 * @param productNum The product number
	 */
	public void doCheck(String productNum )
	{
		var stockAmount = getStockAmount(productNum);
		var pr = getProduct(productNum);

		String theAction = getAction(productNum);

		try {
			if (stockAmount >= 0)
			{
				pr.setQuantity(1);
				thePic = theStock.getImage(productNum);
				displayText = pr.getDescription() + "\nPrice : £" + pr.getPrice() + "\n";

				if (stockAmount == 0)
					displayText += "This item is out of stock.";
				else
					displayText += stockAmount + " in stock.";
			}
			else{
				displayText = "Invalid product ID.";
			}
		} catch(StockException e)
		{
			DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage() );
			theAction = e.getMessage();
		}

		setChanged();
		notifyObservers(theAction);
	}

	/**
	 * Return a picture of the product
	 * @return An instance of an ImageIcon
	 */
	public ImageIcon getPicture()
	{
		return thePic;
	}

	/**
	 * ask for update of view callled at start
	 */
	private void askForUpdate()
	{
		setChanged(); notifyObservers("START only"); // Notify
	}
}

