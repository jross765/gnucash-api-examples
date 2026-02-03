package org.example.gnucashapispec.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashCommodity;
import org.gnucash.api.read.GnuCashPrice;
import org.gnucash.apispec.read.GnuCashSecurity;
import org.gnucash.apispec.read.impl.GnuCashFileExtImpl;
import org.gnucash.base.basetypes.complex.GCshCmdtyNameSpace;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

// Cf. org.example.gnucashapi.read.GetCmdtyInfo
public class GetSecInfo {

    enum Mode {
    	ISIN, 
    	EXCHANGE_TICKER, 
    	NAME
    }

    // -----------------------------------------------------------------

    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName     = "example_in.gnucash";
    private static Mode   mode             = Mode.ISIN;
    private static GCshCmdtyNameSpace.Exchange exchange = GCshCmdtyNameSpace.Exchange.EURONEXT;
    private static String ticker           = "MBG";
    private static String isin             = "DE0007100000";
    private static String searchName       = "merced";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetSecInfo tool = new GetSecInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileExtImpl gcshFile = new GnuCashFileExtImpl(new File(gcshFileName));

		GnuCashSecurity sec = null;
		if ( mode == Mode.ISIN ) {
			sec = gcshFile.getSecurityByXCode(isin);
			if ( sec == null ) {
				System.err.println("Could not find securities with this ISIN.");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Mode.EXCHANGE_TICKER ) {
			sec = gcshFile.getSecurityByQualifID(exchange, ticker);
			if ( sec == null ) {
				System.err.println("Could not find securities with this exchange/ticker.");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Mode.NAME ) {
			Collection<GnuCashSecurity> cmdtyList = gcshFile.getSecuritiesByName(searchName);
			if ( cmdtyList.size() == 0 ) {
				System.err.println("Could not find securities matching this name.");
				throw new NoEntryFoundException();
			}
			if ( cmdtyList.size() > 1 ) {
				System.err.println("Found several securities with that name.");
				System.err.println("Taking first one.");
			}
			sec = cmdtyList.iterator().next(); // first element
		}

		// ------------------------

		try {
			System.out.println("Qualified ID:      '" + sec.getQualifID() + "'");
		} catch (Exception exc) {
			System.out.println("Qualified ID:      " + "ERROR");
		}

		try {
			System.out.println("X-Code:            " + sec.getXCode());
		} catch (Exception exc) {
			System.out.println("X-Code :           " + "ERROR");
		}

		try {
			System.out.println("Name:              '" + sec.getName() + "'");
		} catch (Exception exc) {
			System.out.println("Name:              " + "ERROR");
		}

		try {
			System.out.println("Fraction:          " + sec.getFraction());
		} catch (Exception exc) {
			System.out.println("Fraction:          " + "ERROR");
		}

		// ---

		showQuotes(sec);
	}

	// -----------------------------------------------------------------

	private void showQuotes(GnuCashCommodity cmdty) {
		System.out.println("");
		System.out.println("Quotes:");

		System.out.println("");
		System.out.println("Number of quotes: " + cmdty.getQuotes().size());

		System.out.println("");
		for ( GnuCashPrice prc : cmdty.getQuotes() ) {
			System.out.println(" - " + prc.toString());
		}

		System.out.println("");
		System.out.println("Youngest Quote:");
		System.out.println(cmdty.getYoungestQuote());
	}
}
