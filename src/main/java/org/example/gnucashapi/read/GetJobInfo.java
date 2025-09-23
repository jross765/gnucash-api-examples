package org.example.gnucashapi.read;

import java.io.File;
import java.util.Collection;

import org.gnucash.api.read.GnuCashGenerJob;
import org.gnucash.api.read.impl.GnuCashFileImpl;
import org.gnucash.api.read.spec.GnuCashJobInvoice;
import org.gnucash.base.basetypes.simple.GCshGenerJobID;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;

public class GetJobInfo {
    // BEGIN Example data -- adapt to your needs
    private static String gcshFileName  = "example_in.gnucash";
    private static Helper.Mode mode     = Helper.Mode.ID;
    private static GCshGenerJobID jobID = new GCshGenerJobID("xyz");
    private static String jobName       = "abc";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GetJobInfo tool = new GetJobInfo();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashFileImpl gcshFile = new GnuCashFileImpl(new File(gcshFileName));

		GnuCashGenerJob job = null;
		if ( mode == Helper.Mode.ID ) {
			job = gcshFile.getGenerJobByID(jobID);
			if ( job == null ) {
				System.err.println("Found no job with that ID");
				throw new NoEntryFoundException();
			}
		} else if ( mode == Helper.Mode.NAME ) {
			Collection<GnuCashGenerJob> jobList = null;
			jobList = gcshFile.getGenerJobsByName(jobName, true);
			if ( jobList.size() == 0 ) {
				System.err.println("Found no job with that name.");
				throw new NoEntryFoundException();
			} else if ( jobList.size() > 1 ) {
				System.err.println("Found several jobs with that name.");
				System.err.println("Taking first one.");
			}
			job = jobList.iterator().next(); // first element
		}

		// ------------------------

		try {
			System.out.println("ID:              " + job.getID());
		} catch (Exception exc) {
			System.out.println("ID:              " + "ERROR");
		}

		try {
			System.out.println("Number:          " + job.getNumber());
		} catch (Exception exc) {
			System.out.println("Number:          " + "ERROR");
		}

		try {
			System.out.println("Name:            " + job.getName());
		} catch (Exception exc) {
			System.out.println("Name:            " + "ERROR");
		}

		try {
			System.out.println("Owner type:      " + job.getOwnerType());
		} catch (Exception exc) {
			System.out.println("Owner type:      " + "ERROR");
		}

		try {
			System.out.println("Owner ID:        " + job.getOwnerID());
		} catch (Exception exc) {
			System.out.println("Owner ID:        " + "ERROR");
		}

		System.out.println("");
		try {
			System.out.println("Income generated:  " + job.getIncomeGeneratedFormatted());
		} catch (Exception exc) {
			System.out.println("Income generated:  " + "ERROR");
		}

		try {
			System.out.println("Outstanding value: " + job.getOutstandingValueFormatted());
		} catch (Exception exc) {
			System.out.println("Outstanding value: " + "ERROR");
		}

		// ---

		showInvoices(job);
	}

	// -----------------------------------------------------------------

	private void showInvoices(GnuCashGenerJob job) throws Exception {
		System.out.println("");
		System.out.println("Invoices:");

		System.out.println("");
		System.out.println("Paid invoices:");
		for ( GnuCashJobInvoice invc : job.getPaidInvoices() ) {
			System.out.println(" - " + invc.toString());
		}

		System.out.println("");
		System.out.println("Unpaid invoices:");
		for ( GnuCashJobInvoice invc : job.getUnpaidInvoices() ) {
			System.out.println(" - " + invc.toString());
		}
	}
}
