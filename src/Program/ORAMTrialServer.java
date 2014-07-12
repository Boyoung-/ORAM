// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package Program;

import java.math.*;
import java.util.*;
import java.security.SecureRandom;

import YaoGC.*;
import Utils.*;

public class ORAMTrialServer extends ProgServer {
    private BigInteger sBits;

    private State outputState;

    private BigInteger[][] sBitslps, cBitslps;

    private static final SecureRandom rnd = new SecureRandom();

    public ORAMTrialServer(BigInteger bv, int length) {
	sBits = bv;
	ORAMTrialCommon.bitVecLen = length;
    }

    protected void init() throws Exception {
	ORAMTrialCommon.oos.writeInt(ORAMTrialCommon.bitVecLen);
	ORAMTrialCommon.oos.flush();

	ORAMTrialCommon.initCircuits();

	generateLabelPairs();

	super.init();
    }

    private void generateLabelPairs() {
	sBitslps = new BigInteger[ORAMTrialCommon.bitVecLen][2];
	cBitslps = new BigInteger[ORAMTrialCommon.bitVecLen][2];

	for (int i = 0; i < ORAMTrialCommon.bitVecLen; i++) {
	    BigInteger glb0 = new BigInteger(Wire.labelBitLength, rnd);
	    BigInteger glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
	    sBitslps[i][0] = glb0;
	    sBitslps[i][1] = glb1;

	    glb0 = new BigInteger(Wire.labelBitLength, rnd);
	    glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
	    cBitslps[i][0] = glb0;
	    cBitslps[i][1] = glb1;
	}
    }

    protected void execTransfer() throws Exception {
	for (int i = 0; i < ORAMTrialCommon.bitVecLen; i++) {
	    int idx = sBits.testBit(i) ? 1 : 0;

	    int bytelength = (Wire.labelBitLength-1)/8 + 1;
	    Utils.writeBigInteger(sBitslps[i][idx], bytelength, ORAMTrialCommon.oos);
	}
	ORAMTrialCommon.oos.flush();
	StopWatch.taskTimeStamp("sending labels for selfs inputs");

	snder.execProtocol(cBitslps);
	StopWatch.taskTimeStamp("sending labels for peers inputs");
    }

    protected void execCircuit() throws Exception {
	BigInteger[] sBitslbs = new BigInteger[ORAMTrialCommon.bitVecLen];
	BigInteger[] cBitslbs = new BigInteger[ORAMTrialCommon.bitVecLen];

	for (int i = 0; i < sBitslps.length; i++)
	    sBitslbs[i] = sBitslps[i][0];

	for (int i = 0; i < cBitslps.length; i++)
	    cBitslbs[i] = cBitslps[i][0];

	outputState = ORAMTrialCommon.execCircuit(sBitslbs, cBitslbs);
    }

    protected void interpretResult() throws Exception {
	BigInteger[] outLabels = (BigInteger[]) ORAMTrialCommon.ois.readObject();

	BigInteger output = BigInteger.ZERO;
	for (int i = 0; i < outLabels.length; i++) {
	    if (outputState.wires[i].value != Wire.UNKNOWN_SIG) {
		if (outputState.wires[i].value == 1)
		    output = output.setBit(i);
		continue;
	    }
	    else if (outLabels[i].equals(outputState.wires[i].invd ? 
					 outputState.wires[i].lbl :
					 outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)))) {
		    output = output.setBit(i);
	    }
	    else if (!outLabels[i].equals(outputState.wires[i].invd ? 
					  outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) :
					  outputState.wires[i].lbl)) 
		throw new Exception("Bad label encountered: i = " + i + "\t" +
				    outLabels[i] + " != (" + 
				    outputState.wires[i].lbl + ", " +
				    outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) + ")");
	}
	
	System.out.println("output (pp): " + output.toString());
	StopWatch.taskTimeStamp("output labels received and interpreted");
    }

    protected void verify_result() throws Exception {
	BigInteger cBits = (BigInteger) ORAMTrialCommon.ois.readObject();
	
	//BigInteger res = sBits.xor(cBits);

	System.out.println("output (verify): " +
			   sBits.intValue() + " " + 
			   cBits.intValue());
	//res.bitCount());
    }
}