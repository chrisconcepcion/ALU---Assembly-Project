; R0 is accumulator (sum)
; R1-R7 contain values to be added

        .begin
start:  MOV R0, #0      ; Initialize sum to 0
loop:   CMP R1, #0      ; Check if next value is 0
        BEQ done        ; If zero, we're done
        ADD R0, R1      ; Add value to sum
        MOV R1, R2      ; Shift values down
        MOV R2, R3
        MOV R3, R4
        JMP loop        ; Continue checking/adding
done:   HALT
        .end