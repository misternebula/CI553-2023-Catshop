package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends Observable
{
  private Basket      theBasket  = null;          // Bought items

  private IStockReaderWriter theStock     = null;
  private IOrderProcessing theOrder     = null;

  private String searchString;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReadWriter();           // Database access
      theOrder = mf.makeOrderProcessing();
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    makeBasket();                    // Initial Basket
  }

  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param search The search input
   */
  public void doCheck(String search )
  {
    searchString = search;

    try {
      var products = theStock.getProducts();

      var searchResults = new ArrayList<>();

      for (var product : products)
      {
        if (product.getDescription().toLowerCase().contains(search.toLowerCase()))
        {
          searchResults.add(product);
        }
      }

      setChanged();
      notifyObservers(searchResults);
    } catch (StockException e) {
      throw new RuntimeException(e);
    }
  }

  public void addToBasket(Product product)
  {
    var pr = new Product(product);
    pr.setQuantity(1);
    theBasket.add(pr);
    doCheck(searchString);
  }

  public void incrementProduct(Product product)
  {
    product.setQuantity(product.getQuantity() + 1);
    doCheck(searchString);
  }

  public void decrementProduct(Product product)
  {
    product.setQuantity(product.getQuantity() - 1);

    if (product.getQuantity() == 0)
    {
      theBasket.remove(product);
    }

    doCheck(searchString);
  }

  /**
   * Returns the image of a product.
   * @param pn The product number.
   * @return ImageIcon
   */
  public ImageIcon getPicture(String pn)
  {
    try {
      return theStock.getImage(pn);
    } catch (StockException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected void makeBasket()
  {
    try {
      int uon = theOrder.uniqueNumber();
      theBasket = new Basket();
      theBasket.setOrderNum( uon );
    } catch (OrderException e) {
      throw new RuntimeException(e);
    }

    if (searchString != null)
    {
      // refresh view
      doCheck(searchString);
    }
  }
}

