package clients.customer;

import catalogue.Product;
import clients.Picture;
import middle.MiddleFactory;
import middle.IStockReader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CustomerView implements Observer
{
  private static final int H = 400;       // Height of window pixels
  private static final int W = 600;       // Width  of window pixels

  private final JTextField  searchBar   = new JTextField();
  private JScrollPane scrollPane;
  private JPanel scrollPanePanel;
  private final JButton     searchButton = new JButton("Search");

  private IStockReader theStock   = null;
  private CustomerController cont= null;

  private Container cp;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    searchBar.setBounds( 150, 10, 150, 25 );
    searchBar.setText("");
    cp.add( searchBar );

    searchButton.setBounds( 300, 10, 80, 24 );
    searchButton.addActionListener(e -> cont.doCheck( searchBar.getText() ) );
    cp.add( searchButton );


    scrollPanePanel = new JPanel();
    scrollPanePanel.setLayout(new GridLayout(0, 3));

    scrollPane = new JScrollPane(scrollPanePanel);
    scrollPane.setBounds( 150, 35, 230, 220 );
    cp.add( scrollPane );
    
    rootWindow.setVisible( true );                  // Make visible);
    searchBar.requestFocus();                        // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model = (CustomerModel) modelC;
    ArrayList<Product> searchResults = (ArrayList<Product>)arg;

    scrollPanePanel = new JPanel();
    scrollPanePanel.setLayout(new GridBagLayout());

    int row = 0;
    for (var product : searchResults) {
      var bagConstraints = new GridBagConstraints();
      bagConstraints.gridwidth = 1;
      bagConstraints.gridy = row;
      bagConstraints.ipadx = 5;
      bagConstraints.ipady = 2;

      var desc = new JLabel(product.getDescription());
      var price = new JLabel(String.valueOf(product.getPrice()));
      var stock = new JLabel(String.valueOf(product.getQuantity()));
      var addButton = new JButton("Add");

      scrollPanePanel.add(desc, bagConstraints);
      scrollPanePanel.add(price, bagConstraints);
      scrollPanePanel.add(stock, bagConstraints);
      scrollPanePanel.add(addButton, bagConstraints);
      row++;
    }

    scrollPane.setViewportView(scrollPanePanel);

    searchBar.requestFocus();
  }
}
