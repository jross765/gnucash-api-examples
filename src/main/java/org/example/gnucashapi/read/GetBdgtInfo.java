package org.example.gnucashapi.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashBudget;
import org.gnucash.api.read.aux.GCshBudgetAccount;
import org.gnucash.api.read.aux.GCshBudgetPeriod;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.base.basetypes.simple.GCshBdgtID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;
import xyz.schnorxoborx.base.beanbase.TooManyEntriesFoundException;

public class GetBdgtInfo {

	public enum Mode {
		ID,
		NAME
	}

	// -----------------------------------------------------------------

	// BEGIN Example data -- adapt to your needs
	private static String     gcshFileName = "example_in.gnucash";
	private static Mode       mode         = Mode.ID;
	private static GCshBdgtID bdgtID       = new GCshBdgtID("xyz");
	private static String     bdgtName     = "def";
	// END Example data

	// -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetBdgtInfo tool = new GetBdgtInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashBudget bdgt = null;
		if ( mode == Mode.ID ) {
			bdgt = gcshFile.getBudgetByID(bdgtID);
			if ( bdgt == null ) {
				System.err.println("Could not find a budget with this ID.");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Mode.NAME ) {
			Collection<GnuCashBudget> cmdtyList = gcshFile.getBudgetsByName(bdgtName);
			if ( cmdtyList.size() == 0 ) {
				System.err.println("Could not find budgets matching this name.");
				throw new NoEntryFoundException();
			}
			if ( cmdtyList.size() > 1 ) {
				System.err.println("Found " + cmdtyList.size() + "budgets matching this name.");
				System.err.println("Please specify more precisely.");
				throw new TooManyEntriesFoundException();
			}
			bdgt = cmdtyList.iterator().next(); // first element
		}

		// ----------------------------

		try {
			System.out.println("ID:                " + bdgt.getID());
		} catch (Exception exc) {
			System.out.println("D:                 " + "ERROR");
		}

		try {
			System.out.println("toString:          " + bdgt.toString());
		} catch (Exception exc) {
			System.out.println("toString:          " + "ERROR");
		}

		try {
			System.out.println("Name:              '" + bdgt.getName() + "'");
		} catch (Exception exc) {
			System.out.println("Name:              " + "ERROR");
		}

		try {
			System.out.println("Description:       " + bdgt.getDescription());
		} catch (Exception exc) {
			System.out.println("Description:       " + "ERROR");
		}

		try {
			System.out.println("No. of periods:    " + bdgt.getNofPeriods());
		} catch (Exception exc) {
			System.out.println("No. of periods:    " + "ERROR");
		}

		// ---

		showAccounts(bdgt);
	}

	// -----------------------------------------------------------------

	private void showAccounts(GnuCashBudget bdgt) {
		System.out.println("");
		System.out.println("Accounts:");

		System.out.println("");
		System.out.println("Number of accounts: " + bdgt.getAccounts().size());

		System.out.println("");
		for ( GCshBudgetAccount bdgtAcct : bdgt.getAccounts() ) {
			System.out.println(" - " + bdgtAcct.toString());
			System.out.println("   Number of periods: " + bdgtAcct.getPeriods().size());
			for ( GCshBudgetPeriod bdgtPrd : bdgtAcct.getPeriods() ) {
				System.out.println("   o " + bdgtPrd.toString());
			}
		}
	}
}
