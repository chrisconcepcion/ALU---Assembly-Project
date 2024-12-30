public class InstructionSet {
    // Basic ALU operations
    public static final byte ADD = 0x00;
    public static final byte AND = 0x05;
    public static final byte SHR = 0x07;
    
    // Compare operation
    public static final byte CMP = 0x08;
    
    // Branch instructions
    public static final byte BEQ = 0x09;
    public static final byte BNE = 0x0A;
    public static final byte JMP = 0x0B;
    
    // System control
    public static final byte HALT = 0x0F;
}