package clients.collection;

import clients.ViewBase;
import middle.MiddleFactory;
import middle.IOrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CollectView extends ViewBase
{
    private static final String COLLECT = "Collect";

  private final JButton     theBtCollect= new JButton( COLLECT );
 
  private IOrderProcessing theOrder = null;
  private CollectController cont     = null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public CollectView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
      super(rpc, mf, x, y);

    try                                           // 
    {      
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    Container cp         = rpc.getContentPane();    // Content Pane

    theBtCollect.setBounds( 16, 25+60*0, 80, 40 );  // Check Button
    theBtCollect.addActionListener(                 // Call back code
      e -> cont.doCollect( theInput.getText()) );
    cp.add( theBtCollect );                         //  Add to canvas
  }  
  
  public void setController( CollectController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override 
  public void update( Observable modelC, Object arg )
  {
      super.update(modelC, arg);

    CollectModel model  = (CollectModel) modelC;
    
    theOutput.setText( model.getResponse() );
  }
}
