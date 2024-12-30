public class ControlUnit {
    private ALU alu;
    private byte[] registers;
    private static final int NUM_REGISTERS = 8;
    private boolean halt;
    
    public ControlUnit() {
        alu = new ALU();
        registers = new byte[NUM_REGISTERS];
        halt = false;
    }

    // Add new method for Phase 4 while keeping existing methods
    public byte executeOperation(byte opcode, byte operand1, byte operand2) {
        return alu.executeOperation(opcode, operand1, operand2);
    }
    
    // Rename existing setRegister to loadRegister and keep both for compatibility
    public void loadRegister(int regNum, byte value) {
        setRegister(regNum, value);
    }
    
    
    public void executeBasicOperations() {
        // Basic test operations
        executeInstruction(new Instruction(InstructionSet.ADD, (byte)0, (byte)1));
        executeInstruction(new Instruction(InstructionSet.AND, (byte)0, (byte)1));
        executeInstruction(new Instruction(InstructionSet.SHR, (byte)0, (byte)0));
    }
    
    public void executeProgram(Instruction[] program) {
        int programCounter = 0;
        while (programCounter < program.length && !halt) {
            Instruction inst = program[programCounter];
            byte opcode = inst.getOpcode();
            
            switch(opcode) {
                case InstructionSet.HALT:
                    halt = true;
                    break;
                case InstructionSet.BEQ:
                    if (alu.isZeroFlag()) {
                        programCounter = inst.getOperand1();
                        continue;
                    }
                    break;
                case InstructionSet.BNE:
                    if (!alu.isZeroFlag()) {
                        programCounter = inst.getOperand1();
                        continue;
                    }
                    break;
                case InstructionSet.JMP:
                    programCounter = inst.getOperand1();
                    continue;
                default:
                    executeInstruction(inst);
            }
            programCounter++;
        }
    }
    
    public void executeInstruction(Instruction instruction) {
        byte opcode = instruction.getOpcode();
        byte regDest = instruction.getOperand1();
        byte regSource = instruction.getOperand2();
        
        switch(opcode) {
            case InstructionSet.ADD:
            case InstructionSet.AND:
            case InstructionSet.SHR:
                byte operand1 = registers[regDest];
                byte operand2 = registers[regSource];
                registers[regDest] = alu.executeOperation(opcode, operand1, operand2);
                break;
            case InstructionSet.CMP:
                operand1 = registers[regDest];
                operand2 = registers[regSource];
                alu.executeOperation(opcode, operand1, operand2);
                break;
        }
    }
    
    public void setRegister(int index, byte value) {
        if (index >= 0 && index < NUM_REGISTERS) {
            registers[index] = value;
        }
    }
    
    public byte getRegister(int index) {
        if (index >= 0 && index < NUM_REGISTERS) {
            return registers[index];
        }
        return 0;
    }
}