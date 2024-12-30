// Adding until zero
public class Program1 {
    public static void main(String[] args) {
        ControlUnit cu = new ControlUnit();
        
        // Initialize registers
        cu.setRegister(0, (byte)0);  // Accumulator
        cu.setRegister(1, (byte)5);  // First value to add
        
        // Create program
        Instruction[] program = {
            new Instruction(InstructionSet.ADD, (byte)0, (byte)1),  // Add R1 to R0
            new Instruction(InstructionSet.CMP, (byte)1, (byte)0),  // Compare R1 with 0
            new Instruction(InstructionSet.BNE, (byte)0, (byte)0),  // Branch if not zero
            new Instruction(InstructionSet.HALT, (byte)0, (byte)0)  // Stop program
        };
        
        cu.executeProgram(program);
    }
}