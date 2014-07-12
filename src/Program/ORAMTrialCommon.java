// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package Program;

import java.math.*;

import Utils.*;
import YaoGC.*;

class ORAMTrialCommon extends ProgCommon {
    static int sBitLen;
    static int cBitLen;

    static int bitLength(int x) {
    	return BigInteger.valueOf(x).bitLength();
    }

    protected static void initCircuits() {
	ccs = new Circuit[1];
	ccs[0] = new Concatenate(6);
	//ccs[0] = new FindFirstZeroOrOne_Wplus1_Wplus1(sBitLen+cBitLen-1, true);
    }

    public static State execCircuit(BigInteger[] slbs, BigInteger[] clbs) throws Exception {
	BigInteger[] lbs = new BigInteger[sBitLen+cBitLen];
	System.arraycopy(clbs, 0, lbs, 0, cBitLen);
	System.arraycopy(slbs, 0, lbs, cBitLen, sBitLen);
	State in = State.fromLabels(lbs);

	State out = ccs[0].startExecuting(in);
	
	StopWatch.taskTimeStamp("circuit garbling");

	return out;
    }
}