package org.example.gnucashapi.write.gen;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDate;

import org.apache.commons.numbers.fraction.BigFraction;
import org.gnucash.api.read.aux.GCshBudgetRecurrence;
import org.gnucash.api.write.GnuCashWritableBudget;
import org.gnucash.api.write.aux.GCshWritableBudgetAccount;
import org.gnucash.api.write.aux.GCshWritableBudgetRecurrence;
import org.gnucash.api.write.impl.GnuCashWritableFileImpl;
import org.gnucash.base.basetypes.simple.GCshAcctID;

public class GenBdgt
{
    // BEGIN Example data -- adapt to your needs
    private static String gcshInFileName  = "example_in.gnucash";
    private static String gcshOutFileName = "example_out.gnucash";
    
    private static String     name    = "Budget 2026";
    
    private static GCshAcctID acct1ID = new GCshAcctID("abc");
    private static GCshAcctID acct2ID = new GCshAcctID("def");
    private static GCshAcctID acct3ID = new GCshAcctID("ghi");
    // END Example data

    // -----------------------------------------------------------------

    public static void main(String[] args) {
    	try {
    		GenAcct tool = new GenAcct();
    		tool.kernel();
    	} catch (Exception exc) {
    		System.err.println("Execution exception. Aborting.");
    		exc.printStackTrace();
    		System.exit(1);
    	}
    }

    protected void kernel() throws Exception
    {
    	GnuCashWritableFileImpl gcshFile = new GnuCashWritableFileImpl(new File(gcshInFileName));

    	GnuCashWritableBudget bdgt = gcshFile.createWritableBudget(name);
    	bdgt.setNofPeriods(12);
    	bdgt.setDescription("My big fat budget");
    	
    	GCshWritableBudgetRecurrence bdgtRecurr = bdgt.getWritableRecurrence();
    	bdgtRecurr.setMult(1);
    	bdgtRecurr.setPeriodType(GCshBudgetRecurrence.PeriodType.MONTH);
    	bdgtRecurr.setStart(LocalDate.of(2026, 1, 1));
    	bdgt.setRecurrence(bdgtRecurr);
    	
    	GCshWritableBudgetAccount bdgtAcct1 = bdgt.createWritableAccount(acct1ID);
    	// Entries for mar, jul 
    	// Note: Index 0 does not automatically mean "january", etc..
    	// Instead, its meaning is dependent on how you set the period type
    	// in the recurrence object above.
    	bdgtAcct1.createWritablePeriod(new BigInteger("2"), BigFraction.of(1010));
    	bdgtAcct1.createWritablePeriod(new BigInteger("6"), BigFraction.of(1020));
    	
    	GCshWritableBudgetAccount bdgtAcct2 = bdgt.createWritableAccount(acct2ID);
    	// Entries for feb, mar, apr 
    	bdgtAcct2.createWritablePeriod(new BigInteger("1"), BigFraction.of(2010));
    	bdgtAcct2.createWritablePeriod(new BigInteger("2"), BigFraction.of(2020));
    	bdgtAcct2.createWritablePeriod(new BigInteger("3"), BigFraction.of(2030));
    	
    	GCshWritableBudgetAccount bdgtAcct3 = bdgt.createWritableAccount(acct3ID);
    	// Entries for mar, apr, may 
    	bdgtAcct3.createWritablePeriod(new BigInteger("2"), BigFraction.of(3010));
    	bdgtAcct3.createWritablePeriod(new BigInteger("3"), BigFraction.of(3020));
    	bdgtAcct3.createWritablePeriod(new BigInteger("4"), BigFraction.of(3030));
    	
    	System.out.println("Budget to write: " + bdgt.toString());
    	gcshFile.writeFile(new File(gcshOutFileName));
    	System.out.println("OK");
    }
  
}
