; R0 contains value to be shifted
; R1 used for LSB testing and all-ones check

        .begin
start:  MOV R1, #0xFF   ; For all-ones check
        CMP R0, R1      ; Check if all ones
        BEQ done        ; If all ones, stop
loop:   AND R1, R0, #1  ; Test LSB
        BEQ done        ; If LSB=0, done
        SHR R0          ; Shift right
        JMP loop        ; Continue shifting
done:   HALT
        .end