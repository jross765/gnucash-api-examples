package org.example.gnucashapi.write.gen;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.numbers.fraction.BigFraction;
import org.gnucash.api.read.GnuCashTransactionSplit;
import org.gnucash.api.write.GnuCashWritableTransaction;
import org.gnucash.api.write.GnuCashWritableTransactionSplit;
import org.gnucash.api.write.impl.GnuCashWritableFileImpl;
import org.gnucash.base.basetypes.simple.GCshAcctID;

public class GenTrx {
    // BEGIN Example data -- adapt to your needs
    private static String gcshInFileName     = "example_in.gnucash";
    private static String gcshOutFileName    = "example_out.gnucash";
    
    // ---
    
    private static GCshAcctID       fromAcct1ID = new GCshAcctID("e617cfd1317b4e318caf5dfba51b172e"); // Root Account:Aktiva:Kassen:Kasse Ada
    private static GCshAcctID       toAcct1ID   = new GCshAcctID("0c405f3669a14606be3c1a62ac5455a9"); // Root Account:Aufwendungen:Bildung:Zeitungen
    private static GnuCashTransactionSplit.Action act1 = null;                                    // Do not set here
    private static BigFraction      amt1        = BigFraction.of(1234, 100);
    private static BigFraction      qty1        = amt1;
    private static LocalDate        datPst1     = LocalDate.of(2024, 2, 15);
    private static String           descr1      = "Bahnhof Zeitungskiosk";
    
    // ---
    
    private static GCshAcctID       fromAcct2ID  = new GCshAcctID("bbf77a599bd24a3dbfec3dd1d0bb9f5c"); // Root Account:Aktiva:Sichteinlagen:KK:Giro RaiBa
    private static GCshAcctID       toAcct21ID   = new GCshAcctID("b3741e92e3b9475b9d5a2dc8254a8111"); // Root Account:Aktiva:Depots:Depot RaiBa:DE0007164600 SAP
    private static GCshAcctID       toAcct22ID   = new GCshAcctID("d3f947fdfbf54240b0cfb09fea4963ca"); // Root Account:Aufwendungen:Sonstiges:Bankgebühren
    private static GnuCashTransactionSplit.Action act2 = GnuCashTransactionSplit.Action.BUY;
    private static BigFraction qty22             = BigFraction.of(15);
    // private static BigFraction prc1         = new BigFraction(1);       // optional
    private static BigFraction prc2              = BigFraction.of(15574, 100); // half-mandatory
    // private static BigFraction prc3         = prc1;                              // optional
    private static BigFraction      amt22        = qty22.multiply(prc2);       // net
    private static BigFraction      amt23        = BigFraction.of(95, 10);     // fees & commissions
    private static BigFraction      amt21        = amt22.add(amt23);           // gross
    private static BigFraction      qty21        = amt21;
    private static BigFraction      qty23        = amt23;
    private static LocalDate        datPst2      = LocalDate.of(2024, 1, 15);
    private static String           descr2       = "Aktienkauf";
    // END Example data

    // -----------------------------------------------------------------

	public static void main(String[] args) {
		try {
			GenTrx tool = new GenTrx();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
	}

	protected void kernel() throws Exception {
		GnuCashWritableFileImpl gcshFile = new GnuCashWritableFileImpl(new File(gcshInFileName));

		System.out.println("---------------------------");
		System.out.println("Generate transaction no. 1:");
		System.out.println("---------------------------");
		genTrx1(gcshFile);

		System.out.println("");
		System.out.println("---------------------------");
		System.out.println("Generate transaction no. 2:");
		System.out.println("---------------------------");
		genTrx2(gcshFile);

		System.out.println("");
		System.out.println("---------------------------");
		System.out.println("Write file:");
		System.out.println("---------------------------");
		gcshFile.writeFile(new File(gcshOutFileName));

		System.out.println("OK");
	}

	private void genTrx1(GnuCashWritableFileImpl gcshFile) {
		System.err.println("Account 1 name (from): '" + gcshFile.getAccountByID(fromAcct1ID).getQualifiedName() + "'");
		System.err.println("Account 2 name (to):   '" + gcshFile.getAccountByID(toAcct1ID).getQualifiedName() + "'");

		GnuCashWritableTransaction trx = gcshFile.createWritableTransaction();
		trx.setDescription(descr1);

		GnuCashWritableTransactionSplit splt1 = trx.createWritableSplit(gcshFile.getAccountByID(fromAcct1ID));
		splt1.setValue(amt1.negate());
		splt1.setQuantity(qty1.negate());

		GnuCashWritableTransactionSplit splt2 = trx.createWritableSplit(gcshFile.getAccountByID(toAcct1ID));
		splt2.setValue(amt1);
		splt2.setQuantity(qty1);

		trx.setDatePosted(datPst1);
		trx.setDateEntered(LocalDateTime.now());

		System.out.println("Transaction to write: " + trx.toString());
	}

	private void genTrx2(GnuCashWritableFileImpl gcshFile) {
		System.err.println("Account 1 name (from): '" + gcshFile.getAccountByID(fromAcct2ID).getQualifiedName() + "'");
		System.err.println("Account 2 name (to):   '" + gcshFile.getAccountByID(toAcct21ID).getQualifiedName() + "'");
		System.err.println("Account 3 name (to):   '" + gcshFile.getAccountByID(toAcct22ID).getQualifiedName() + "'");

		// ---

		GnuCashWritableTransaction trx = gcshFile.createWritableTransaction();
		trx.setDescription(descr2);

		// ---

		GnuCashWritableTransactionSplit splt1 = trx.createWritableSplit(gcshFile.getAccountByID(fromAcct2ID));
		splt1.setValue(amt21.negate());
		splt1.setQuantity(qty21.negate());
		splt1.setDescription("Abrechnung");
		System.out.println("Split 1 to write: " + splt1.toString());

		// ---

		GnuCashWritableTransactionSplit splt2 = trx.createWritableSplit(gcshFile.getAccountByID(toAcct21ID));
		splt2.setValue(amt22);
		splt2.setQuantity(qty22);
		splt2.setAction(act2);
		splt2.setDescription("Kauf SAP");
		System.out.println("Split 2 to write: " + splt2.toString());

		// ---

		GnuCashWritableTransactionSplit splt3 = trx.createWritableSplit(gcshFile.getAccountByID(toAcct22ID));
		splt3.setValue(amt23);
		splt3.setQuantity(qty23);
		splt3.setDescription("Bankgebühren");
		System.out.println("Split 3 to write: " + splt3.toString());

		// ---

		trx.setDatePosted(datPst2);
		trx.setDateEntered(LocalDateTime.now());

		// ---

		System.out.println("Transaction to write: " + trx.toString());
	}
    
}
