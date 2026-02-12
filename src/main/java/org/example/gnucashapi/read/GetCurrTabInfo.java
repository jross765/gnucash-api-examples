package org.example.gnucashapi.read;

import java.io.File;

import org.gnucash.api.pricedb.ComplexPriceTable;
import org.gnucash.api.read.impl.GnuCashFileImpl;

public class GetCurrTabInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName = "example_in.gnucash";
    // END Example data

    // -----------------------------------------------------------------

    public static void main(String[] args) {
    	try {
    		GetCurrTabInfo tool = new GetCurrTabInfo();
    		tool.kernel();
    	} catch (Exception exc) {
    		System.err.println("Execution exception. Aborting.");
    		exc.printStackTrace();
    		System.exit(1);
    	}
    }

    protected void kernel() throws Exception {
    	GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

    	ComplexPriceTable tab = gcshFile.getCurrencyTable();
    	System.out.println(tab.toString());
    }
}
