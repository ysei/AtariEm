PageOne		    = $0100


NextByte 
            	lda #$11                        ; 7CE9 A9 11 
WalkBit     	sta PageOne,x                    ; 7CEB 9D 00 01 
            	tay                             ; 7CEE A8 
            	eor PageOne,x                    ; 7CEF 5D 00 01 
            	bne BadRam                      ; 7CF2 D0 54 
            	tya                             ; 7CF4 98 
            	asl                             ; 7CF5 0A 
            	bcc WalkBit                     ; 7CF6 90 F3

BadRam	  	    brk