package clients;

import catalogue.Product;
import debug.DEBUG;
import middle.IStockReaderWriter;
import middle.MiddleFactory;
import middle.StockException;

import java.util.Observable;

public class ModelBase extends Observable {

    protected String displayText = "";
    protected IStockReaderWriter theStock = null;

    public ModelBase(MiddleFactory mf)
    {
        try
        {
            theStock = mf.makeStockReadWriter();
        } catch ( Exception e ) {
            DEBUG.error("ModelBase.ctor\n%s", e.getMessage() );
        }
    }

    public String getDisplayText() { return displayText; }

    /**
     * Gets whether the given product is in stock.
     *
     * @param productNum The product number of the stock to search for.
     * @return True if the product was found and in stock, False if there is no stock or the product was not found.
     */
    public Boolean isInStock(String productNum)
    {
        return getStockAmount(productNum) > 0;
    }

    /**
     * Returns the amount of stock currently in the warehouse of a specific item.
     *
     * @param productNum The product number of the stock to search for.
     * @return The amount of the product currently stored. If the product was not found, or an error occurred, -1 is returned.
     */
    public int getStockAmount(String productNum )
    {
        var pn  = productNum.trim();
        try
        {
            if (theStock.exists(pn))
            {
                Product pr = theStock.getDetails(pn);
                return pr.getQuantity();
            } else {
                return -1;
            }
        } catch( StockException e ) {
            DEBUG.error("ModelBase.getStockAmount()\n%s", e.getMessage());
            return -1;
        }
    }

    public Product getProduct(String productNum)
    {
        var pn  = productNum.trim();

        try
        {
            if (theStock.exists(pn))
            {
                return theStock.getDetails(pn);
            }
            else
            {
                return null;
            }

        } catch(StockException e) {
            DEBUG.error("ModelBase.getProduct()\n%s", e.getMessage());
            return null;
        }
    }

    public String getAction(String productNum)
    {
        var stockAmount = getStockAmount(productNum);
        var pr = getProduct(productNum);

        String theAction = "";

        if (stockAmount > 0)
        {
            theAction =
                    String.format( "%s : £%7.2f (%2d in stock) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity() );
        }
        else if (stockAmount == 0)
        {
            theAction = pr.getDescription() + " not in stock" ;
        }
        else if (stockAmount == -1)
        {
            theAction = "Unknown product number " + productNum;
        }

        return theAction;
    }
}