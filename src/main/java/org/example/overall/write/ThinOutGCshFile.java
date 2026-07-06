package org.example.overall.write;

import java.io.File;
import java.util.ArrayList;

import org.gnucash.api.read.GnuCashAccount;
import org.gnucash.api.read.GnuCashTransaction;
import org.gnucash.api.read.GnuCashTransactionSplit;
import org.gnucash.api.write.GnuCashWritableAccount;
import org.gnucash.api.write.GnuCashWritableFile;
import org.gnucash.api.write.GnuCashWritableTransaction;
import org.gnucash.api.write.impl.GnuCashWritableFileImpl;
import org.gnucash.base.basetypes.simple.GCshAcctID;

// This program will "thin-out" a GnuCash file, i.e.
// batch-delete a number of objects. 
// 
// You might need that when you want to take a real-world
// file as a template and experiment with a smaller/
// less complicated version of it (for test purposes, say).
//
// The author is currently working on a full-fledged tool
// that does this in a more sophisticated and generally-
// applicable way (not published yet). What you see here is 
// a simplified version of it.
// 
// Apart from that, this example essentially demonstrates 
// how to delete objects (as opposed to all the other examples 
// that only read, generate or update objects). And since this 
// is a non-trivial exercise (you have to take into account 
// the dependencies between the objects, and there is some
// API-internal bookkeeping involved), it merits an example
// program of its own.
public class ThinOutGCshFile {
	private static final int NOF_ITER_MAX = 10;

	// -----------------------------------------------------------------

	// BEGIN Example data -- adapt to your needs
	private static String gcshInFileName  = "example_in.kmy";
	private static String gcshOutFileName = "example_out.kmy";

	// Fill the following list with IDs of accounts to be *kept*.
	// Transactions referencing these accounts will be kept as well.
	// You only have to specifiy the highest-level account in a branch
	// of the account tree -- all its children will be kept as well.
	private static ArrayList<GCshAcctID> lofAcctIDs = new ArrayList<GCshAcctID>();
	// END Example data

	// -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			ThinOutGCshFile tool = new ThinOutGCshFile();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashWritableFile gcshFile = new GnuCashWritableFileImpl(new File(gcshInFileName), true);

		expandExcludeList(gcshFile);

		// ---
		// BEGIN CORE

		System.out.println("");
		System.out.println("----------------------------------------------");
		System.out.println("Transactions");
		deleteTransactions(gcshFile);

		System.out.println("");
		System.out.println("----------------------------------------------");
		System.out.println("Accounts");
		deleteAcctsIter(gcshFile);

		// END CORE
		// ---

		// Write thinned-out data to file
		System.out.print("Writing file: ");
		gcshFile.writeFile(new File(gcshOutFileName));
		System.out.println("OK");
	}

	// -----------------------------------------------------------------

	// Expand list of accounts to be kept with children of accounts
	// initially specified above. All the way down to the leaves.
	private void expandExcludeList(GnuCashWritableFile gcshFile) {
		ArrayList<GCshAcctID> newExclList = new ArrayList<GCshAcctID>();
		for ( GCshAcctID acctID : lofAcctIDs ) {
			newExclList.add(acctID);
			GnuCashAccount acct = gcshFile.getAccountByID(acctID);
			for ( GnuCashAccount subAcct : acct.getChildren() )
				newExclList.add(subAcct.getID());
		}

		lofAcctIDs = newExclList;

		System.out.println("Expanded exlude list: " + lofAcctIDs);
	}

	private void deleteTransactions(GnuCashWritableFile gcshFile) {
		for ( GnuCashWritableTransaction trx : gcshFile.getWritableTransactions() ) {
			if ( toBeDeletedTrx(gcshFile, trx) ) {
				gcshFile.removeTransaction(trx);
			}
		}
	}

	private void deleteAcctsIter(GnuCashWritableFile gcshFile) {
		int nofIter = 0;
		while ( nofIter < NOF_ITER_MAX ) { // trivial exist of iteration. This can be improved, of course
			System.out.println("");
			System.out.println("-----------------------");
			System.out.println("Iteration " + (nofIter + 1));

			doAcctIteration(gcshFile);

			nofIter++;
		}
	}

	private void doAcctIteration(GnuCashWritableFile gcshFile) {
		for ( GnuCashWritableAccount acct : gcshFile.getWritableAccounts() ) {
			if ( toBeDeletedAcct(gcshFile, acct) ) {
				acct.remove();
			}
		}
	}

	// ---------------------------------------------------------------

	private boolean toBeDeletedTrx(GnuCashWritableFile gcshFile, GnuCashTransaction trx) {
		boolean result = true;

		// Do not delete transactions pointing to one of the
		// accounts in the exclude-list
		for ( GnuCashTransactionSplit splt : trx.getSplits() ) {
			if ( lofAcctIDs.contains( splt.getAccountID() ) ) {
				result = false;
				continue;
			}
		}
		
		// More criteria, if you want, e.g.: date, action, etc.
		// ...

		return result;
	}

	private boolean toBeDeletedAcct(GnuCashWritableFile gcshFile, GnuCashAccount acct) {
		// Do not delete the top-level accounts (obviously)
		if ( acct.isRootAccount() )
			return false; 

		// Do not delete accounts that have children
		if ( acct.getChildren() != null ) {
			if ( acct.getChildren().size() > 0 )
				return false;
		}

		// Do not delete accounts that are in the exclude-list
		if ( lofAcctIDs.contains( acct.getID() ) )
			return false; 

		// And of course: Do not delete accounts that have transactions 
		// pointing to them
		if ( acct.hasTransactions() )
			return false;
		
		// More criteria, if you want, e.g.: account type.
		// ...

		return true;
	}

}
