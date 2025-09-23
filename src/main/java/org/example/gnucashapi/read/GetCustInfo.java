package org.example.gnucashapi.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashCustomer;
import org.gnucash.api.read.GnuCashGenerInvoice;
import org.gnucash.api.read.aux.GCshBillTerms;
import org.gnucash.api.read.aux.GCshTaxTable;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.api.read.spec.GnuCashCustomerInvoice;
import org.gnucash.api.read.spec.GnuCashCustomerJob;
import org.gnucash.api.read.spec.GnuCashJobInvoice;
import org.gnucash.base.basetypes.simple.GCshCustID;
import org.gnucash.base.basetypes.simple.aux.GCshBllTrmID;
import org.gnucash.base.basetypes.simple.aux.GCshTaxTabID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

public class GetCustInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName = "example_in.gnucash";
    private static Helper.Mode mode    = Helper.Mode.ID;
    private static GCshCustID custID   = new GCshCustID("xyz");
    private static String custName     = "abc";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetCustInfo tool = new GetCustInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashCustomer cust = gcshFile.getCustomerByID(custID);
		if ( mode == Helper.Mode.ID ) {
			cust = gcshFile.getCustomerByID(custID);
			if ( cust == null ) {
				System.err.println("Found no account with that ID");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Helper.Mode.NAME ) {
			Collection<GnuCashCustomer> custList = null;
			custList = gcshFile.getCustomersByName(custName, true);
			if ( custList.size() == 0 ) {
				System.err.println("Found no account with that name.");
				throw new NoEntryFoundException();
			} else if ( custList.size() > 1 ) {
				System.err.println("Found several accounts with that name.");
				System.err.println("Taking first one.");
			}
			cust = custList.iterator().next(); // first element
		}

		// ------------------------

		try {
			System.out.println("ID:                " + cust.getID());
		} catch (Exception exc) {
			System.out.println("ID:                " + "ERROR");
		}

		try {
			System.out.println("Number:            '" + cust.getNumber() + "'");
		} catch (Exception exc) {
			System.out.println("Number:            " + "ERROR");
		}

		try {
			System.out.println("Name:              '" + cust.getName() + "'");
		} catch (Exception exc) {
			System.out.println("Name:              " + "ERROR");
		}

		try {
			System.out.println("Address:           '" + cust.getAddress() + "'");
		} catch (Exception exc) {
			System.out.println("Address:           " + "ERROR");
		}

		System.out.println("");
		try {
			System.out.println("Discount:          " + cust.getDiscount());
		} catch (Exception exc) {
			System.out.println("Discount:          " + "ERROR");
		}

		try {
			System.out.println("Credit:            " + cust.getCredit());
		} catch (Exception exc) {
			System.out.println("Credit:            " + "ERROR");
		}

		System.out.println("");
		try {
			GCshTaxTabID taxTabID = cust.getTaxTableID();
			System.out.println("Tax table ID:      " + taxTabID);

			if ( cust.getTaxTableID() != null ) {
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
			GCshBllTrmID bllTrmID = cust.getTermsID();
			System.out.println("Bill terms ID:     " + bllTrmID);

			if ( cust.getTermsID() != null ) {
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
		System.out.println("Income generated:");
		try {
			System.out
					.println(" - direct:  " + cust.getIncomeGeneratedFormatted(GnuCashGenerInvoice.ReadVariant.DIRECT));
		} catch (Exception exc) {
			System.out.println(" - direct:  " + "ERROR");
		}

		try {
			System.out.println(
					" - via all jobs:  " + cust.getIncomeGeneratedFormatted(GnuCashGenerInvoice.ReadVariant.VIA_JOB));
		} catch (Exception exc) {
			System.out.println(" - via all jobs:  " + "ERROR");
		}

		System.out.println("Outstanding value:");
		try {
			System.out
					.println(" - direct: " + cust.getOutstandingValueFormatted(GnuCashGenerInvoice.ReadVariant.DIRECT));
		} catch (Exception exc) {
			System.out.println(" - direct: " + "ERROR");
		}

		try {
			System.out.println(
					" - via all jobs: " + cust.getOutstandingValueFormatted(GnuCashGenerInvoice.ReadVariant.VIA_JOB));
		} catch (Exception exc) {
			System.out.println(" - via all jobs: " + "ERROR");
		}

		// ---

		showJobs(cust);
		showInvoices(cust);
	}

	// -----------------------------------------------------------------

	private void showJobs(GnuCashCustomer cust) {
		System.out.println("");
		System.out.println("Jobs:");
		for ( GnuCashCustomerJob job : cust.getJobs() ) {
			System.out.println(" - " + job.toString());
		}
	}

	private void showInvoices(GnuCashCustomer cust) throws Exception {
		System.out.println("");
		System.out.println("Invoices:");

		System.out.println("Number of open invoices: " + cust.getNofOpenInvoices());

		System.out.println("");
		System.out.println("Paid invoices (direct):");
		for ( GnuCashCustomerInvoice invc : cust.getPaidInvoices_direct() ) {
			System.out.println(" - " + invc.toString());
		}

		System.out.println("");
		System.out.println("Paid invoices (via all jobs):");
		for ( GnuCashJobInvoice invc : cust.getPaidInvoices_viaAllJobs() ) {
			System.out.println(" - " + invc.toString());
		}

		System.out.println("");
		System.out.println("Unpaid invoices (direct):");
		for ( GnuCashCustomerInvoice invc : cust.getUnpaidInvoices_direct() ) {
			System.out.println(" - " + invc.toString());
		}

		System.out.println("");
		System.out.println("Unpaid invoices (via all jobs):");
		for ( GnuCashJobInvoice invc : cust.getUnpaidInvoices_viaAllJobs() ) {
			System.out.println(" - " + invc.toString());
		}
	}
}
