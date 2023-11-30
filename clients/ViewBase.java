package clients;

import clients.backDoor.BackDoorController;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import middle.IStockReader;
import middle.MiddleFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ViewBase<O extends ControllerBase> implements Observer {
    protected static final int H = 300;       // Height of window pixels
    protected static final int W = 400;       // Width  of window pixels

    protected final JLabel theAction  = new JLabel();
    protected final JTextField  theInput   = new JTextField();
    protected final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();

    protected IStockReader theStock   = null;
    protected O cont = null;

    public ViewBase(RootPaneContainer rpc, MiddleFactory mf, int x, int y )
    {
        try                                             //
        {
            theStock  = mf.makeStockReader();             // Database Access
        } catch ( Exception e )
        {
            System.out.println("Exception: " + e.getMessage() );
        }
        Container cp         = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc;         // Root Window
        cp.setLayout(null);                             // No layout manager
        rootWindow.setSize( W, H );                     // Size of Window
        rootWindow.setLocation( x, y );

        Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

        theAction.setBounds( 110, 25 , 270, 20 );       // Message area
        theAction.setText( "" );                        //  Blank
        cp.add( theAction );                            //  Add to canvas

        theInput.setBounds( 110, 50, 270, 40 );         // Product no area
        theInput.setText("");                           // Blank
        cp.add( theInput );                             //  Add to canvas

        theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
        theOutput.setText( "" );                        //  Blank
        theOutput.setFont( f );                         //  Uses font
        theOutput.setEditable(false);
        cp.add( theSP );                                //  Add to canvas
        theSP.getViewport().add( theOutput );           //  In TextArea

        rootWindow.setVisible( true );                  // Make visible);
        theInput.requestFocus();                        // Focus is here
    }

    public void setController( O c )
    {
        cont = c;
    }

    public void update(Observable modelC, Object arg )
    {
        ModelBase model = (ModelBase)modelC;
        String message = (String)arg;
        theAction.setText(message);

        theOutput.setText(model.getDisplayText());

        theInput.requestFocus();
    }
}
