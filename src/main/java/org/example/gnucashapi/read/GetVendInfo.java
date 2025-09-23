package org.example.gnucashapi.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashGenerInvoice;
import org.gnucash.api.read.GnuCashVendor;
import org.gnucash.api.read.aux.GCshBillTerms;
import org.gnucash.api.read.aux.GCshTaxTable;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.api.read.spec.GnuCashJobInvoice;
import org.gnucash.api.read.spec.GnuCashVendorBill;
import org.gnucash.api.read.spec.GnuCashVendorJob;
import org.gnucash.base.basetypes.simple.GCshVendID;
import org.gnucash.base.basetypes.simple.aux.GCshBllTrmID;
import org.gnucash.base.basetypes.simple.aux.GCshTaxTabID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

public class GetVendInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName = "example_in.gnucash";
    private static Helper.Mode mode    = Helper.Mode.ID;
    private static GCshVendID vendID   = new GCshVendID("xyz");
    private static String vendName     = "abc";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetVendInfo tool = new GetVendInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashVendor vend = gcshFile.getVendorByID(vendID);
		if ( mode == Helper.Mode.ID ) {
			vend = gcshFile.getVendorByID(vendID);
			if ( vend == null ) {
				System.err.println("Found no vendor with that ID");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Helper.Mode.NAME ) {
			Collection<GnuCashVendor> vendList = null;
			vendList = gcshFile.getVendorsByName(vendName, true);
			if ( vendList.size() == 0 ) {
				System.err.println("Found no vendor with that name.");
				throw new NoEntryFoundException();
			} else if ( vendList.size() > 1 ) {
				System.err.println("Found several vendors with that name.");
				System.err.println("Taking first one.");
			}
			vend = vendList.iterator().next(); // first element
		}

		// ------------------------

		try {
			System.out.println("ID:                " + vend.getID());
		} catch (Exception exc) {
			System.out.println("ID:                " + "ERROR");
		}

		try {
			System.out.println("Number:            '" + vend.getNumber() + "'");
		} catch (Exception exc) {
			System.out.println("Number:            " + "ERROR");
		}

		try {
			System.out.println("Name:              '" + vend.getName() + "'");
		} catch (Exception exc) {
			System.out.println("Name:              " + "ERROR");
		}

		try {
			System.out.println("Address:           '" + vend.getAddress() + "'");
		} catch (Exception exc) {
			System.out.println("Address:           " + "ERROR");
		}

		System.out.println("");
		try {
			GCshTaxTabID taxTabID = vend.getTaxTableID();
			System.out.println("Tax table ID:      " + taxTabID);

			if ( vend.getTaxTableID() != null ) {
				try {
					GCshTaxTable taxTab = gcshFile.getTaxTableByID(taxTabID);
					System.out.println("Tax table:        " + taxTab.toString());
				} catch (Exception exc2) {
					System.out.println("Tax table:        " + "ERROR");
				}
			}
		} catch (Exception exc) {
			System.out.println("Tax table ID:      " + "ERROR");
		}

		System.out.println("");
		try {
			GCshBllTrmID bllTrmID = vend.getTermsID();
			System.out.println("Bill terms ID:     " + bllTrmID);

			if ( vend.getTermsID() != null ) {
				try {
					GCshBillTerms bllTrm = gcshFile.getBillTermsByID(bllTrmID);
					System.out.println("Bill Terms:        " + bllTrm.toString());
				} catch (Exception exc2) {
					System.out.println("Bill Terms:        " + "ERROR");
				}
			}
		} catch (Exception exc) {
			System.out.println("Bill terms ID:     " + "ERROR");
		}

		System.out.println("");
		System.out.println("Expenses generated:");
		try {
			System.out.println(
					" - direct: " + vend.getExpensesGeneratedFormatted(GnuCashGenerInvoice.ReadVariant.DIRECT));
		} catch (Exception exc) {
			System.out.println(" - direct: " + "ERROR");
		}

		try {
			System.out.println(
					" - via all jobs: " + vend.getExpensesGeneratedFormatted(GnuCashGenerInvoice.ReadVariant.VIA_JOB));
		} catch (Exception exc) {
			System.out.println(" - via all jobs: " + "ERROR");
		}

		System.out.println("Outstanding value:");
		try {
			System.out
					.println(" - direct: " + vend.getOutstandingValueFormatted(GnuCashGenerInvoice.ReadVariant.DIRECT));
		} catch (Exception exc) {
			System.out.println(" - direct: " + "ERROR");
		}

		try {
			System.out.println(
					" - via all jobs: " + vend.getOutstandingValueFormatted(GnuCashGenerInvoice.ReadVariant.VIA_JOB));
		} catch (Exception exc) {
			System.out.println(" - via all jobs: " + "ERROR");
		}

		// ---

		showJobs(vend);
		showBills(vend);
	}

	// -----------------------------------------------------------------

	private void showJobs(GnuCashVendor vend) {
		System.out.println("");
		System.out.println("Jobs:");
		for ( GnuCashVendorJob job : vend.getJobs() ) {
			System.out.println(" - " + job.toString());
		}
	}

	private void showBills(GnuCashVendor vend) throws Exception {
		System.out.println("");
		System.out.println("Bills:");

		System.out.println("Number of open bills: " + vend.getNofOpenBills());

		System.out.println("");
		System.out.println("Paid bills (direct):");
		for ( GnuCashVendorBill bll : vend.getPaidBills_direct() ) {
			System.out.println(" - " + bll.toString());
		}

		System.out.println("");
		System.out.println("Paid bills (via all jobs):");
		for ( GnuCashJobInvoice bll : vend.getPaidBills_viaAllJobs() ) {
			System.out.println(" - " + bll.toString());
		}

		System.out.println("");
		System.out.println("Unpaid bills (direct):");
		for ( GnuCashVendorBill bll : vend.getUnpaidBills_direct() ) {
			System.out.println(" - " + bll.toString());
		}

		System.out.println("");
		System.out.println("Unpaid bills (via all jobs):");
		for ( GnuCashJobInvoice bll : vend.getUnpaidBills_viaAllJobs() ) {
			System.out.println(" - " + bll.toString());
		}
	}
}
