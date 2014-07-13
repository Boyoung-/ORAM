// by Boyang

package YaoGC;

public class F2ET_Wplus2_Wplus2 extends CompositeCircuit {
    private final int w;

    public F2ET_Wplus2_Wplus2(int w) {
	super(w+2, w+2, 2*w+2, "F2ET_" + (w+2) + "_" + w+2);

	this.w = w;
    }

    protected void createSubCircuits() throws Exception {
	for (int i = 0; i < 2*w; i++)
	    subCircuits[i] = new XOR_2_1();
	subCircuits[2*w]   = new FindFirstZeroOrOne_Wplus1_Wplus1(w, false);
	subCircuits[2*w+1] = new FindFirstZeroOrOne_Wplus1_Wplus1(w, false);

	super.createSubCircuits();
    }

    protected void connectWires() throws Exception {
	// Enable wires
	inputWires[0].connectTo(subCircuits[2*w].inputWires, 0); 
	inputWires[1].connectTo(subCircuits[2*w+1].inputWires, 0);

	for (int i = 0; i < w; i++) {
	    inputWires[i+2].connectTo(subCircuits[2*w].inputWires, i+1); 
	    inputWires[i+2].connectTo(subCircuits[i].inputWires, 1);
	    subCircuits[2*w].outputWires[i].connectTo(subCircuits[i].inputWires, 0);
	    subCircuits[2*w].outputWires[i].connectTo(subCircuits[i+w].inputWires, 1);
	    subCircuits[i].outputWires[0].connectTo(subCircuits[2*w+1].inputWires, i+1);
	    subCircuits[2*w+1].outputWires[i].connectTo(subCircuits[i+w].inputWires, 0);
	}
    }

    protected void defineOutputWires() {
	outputWires[0] = subCircuits[2*w].outputWires[w];
	outputWires[1] = subCircuits[2*w+1].outputWires[w];

	for (int i = 0; i < w; i++)
	    outputWires[i+2] = subCircuits[i+w].outputWires[0];
    }
}