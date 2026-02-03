package org.example.gnucashapispec.write.gen;

import java.io.File;

import org.gnucash.apispec.write.GnuCashWritableSecurity;
import org.gnucash.apispec.write.impl.GnuCashWritableFileExtImpl;
import org.gnucash.base.basetypes.complex.GCshCmdtyNameSpace;
import org.gnucash.base.basetypes.complex.GCshSecID_Exchange;
import org.gnucash.base.basetypes.complex.GCshSecID_SecIdType;

// Cf. org.example.gnucashapi.write.gen.GenCmdty
public class GenSec {
    // BEGIN Example data -- adapt to your needs
    private static String gcshInFileName  = "example_in.gnucash";
    private static String gcshOutFileName = "example_out.gnucash";
    
    private static String sec1_name       = "The Walt Disney Co.";
    private static GCshCmdtyNameSpace.Exchange sec1_exchange = GCshCmdtyNameSpace.Exchange.NYSE;
    private static String sec1_ticker     = "DIS";
    private static String sec1_isin       = "US2546871060";
    
    private static String sec2_name       = "Unilever Plc";
    private static GCshCmdtyNameSpace.SecIdType sec2_secIdType = GCshCmdtyNameSpace.SecIdType.ISIN;
    private static String sec2_isin       = "GB00B10RZP78";
    // END Example data

    // -----------------------------------------------------------------

    public static void main(String[] args) {
		try {
			GenSec tool = new GenSec();
			tool.kernel();
		} catch (Exception exc) {
			System.err.println("Execution exception. Aborting.");
			exc.printStackTrace();
			System.exit(1);
		}
    }

    protected void kernel() throws Exception {
		GnuCashWritableFileExtImpl gcshFile = new GnuCashWritableFileExtImpl(new File(gcshInFileName));

		GCshSecID_Exchange qualifID1 = new GCshSecID_Exchange(sec1_exchange, sec1_ticker);
		GnuCashWritableSecurity sec1 = gcshFile.createWritableSecurity(qualifID1, sec1_isin, sec1_name);
		sec1.setXCode(sec1_isin);
		System.out.println("Security no. 1 to write: " + sec1.toString());

		GCshSecID_SecIdType qualifID2 = new GCshSecID_SecIdType(sec2_secIdType, sec2_isin);
		GnuCashWritableSecurity sec2 = gcshFile.createWritableSecurity(qualifID2, sec2_isin, sec2_name);
		sec2.setXCode(sec2_isin);
		System.out.println("Security no. 2 to write: " + sec2.toString());

		gcshFile.writeFile(new File(gcshOutFileName));
		System.out.println("OK");
    }
}
