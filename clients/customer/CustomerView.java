package clients.customer;

import catalogue.Product;
import clients.Picture;
import middle.MiddleFactory;
import middle.IStockReader;

import javax.swing.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

  private final JTextField searchBar = new JTextField();
  private final JScrollPane searchScrollPane;
  private final JScrollPane basketScrollPane;
  private final JLabel totalLabel;

  private CustomerController cont = null;

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
      IStockReader theStock = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    Container cp = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    searchBar.setBounds( 250, 10, 250, 25 );
    searchBar.setText("");
    cp.add( searchBar );

    JButton searchButton = new JButton("Search");
    searchButton.setBounds( 490, 10, 79, 24 );
    searchButton.addActionListener(e -> cont.doCheck( searchBar.getText() ) );
    cp.add(searchButton);

    searchScrollPane = new JScrollPane();
    searchScrollPane.setBounds( 250, 35, 320, 310 );
    cp.add(searchScrollPane);

    basketScrollPane = new JScrollPane();
    basketScrollPane.setBounds(10, 10, 230, 270);
    cp.add(basketScrollPane);

    totalLabel = new JLabel("0 Items - Total £0.00");
    totalLabel.setBounds(10, 280, 230, 20);
    totalLabel.setHorizontalAlignment(JLabel.CENTER);
    cp.add(totalLabel);

    JButton finishButton = new JButton("Finish");
    finishButton.setBounds(10, 310, 229, 34);
    cp.add(finishButton);
    
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

    // draw search results
    var layout = new GridBagLayout();
    JPanel scrollPanePanel = new JPanel(layout);
    int row = 0;
    for (var product : searchResults) {
      var bagConstraints = new GridBagConstraints();
      bagConstraints.gridwidth = 1;
      bagConstraints.gridy = row;
      bagConstraints.ipadx = 5;
      bagConstraints.ipady = 2;
      bagConstraints.fill = GridBagConstraints.BOTH;
      bagConstraints.weightx = 1;

      var pic = new Picture(30, 30);
      var imageIcon = model.getPicture(product.getProductNum());
      var image = imageIcon.getImage();
      var scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      pic.set(new ImageIcon(scaledImage));

      var desc = new JLabel(product.getDescription());
      desc.setBorder(BorderFactory.createLineBorder(Color.black));

      var price = new JLabel("£" + product.getPrice());
      price.setBorder(BorderFactory.createLineBorder(Color.black));
      price.setHorizontalAlignment(JLabel.CENTER);

      var stock = new JLabel(String.valueOf(product.getQuantity()));
      stock.setBorder(BorderFactory.createLineBorder(Color.black));
      stock.setHorizontalAlignment(JLabel.CENTER);

      var addButton = new JButton("Add");
      addButton.addActionListener(x -> {
        cont.addToBasket(product);
      });

      scrollPanePanel.add(pic, bagConstraints);
      scrollPanePanel.add(desc, bagConstraints);
      scrollPanePanel.add(price, bagConstraints);
      scrollPanePanel.add(stock, bagConstraints);
      scrollPanePanel.add(addButton, bagConstraints);
      row++;
    }
    searchScrollPane.setViewportView(scrollPanePanel);

    // draw basket
    layout = new GridBagLayout();
    var basketPanel = new JPanel(layout);
    row = 0;
    for (var product : model.getBasket())
    {
      var bagConstraints = new GridBagConstraints();
      bagConstraints.gridwidth = 1;
      bagConstraints.gridy = row;
      bagConstraints.ipadx = 5;
      bagConstraints.ipady = 2;
      bagConstraints.fill = GridBagConstraints.BOTH;
      bagConstraints.weightx = 1;

      var desc = new JLabel(product.getDescription());
      desc.setBorder(BorderFactory.createLineBorder(Color.black));
      desc.setHorizontalAlignment(JLabel.CENTER);

      var price = new JLabel("£" + product.getPrice());
      price.setBorder(BorderFactory.createLineBorder(Color.black));
      price.setHorizontalAlignment(JLabel.CENTER);

      var quantity = new JLabel(String.valueOf(product.getQuantity()));
      quantity.setBorder(BorderFactory.createLineBorder(Color.black));
      quantity.setHorizontalAlignment(JLabel.CENTER);

      var addButton = new JButton("+");
      addButton.setMargin(new Insets(0, 0, 0, 0));
      addButton.setPreferredSize(new Dimension(20, 20));
      addButton.addActionListener(x -> cont.incrementProduct(product));

      var subButton = new JButton("-");
      subButton.setMargin(new Insets(0, 0, 0, 0));
      subButton.setPreferredSize(new Dimension(20, 20));
      subButton.addActionListener(x -> cont.decrementProduct(product));

      basketPanel.add(desc, bagConstraints);
      basketPanel.add(price, bagConstraints);
      basketPanel.add(addButton, bagConstraints);
      basketPanel.add(quantity, bagConstraints);
      basketPanel.add(subButton, bagConstraints);
      row++;
    }
    basketScrollPane.setViewportView(basketPanel);

    // update total
    var decimalFormat = new DecimalFormat("0.00");
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

    var count = 0;
    var price = 0.0;
    for (var product : model.getBasket())
    {
      count += product.getQuantity();
      price += product.getPrice() * product.getQuantity();
    }

    totalLabel.setText(count + " Items - Total £" + decimalFormat.format(price));

    searchBar.requestFocus();
  }
}
