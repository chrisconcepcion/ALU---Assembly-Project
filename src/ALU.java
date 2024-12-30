public class ALU {
    // Original operations
    private static final int ADDITION = 0;
    private static final int INCREMENT = 1;
    private static final int DECREMENT = 2;
    private static final int COMPARE = 3;
    private static final int NOT = 4;
    private static final int AND = 5;
    private static final int OR = 6;
    private static final int SHIFT_RIGHT = 7;
    private static final int SHIFT_LEFT = 8;
    
    // Additional operations
    private static final int SUBTRACT = 9;
    private static final int XOR = 10;
    private static final int SHIFT_RIGHT_ARITHMETIC = 11;
    private static final int CLEAR = 12;
    private static final int SET = 13;

    // Status flags
    private boolean zeroFlag;
    private boolean carryFlag;
    private boolean equalFlag;
    private boolean lessThanFlag;
    private boolean greaterThanFlag;

    public byte executeOperation(int opcode, byte operandA, byte operandB) {
        switch (opcode) {
            case ADDITION: return add(operandA, operandB);
            case INCREMENT: return increment(operandA);
            case DECREMENT: return decrement(operandA);
            case COMPARE: compare(operandA, operandB); return 0;
            case NOT: return not(operandA);
            case AND: return and(operandA, operandB);
            case OR: return or(operandA, operandB);
            case SHIFT_RIGHT: return shiftRight(operandA);
            case SHIFT_LEFT: return shiftLeft(operandA);
            case SUBTRACT: return subtract(operandA, operandB);
            case XOR: return xor(operandA, operandB);
            case SHIFT_RIGHT_ARITHMETIC: return shiftRightArithmetic(operandA);
            case CLEAR: return clear();
            case SET: return set();
            default: throw new IllegalArgumentException("Invalid opcode");
        }
    }

    private byte add(byte a, byte b) {
        // Convert to ints to handle full range of addition
        int unsignedA = a & 0xFF;
        int unsignedB = b & 0xFF;
        int result = unsignedA + unsignedB;
        
        // Set carry flag on signed overflow
        carryFlag = ((a >= 0 && b >= 0 && result < 0) || 
        (a < 0 && b < 0 && result >= 0));
        
        // Convert back to byte for final result
        byte finalResult = (byte) result;
        
        // Set zero flag if result is zero
        zeroFlag = (finalResult == 0);
        
        return finalResult;
    }

    private byte increment(byte a) {
        return add(a, (byte) 1);
    }

    private byte decrement(byte a) {
        return add(a, (byte) -1);
    }

    private void compare(byte a, byte b) {
        equalFlag = (a == b);
        lessThanFlag = (a < b);
        greaterThanFlag = (a > b);
    }

    private byte not(byte a) {
        byte result = (byte) ~a;
        zeroFlag = (result == 0);
        return result;
    }

    private byte and(byte a, byte b) {
        byte result = (byte) (a & b);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte or(byte a, byte b) {
        byte result = (byte) (a | b);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte shiftRight(byte a) {
        byte result = (byte) ((a & 0xFF) >>> 1);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte shiftLeft(byte a) {
        byte result = (byte) (a << 1);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte subtract(byte a, byte b) {
        int result = a - b;
        carryFlag = false;
        byte finalResult = (byte) result;
        zeroFlag = (finalResult == 0);
        return finalResult;
    }

    private byte xor(byte a, byte b) {
        byte result = (byte) (a ^ b);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte shiftRightArithmetic(byte a) {
        byte result = (byte) (a >> 1);
        zeroFlag = (result == 0);
        carryFlag = false;
        return result;
    }

    private byte clear() {
        byte result = 0;
        zeroFlag = true;
        carryFlag = false;
        return result;
    }

    private byte set() {
        byte result = (byte) 0xFF;
        zeroFlag = false;
        carryFlag = false;
        return result;
    }

    public boolean isZeroFlag() { return zeroFlag; }
    public boolean isCarryFlag() { return carryFlag; }
    public boolean isEqualFlag() { return equalFlag; }
    public boolean isLessThanFlag() { return lessThanFlag; }
    public boolean isGreaterThanFlag() { return greaterThanFlag; }
}