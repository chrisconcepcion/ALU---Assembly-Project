// Shift until LSB is zero
public class Program2 {
    public static void main(String[] args) {
        ControlUnit cu = new ControlUnit();
        
        // Initialize register with test value
        cu.setRegister(0, (byte)0x85);  // Test value 10000101
        
        // Create program
        Instruction[] program = {
            new Instruction(InstructionSet.AND, (byte)0, (byte)1),  // Check LSB
            new Instruction(InstructionSet.CMP, (byte)0, (byte)0),  // Is result zero?
            new Instruction(InstructionSet.BEQ, (byte)7, (byte)0),  // If zero, jump to end
            new Instruction(InstructionSet.SHR, (byte)0, (byte)0),  // Shift right
            new Instruction(InstructionSet.JMP, (byte)0, (byte)0),  // Jump back to start
            new Instruction(InstructionSet.HALT, (byte)0, (byte)0)  // Stop program
        };
        
        cu.executeProgram(program);
    }
}