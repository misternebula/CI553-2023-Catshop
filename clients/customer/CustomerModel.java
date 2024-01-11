package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.stream.Collectors;

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
   * Notifies observers with search results based on the search string
   * @param search The search input
   */
  public void doCheck(String search )
  {
    searchString = search;

    try {
      var products = theStock.getProducts();
      var searchResults = new ArrayList<>();

      var searchWords = search.toLowerCase().split(" ");

      for (var product : products)
      {
        if (Arrays.stream(searchWords).anyMatch(product.getDescription().toLowerCase()::contains)
                || product.getProductNum().equalsIgnoreCase(search))
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

  public int paymentFinished()
  {
    if (theBasket.isEmpty())
    {
      // Should not happen if using CustomerView.
      // This check is here just in case, to stop bad orders going through the system.
      return 0;
    }

    try {
      for (var product : theBasket)
      {
        theStock.buyStock(product.getProductNum(), product.getQuantity());
      }

      theBasket.setOrderNum(theOrder.uniqueNumber());
      var finishedOrderNum = theBasket.getOrderNum();
      theOrder.newOrder(theBasket);
      makeBasket();
      return finishedOrderNum;
    } catch (OrderException | StockException e) {
      throw new RuntimeException(e);
    }
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
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected void makeBasket()
  {
    theBasket = new Basket();

    if (searchString != null)
    {
      // refresh view
      doCheck(searchString);
    }
  }
}

