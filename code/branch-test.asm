;0000000 ad 00 30 c9 2a f0 03 4c 00 60 ea ad 00 30 c9 2a
;0000020 d0 f9


*		= $6000

; forward branch test
loop	lda	$3000		; ad 00 30
		cmp	#42			; c9 2a
		beq foundit		; f0 03
		jmp loop		; 4c 00 60
foundit	nop		
		
		
; backwards branch test
loop2	lda $3000		; ad 00 30
		cmp #42			; c9 2a
		bne loop2		; d0 f9
