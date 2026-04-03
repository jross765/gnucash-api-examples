package org.example.gnucashapispec.read;

import java.io.File;

import org.apache.commons.numbers.fraction.BigFraction;
import org.gnucash.api.read.GnuCashTransaction;
import org.gnucash.api.read.GnuCashTransactionSplit;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.api.read.impl.GnuCashTransactionImpl;
import org.gnucash.apispec.read.GnuCashStockDividendTransaction;
import org.gnucash.apispec.read.impl.GnuCashStockDividendTransactionImpl;
import org.gnucash.base.basetypes.simple.GCshTrxID;

import xyz.schnorxoborx.base.cmdlinetools.Helper;
import xyz.schnorxoborx.base.numbers.FixedPointNumber;

public class GetStockDivTrxInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName = "example_in.gnucash";
    private static Helper.Mode mode    = Helper.Mode.ID;
    private static GCshTrxID trxID     = new GCshTrxID("xyz");
    private static String acctName     = "abc";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetStockDivTrxInfo tool = new GetStockDivTrxInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashTransaction genTrx = gcshFile.getTransactionByID(trxID);
		if ( genTrx == null ) {
			System.err.println("Error: No (generic) transaction with ID '" + trxID + "' in GnuCash file");
			System.exit(-1); 
		}

		GnuCashStockDividendTransaction specTrx = new GnuCashStockDividendTransactionImpl((GnuCashTransactionImpl) genTrx);
		if ( specTrx == null ) {
			System.err.println("Error: Transaction with ID '" + trxID + "' does not meet criteria for stock dividend transaction");
			System.exit(-1); 
		}
		
		// ---
		// Inherited from GnuCashTransaction:
		
		FixedPointNumber balance = specTrx.getBalance();
		BigFraction balanceRat = specTrx.getBalanceRat();
		
		int nofSplits = specTrx.getSplitsCount();
		GnuCashTransactionSplit splt1 = specTrx.getSplits().get(0);

		// ---
		// Inherited from GnuCashStockDividendTransaction:
		
		FixedPointNumber grossDiv = specTrx.getGrossDividend();
		FixedPointNumber feeTax = specTrx.getFeesTaxes();
		FixedPointNumber netDiv = specTrx.getNetDividend();

		BigFraction grossDivRat = specTrx.getGrossDividendRat();
		BigFraction feeTaxRat = specTrx.getFeesTaxesRat();
		BigFraction netDivRat = specTrx.getNetDividendRat();
		
		System.out.println("Stock acct. split:      " + specTrx.getStockAccountSplit());
		System.out.println("Income acct. split:     " + specTrx.getIncomeAccountSplit());
		for ( GnuCashTransactionSplit splt : specTrx.getExpensesSplits() ) {
			System.out.println("Expenses acct. split:   " + splt);
		}
		System.out.println("Offsetting acct. split: " + specTrx.getOffsettingAccountSplit());
		
		// ---
		
		System.out.println("");
		System.out.println("String rep. (1): " + specTrx.toString() );
		System.out.println("String rep. (2): " + ((GnuCashStockDividendTransactionImpl) specTrx).toStringHuman() );
	}

}
