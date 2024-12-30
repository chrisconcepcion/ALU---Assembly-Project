public class CPUCompleteTest {
    private static ALU alu;
    private static ControlUnit controlUnit;
    public static void main(String[] args) {
        alu = new ALU();
        controlUnit = new ControlUnit();

        testPhase1();
        testPhase2();
        testPhase3();
        testPhase4();
    }

    private static void testPhase1() {
        System.out.println("=== Phase 1: ALU Testing ===\n");
        ALU alu = new ALU();
    
        // Match exact test cases from expected output
        testOperation(alu, "Addition", 0, (byte)5, (byte)3);                  // 0x05 + 0x03 = 0x08
        testOperation(alu, "Addition Overflow", 0, (byte)127, (byte)1);       // 0x7F + 0x01 = 0x80
        testOperation(alu, "AND", 5, (byte)0xF0, (byte)0x0F);                // 0xF0 & 0x0F = 0x00
        testOperation(alu, "Right Shift", 7, (byte)0x80, (byte)0x00);        // 0x80 >> 1 = 0x40
        testCompare(alu, (byte)5, (byte)3);
        
        System.out.println();
    }
    
    private static void testOperation(ALU alu, String opName, int opcode, byte a, byte b) {
        System.out.println("Testing " + opName + ":");
        System.out.printf("Input: a = 0x%02X, b = 0x%02X\n", a & 0xFF, b & 0xFF);
        
        byte result = alu.executeOperation(opcode, a, b);
        
        System.out.printf("Result: 0x%02X\n", result & 0xFF);
        System.out.println("Carry Flag: " + alu.isCarryFlag());
        System.out.println("Zero Flag: " + alu.isZeroFlag());
        System.out.println();
    }
    
    private static void testCompare(ALU alu, byte a, byte b) {
        System.out.println("Testing Compare:");
        System.out.println("Comparing " + a + " vs " + b);
        alu.executeOperation(3, a, b);  // 3 is COMPARE opcode
        System.out.println("Equal: " + alu.isEqualFlag());
        System.out.println("Less Than: " + alu.isLessThanFlag());
        System.out.println("Greater Than: " + alu.isGreaterThanFlag());
        System.out.println();
    }

    private static void testPhase2() {
        System.out.println("=== Phase 2: Instruction Set Testing ===\n");
        
        Instruction[] testInstructions = {
            new Instruction(InstructionSet.ADD, (byte)1, (byte)2),
            new Instruction(InstructionSet.AND, (byte)3, (byte)4),
            new Instruction(InstructionSet.SHR, (byte)5, (byte)0)
        };

        for (Instruction inst : testInstructions) {
            System.out.println(String.format(
                "Instruction: opcode=0x%02X, operand1=0x%02X, operand2=0x%02X",
                inst.getOpcode(),
                inst.getOperand1(),
                inst.getOperand2()
            ));
        }
        System.out.println();
    }

    private static void testPhase3() {
        System.out.println("=== Phase 3: Control Unit Testing ===\n");
        ControlUnit cu = new ControlUnit();
    
        // Test Addition
        System.out.println("Testing Addition:");
        cu.setRegister(0, (byte)10);
        cu.setRegister(1, (byte)5);
        System.out.println("Initial values: R0=" + cu.getRegister(0) + ", R1=" + cu.getRegister(1));
        cu.executeInstruction(new Instruction(InstructionSet.ADD, (byte)0, (byte)1));
        System.out.println("After ADD: R0=" + cu.getRegister(0) + "\n");
    
        // Test AND
        System.out.println("Testing AND:");
        cu.setRegister(0, (byte)0xF0);
        cu.setRegister(1, (byte)0x0F);
        System.out.println("Initial values: R0=0x" + String.format("%02X", cu.getRegister(0)) + 
                          ", R1=0x" + String.format("%02X", cu.getRegister(1)));
        cu.executeInstruction(new Instruction(InstructionSet.AND, (byte)0, (byte)1));
        System.out.println("After AND: R0=0x" + String.format("%02X", cu.getRegister(0)) + "\n");
    
        // Test Right Shift
        System.out.println("Testing Right Shift:");
        cu.setRegister(0, (byte)0x80);
        System.out.println("Initial value: R0=0x" + String.format("%02X", cu.getRegister(0)));
        cu.executeInstruction(new Instruction(InstructionSet.SHR, (byte)0, (byte)0));
        System.out.println("After SHR: R0=0x" + String.format("%02X", cu.getRegister(0)) + "\n");
    }

    private static void testAssemblyPrograms() {
        System.out.println("\n=== Testing Assembly Programs ===\n");
        
        // Test add_until_zero.asm
        System.out.println("Testing add_until_zero.asm:");
        controlUnit = new ControlUnit();
        // Set up test values
        controlUnit.setRegister(0, (byte)0);  // Accumulator
        controlUnit.setRegister(1, (byte)5);  // First value
        controlUnit.setRegister(2, (byte)3);  // Second value
        controlUnit.setRegister(3, (byte)2);  // Third value
        controlUnit.setRegister(4, (byte)0);  // Stop value
        
        testAddUntilZero();
        
        // Test shift_until_zero.asm
        System.out.println("\nTesting shift_until_zero.asm:");
        controlUnit = new ControlUnit();
        byte testValue = (byte)0x85;  // 10000101 in binary
        controlUnit.setRegister(0, testValue);
        
        testShiftUntilZero();
    }

    private static void testAddUntilZero() {
        controlUnit = new ControlUnit();
        
        // Initial setup
        System.out.println("Initial accumulator (R0): 0");
        System.out.println("Values to add: 5, 3, 2, 0");
        
        controlUnit.loadRegister(0, (byte)0);  // Accumulator
        controlUnit.loadRegister(1, (byte)5);
        controlUnit.loadRegister(2, (byte)3);
        controlUnit.loadRegister(3, (byte)2);
        
        // Execute program steps
        byte result = controlUnit.getRegister(0);
        System.out.println("After adding R1(5): R0 = " + result);
        
        result = controlUnit.executeOperation((byte)0, result, controlUnit.getRegister(2));
        System.out.println("After adding R2(3): R0 = " + result);
        
        result = controlUnit.executeOperation((byte)0, result, controlUnit.getRegister(3));
        System.out.println("After adding R3(2): R0 = " + result);
    }

    private static void testShiftUntilZero() {
        System.out.println("\nTesting Shift Until LSB Zero:");
        byte value = (byte)0x85;  // Initial value 10000101
        
        System.out.println("Initial value: 0x" + String.format("%02X", value));
        controlUnit.setRegister(0, value);
        
        // Keep track of the current value
        byte currentValue = value;
        
        // Add a counter to prevent infinite loops
        int maxIterations = 8;  // maximum number of shifts possible for a byte
        int iterations = 0;
        
        while ((currentValue & 0x01) != 0 && 
               currentValue != (byte)0xFF && 
               iterations < maxIterations) {
            
            currentValue = controlUnit.executeOperation((byte)7, currentValue, (byte)0);
            System.out.println("After shift: 0x" + String.format("%02X", currentValue));
            iterations++;
        }
        
        if (currentValue == (byte)0xFF) {
            System.out.println("Stopped: All ones detected");
        } else if ((currentValue & 0x01) == 0) {
            System.out.println("Stopped: LSB is zero");
        } else {
            System.out.println("Stopped: Maximum iterations reached");
        }
        
        System.out.println("Final value: 0x" + String.format("%02X", currentValue));
    }

    private static void testAddUntilZeroEdgeCases() {
        System.out.println("Program 1 Edge Cases:\n");
        // Test all zeros
        testAddUntilZeroCase("All zeros", (byte)0, (byte)0, (byte)0);
        
        // Test overflow
        testAddUntilZeroCase("Overflow scenario", (byte)127, (byte)1, (byte)1);
        
        // Test negative numbers
        testAddUntilZeroCase("Negative numbers", (byte)-5, (byte)-3, (byte)0);
    }

    private static void testShiftUntilZeroEdgeCases() {
        System.out.println("\nProgram 2 Edge Cases:\n");
        // Test already zero LSB
        testShiftCase("Already zero LSB (0x84)", (byte)0x84);
        
        // Test all ones
        testShiftCase("All ones (0xFF)", (byte)0xFF);
        
        // Test alternating bits
        testShiftCase("Alternating bits (0x55)", (byte)0x55);
    }
    
    private static void executeAddUntilZero(ControlUnit cu) {
        System.out.println("Initial accumulator (R0): " + cu.getRegister(0));
        
        for (int i = 1; i < 8; i++) {
            byte value = cu.getRegister(i);
            if (value == 0) {
                System.out.println("Found zero value in R" + i + ", stopping");
                break;
            }
            
            System.out.println("Before adding R" + i + "(" + value + "): R0 = " + cu.getRegister(0));
            cu.executeInstruction(new Instruction(InstructionSet.ADD, (byte)0, (byte)i));
            System.out.println("After adding: R0 = " + cu.getRegister(0));
        }
    }

    private static void testPhase4() {
        System.out.println("=== Phase 4: Assembly Programs Testing ===\n");
        
        // Test Program 1 - Add until zero
        testAddUntilZero();
        
        // Test Program 2 - Shift until LSB is zero
        testShiftUntilZero();
        
        // Edge cases
        testAddUntilZeroEdgeCases();
        testShiftUntilZeroEdgeCases();
    }

    private static void testShiftCase(String testName, byte initialValue) {
        System.out.println("Testing: " + testName);
        controlUnit = new ControlUnit();
        
        System.out.println("Initial value: 0x" + String.format("%02X", initialValue));
        controlUnit.loadRegister(0, initialValue);
        
        byte value = initialValue;
        while ((value & 0x01) != 0 && value != (byte)0xFF) {
            value = controlUnit.executeOperation((byte)7, value, (byte)0);
            System.out.println("After shift: 0x" + String.format("%02X", value));
        }
        
        if (value == (byte)0xFF) {
            System.out.println("Stopped: All ones detected");
        } else {
            System.out.println("Stopped: LSB is zero");
        }
        System.out.println("Final value: 0x" + String.format("%02X", value) + "\n");
    }

    private static void testAddUntilZeroCase(String testName, byte val1, byte val2, byte val3) {
        System.out.println("Testing: " + testName);
        
        controlUnit.loadRegister(0, (byte)0);  // Clear accumulator
        controlUnit.loadRegister(1, val1);
        controlUnit.loadRegister(2, val2);
        controlUnit.loadRegister(3, val3);
        
        byte result = controlUnit.getRegister(0);
        System.out.println("Initial sum: " + result);
        
        // Add values until zero is encountered
        if (val1 != 0) {
            result = controlUnit.executeOperation((byte)0, result, val1);
            System.out.println("After adding " + val1 + ": " + result);
        }
        if (val2 != 0) {
            result = controlUnit.executeOperation((byte)0, result, val2);
            System.out.println("After adding " + val2 + ": " + result);
        }
        if (val3 != 0) {
            result = controlUnit.executeOperation((byte)0, result, val3);
            System.out.println("After adding " + val3 + ": " + result);
        }
        
        System.out.println("Final sum: " + result + "\n");
    }
}