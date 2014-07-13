// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package YaoGC;

public class FindFirstZeroOrOne_Wplus1_Wplus1 extends CompositeCircuit {
    private final int w;
    private final boolean b; // find 1 or find 0

    public FindFirstZeroOrOne_Wplus1_Wplus1(int w, boolean b) {
	super(w+1, w+1, w*3, "FindFirstZeroOrOne_" + (w+1) + "_" + w+1);

	this.w = w;
	this.b = b;
    }

    protected void createSubCircuits() throws Exception {
	int i = 0;
	if (b) {
	    for (; i < w*2; i++)
		subCircuits[i] = new FindFirstOne_2_2();
	}
	else {
	    for (; i < w*2; i++)
		subCircuits[i] = new FindFirstZero_2_2();
	}
	for (; i < w*3; i++)
	    subCircuits[i] = new XOR_2_1();

	super.createSubCircuits();
    }

    protected void connectWires() throws Exception {
	inputWires[0].connectTo(subCircuits[0].inputWires, 0); // Enable wire

	for (int i = 0; i < w; i++) {
	    inputWires[i+1].connectTo(subCircuits[i].inputWires, 1); // green wire
	    inputWires[i+1].connectTo(subCircuits[i+w].inputWires, 1); // green wire
	    subCircuits[i].outputWires[1].connectTo(subCircuits[i+2*w].inputWires, 0); // orange wire
	    subCircuits[i+w].outputWires[1].connectTo(subCircuits[i+2*w].inputWires, 1); // orange wire
	}

	for (int i = 0; i < 2*w-1; i++)
	    subCircuits[i].outputWires[0].connectTo(subCircuits[i+1].inputWires, 0); // blue wire
    }

    protected void defineOutputWires() {
	for (int i = 0; i < w; i++)
	    outputWires[i] = subCircuits[i+2*w].outputWires[0]; // orange wire

	outputWires[w] = subCircuits[2*w-1].outputWires[0]; // Success wire
    }
}