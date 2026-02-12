package org.example.gnucashapispec.read;

import java.io.File;

import org.gnucash.api.read.GnuCashPrice;
import org.gnucash.apispec.read.GnuCashCurrency;
import org.gnucash.apispec.read.impl.GnuCashFileExtImpl;
import org.gnucash.base.basetypes.complex.GCshCurrID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

public class GetCurrInfo {
    // BEGIN Example data -- adapt to your needs
    private static String kmmFileName = "example_in.gnucash";
    private static String symbol      = "abc";
    // END Example data

    // -----------------------------------------------------------------

    public static void main(String[] args) {
	try {
	    GetCurrInfo tool = new GetCurrInfo();
	    tool.kernel();
	} catch (Exception exc) {
	    System.err.println("Execution exception. Aborting.");
	    exc.printStackTrace();
	    System.exit(1);
	}
    }

    protected void kernel() throws Exception {
	GnuCashFileExtImpl gcshFile = new GnuCashFileExtImpl(new File(kmmFileName));

	GCshCurrID currID = new GCshCurrID(symbol);
	GnuCashCurrency curr = gcshFile.getCurrencyByID(currID);
	if (curr == null) {
	    System.err.println("Could not find currency with qualif. ID " + currID.toString());
	    throw new NoEntryFoundException();
	}

	// ------------------------

	try {
	    System.out.println("Qualified ID:      '" + curr.getQualifID() + "'");
	} catch (Exception exc) {
	    System.out.println("Qualified ID:      " + "ERROR");
	}

	try {
	    System.out.println("Symbol:            '" + curr.getSymbol() + "'");
	} catch (Exception exc) {
	    System.out.println("Symbol:            " + "ERROR");
	}

	try {
	    System.out.println("toString:          " + curr.toString());
	} catch (Exception exc) {
	    System.out.println("toString:          " + "ERROR");
	}

	try {
	    System.out.println("Name:              '" + curr.getName() + "'");
	} catch (Exception exc) {
	    System.out.println("Name:              " + "ERROR");
	}

	try {
	    System.out.println("Fraction:          " + curr.getFraction());
	} catch (Exception exc) {
	    System.out.println("PP:                " + "ERROR");
	}

	// ---

	showQuotes(curr);
    }

    // -----------------------------------------------------------------

    private void showQuotes(GnuCashCurrency curr) {
	System.out.println("");
	System.out.println("Quotes:");

	System.out.println("");
	System.out.println("Number of quotes: " + curr.getQuotes().size());

	System.out.println("");
	for ( GnuCashPrice prc : curr.getQuotes() ) {
	    System.out.println(" - " + prc.toString());
	}

	System.out.println("");
	System.out.println("Youngest Quote:");
	System.out.println(curr.getYoungestQuote());
    }
}
