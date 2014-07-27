// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package Program;

import java.math.*;

import Utils.*;
import YaoGC.*;

public class ORAMTrialCommon extends ProgCommon {
    static int sBitLen;
    static int cBitLen;

    public static String circuit = null;

    static int bitLength(int x) {
    	return BigInteger.valueOf(x).bitLength();
    }

    protected static void initCircuits() {
	int s1, s2;
	s1 = 1;
	s2 = 5;
	System.out.println("---- Sigma: " + s1 + "  " + s2);

	ccs = new Circuit[1];
	if (circuit.equals("F2ET"))
	    ccs[0] = new F2ET_Wplus2_Wplus2(sBitLen+cBitLen-2, s1, s2); // for testing F2ET
	else
	    ccs[0] = new F2FT_2Wplus2_Wplus2((sBitLen+cBitLen-2)/2, s1, s2); // for testing F2FT
    }

    public static State execCircuit(BigInteger[] slbs, BigInteger[] clbs) throws Exception {
	BigInteger[] lbs = new BigInteger[sBitLen+cBitLen];
	System.arraycopy(slbs, 0, lbs, 0, sBitLen);
	System.arraycopy(clbs, 0, lbs, sBitLen, cBitLen);
	State in = State.fromLabels(lbs);

	State out = ccs[0].startExecuting(in);
	
	StopWatch.taskTimeStamp("circuit garbling");

	return out;
    }
}