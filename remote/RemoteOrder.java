package remote;

import catalogue.Basket;
import middle.OrderException;
import orders.Order;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * The order processing handling.
 * This code is incomplete
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class RemoteOrder
       extends    UnicastRemoteObject
       implements IRemoteOrder
{
  private static final long serialVersionUID = 1;
  private Order aOrder = null;

  public RemoteOrder(String url )
         throws RemoteException, OrderException
  {
    aOrder = new Order();
  }

  public void newOrder( Basket bought )
         throws RemoteException, OrderException
  {
     aOrder.newOrder( bought );
    
  }

  public int uniqueNumber()
         throws RemoteException, OrderException
  {
    return aOrder.uniqueNumber();
  }
  
  public Basket getOrderToPick()
         throws RemoteException, OrderException
  {
      return aOrder.getOrderToPick();
  }
  
  public boolean informOrderPicked( int orderNum )
         throws RemoteException, OrderException
  {
     return aOrder.informOrderPicked(orderNum);
  }
  
  public boolean informOrderCollected( int orderNum )
         throws RemoteException, OrderException
  {
      return aOrder.informOrderCollected(orderNum);
  }
  
  
  public Map<String, List<Integer>> getOrderState()
          throws RemoteException, OrderException
  {
    return aOrder.getOrderState();
  }

}
