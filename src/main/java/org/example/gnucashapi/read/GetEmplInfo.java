package org.example.gnucashapi.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashEmployee;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.api.read.spec.GnuCashEmployeeVoucher;
import org.gnucash.base.basetypes.simple.GCshEmplID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

public class GetEmplInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName = "example_in.gnucash";
    private static Helper.Mode mode    = Helper.Mode.ID;
    private static GCshEmplID emplID   = new GCshEmplID("xyz");
    private static String emplName     = "abc";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetEmplInfo tool = new GetEmplInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashEmployee empl = gcshFile.getEmployeeByID(emplID);
		if ( mode == Helper.Mode.ID ) {
			empl = gcshFile.getEmployeeByID(emplID);
			if ( empl == null ) {
				System.err.println("Found no account with that ID");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Helper.Mode.NAME ) {
			Collection<GnuCashEmployee> emplList = null;
			emplList = gcshFile.getEmployeesByUserName(emplName, true);
			if ( emplList.size() == 0 ) {
				System.err.println("Found no account with that name.");
				throw new NoEntryFoundException();
			} else if ( emplList.size() > 1 ) {
				System.err.println("Found several accounts with that name.");
				System.err.println("Taking first one.");
			}
			empl = emplList.iterator().next(); // first element
		}

		// ------------------------

		try {
			System.out.println("ID:                " + empl.getID());
		} catch (Exception exc) {
			System.out.println("ID:                " + "ERROR");
		}

		try {
			System.out.println("Number:            '" + empl.getNumber() + "'");
		} catch (Exception exc) {
			System.out.println("Number:            " + "ERROR");
		}

		try {
			System.out.println("User name:         '" + empl.getUserName() + "'");
		} catch (Exception exc) {
			System.out.println("User name:         " + "ERROR");
		}

		try {
			System.out.println("Address:           '" + empl.getAddress() + "'");
		} catch (Exception exc) {
			System.out.println("Address:           " + "ERROR");
		}

		System.out.println("");
		System.out.println("Expenses generated:");
		try {
			System.out.println(" - direct:  " + empl.getExpensesGeneratedFormatted());
		} catch (Exception exc) {
			System.out.println(" - direct:  " + "ERROR");
		}

		System.out.println("Outstanding value:");
		try {
			System.out.println(" - direct: " + empl.getOutstandingValueFormatted());
		} catch (Exception exc) {
			System.out.println(" - direct: " + "ERROR");
		}

		// ---

		showVouchers(empl);
	}

	// -----------------------------------------------------------------

	private void showVouchers(GnuCashEmployee empl) throws Exception {
		System.out.println("");
		System.out.println("Vouchers:");

		System.out.println("Number of open vouchers: " + empl.getNofOpenVouchers());

		System.out.println("");
		System.out.println("Paid vouchers (direct):");
		for ( GnuCashEmployeeVoucher vch : empl.getPaidVouchers() ) {
			System.out.println(" - " + vch.toString());
		}

		System.out.println("");
		System.out.println("Unpaid vouchers (direct):");
		for ( GnuCashEmployeeVoucher vch : empl.getUnpaidVouchers() ) {
			System.out.println(" - " + vch.toString());
		}

		// There are no "employee jobs" and thus no paid / unpaid vouchers
		// "via jobs"
	}
}
