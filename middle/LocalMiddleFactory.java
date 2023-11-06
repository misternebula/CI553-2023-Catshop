/**
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */

package middle;

import dbAccess.StockReader;
import dbAccess.StockReaderWriter;
import orders.Order;


/**
  * Provide access to middle tier components.
  * Now only one instance of each middle tier object is created
  */

// Pattern: Abstract Factory

public class LocalMiddleFactory implements MiddleFactory
{
  private static StockReader aStockR  = null;
  private static StockReaderWriter aStockRW = null;
  private static Order   aOrder   = null;
  
  /**
   * Return an object to access the database for read only access.
   * All users share this same object.
   */
  
  public IStockReader makeStockReader() throws StockException
  {
    if ( aStockR == null )
      aStockR = new StockReader();
    return aStockR;
  }

  /**
   * Return an object to access the database for read/write access.
   * All users share this same object.
   */
  
  public IStockReaderWriter makeStockReadWriter() throws StockException
  {
    if ( aStockRW == null )
      aStockRW = new StockReaderWriter();
    return aStockRW;
  }
  
  /**
   * Return an object to access the order processing system.
   * All users share this same object.
   */
   
  public IOrderProcessing makeOrderProcessing() throws OrderException
  {
    if ( aOrder == null )
      aOrder = new Order();
    return aOrder;
  }
}

