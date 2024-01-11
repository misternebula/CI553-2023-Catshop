package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The visual display seen by customers (Change to graphical version)
 * Change to a graphical display
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class DisplayView extends Canvas implements Observer
{
  private static final long serialVersionUID = 1L;
  private int H = 400;
  private int W = 600;
  private DisplayController cont = null;
  private final JTextArea waitingTextArea;
  private final JTextArea beingPickedTextArea;
  private final JTextArea readyTextArea;
  
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
  
  public DisplayView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    Container cp = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    var waitingLabel = new JLabel("Waiting");
    Font font = new Font("Monospaced", Font.BOLD, 24);
    waitingLabel.setFont(font);
    waitingLabel.setBounds(30, 10, 180, 30);
    waitingLabel.setHorizontalAlignment(JLabel.CENTER);
    cp.add(waitingLabel);

    waitingTextArea = new JTextArea();
    waitingTextArea.setBounds(30, 40, 180, 300);
    waitingTextArea.setFont(font);
    waitingTextArea.setEditable(false);
    waitingTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
    cp.add(waitingTextArea);

    var beingPickedLabel = new JLabel("Being Picked");
    beingPickedLabel.setFont(font);
    beingPickedLabel.setBounds(210, 10, 180, 30);
    beingPickedLabel.setHorizontalAlignment(JLabel.CENTER);
    cp.add(beingPickedLabel);

    beingPickedTextArea = new JTextArea();
    beingPickedTextArea.setBounds(210, 40, 180, 300);
    beingPickedTextArea.setFont(font);
    beingPickedTextArea.setEditable(false);
    beingPickedTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
    cp.add(beingPickedTextArea);

    var readyLabel = new JLabel("Ready");
    readyLabel.setFont(font);
    readyLabel.setBounds(390, 10, 180, 30);
    readyLabel.setHorizontalAlignment(JLabel.CENTER);
    cp.add(readyLabel);

    readyTextArea = new JTextArea();
    readyTextArea.setBounds(390, 40, 180, 300);
    readyTextArea.setFont(font);
    readyTextArea.setEditable(false);
    readyTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
    cp.add(readyTextArea);
    
    rootWindow.setVisible( true );                  // Make visible
  }
  
  
  public void setController( DisplayController c )
  {
    cont = c;
  }
  
  /**
   * Called to update the display in the shop
   */
  @Override
  public void update( Observable aModelOfDisplay, Object arg )
  {
    // Code to update the graphical display with the current
    //  state of the system
    //  Orders awaiting processing
    //  Orders being picked in the 'warehouse. 
    //  Orders awaiting collection
    
    try
    {
      Map<String, List<Integer> > res =
      ( (DisplayModel) aModelOfDisplay ).getOrderState();

      waitingTextArea.setText(listOfOrders(res, "Waiting"));
      beingPickedTextArea.setText(listOfOrders(res, "BeingPicked"));
      readyTextArea.setText(listOfOrders(res, "ToBeCollected"));
    }
    catch ( OrderException err ) {
      throw new RuntimeException(err);
    }

    repaint();
  }

  /**
   * Return a string of order numbers
   * @param map Contains the current state of the system
   * @param key The key of the list requested
   * @return As a string a list of order numbers.
   */
  private String listOfOrders( Map<String, List<Integer> > map, String key )
  {
    StringBuilder res = new StringBuilder();

    if ( map.containsKey( key ))
    {
      List<Integer> orders = map.get(key);

      for (var i = 0; i < orders.size(); i++)
      {
        if (i == 0)
        {
          res.append(orders.get(i));
        }
        else{
          res.append("\r\n").append(orders.get(i));
        }
      }
    } else {
      res = new StringBuilder("-No key-");
    }

    return res.toString();
  }
}
