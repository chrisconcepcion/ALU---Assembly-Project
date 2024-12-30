public class Instruction {
    private byte opcode;
    private byte operand1;
    private byte operand2;
    
    public Instruction(byte opcode, byte operand1, byte operand2) {
        this.opcode = opcode;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }
    
    public byte getOpcode() { return opcode; }
    public byte getOperand1() { return operand1; }
    public byte getOperand2() { return operand2; }
}