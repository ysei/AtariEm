ScratchRam      = $0000

PageOne		= $0100
PageThree	= $0300

V0319		= $0319

L01A4A			= $1a4a
L01C16			= $1c16
L01C1A			= $1c1a
L0201F			= $201f
L02BFF			= $2bff
L02C56			= $2c56
L03440			= $3440
L03873			= $3873
L056F7			= $56f7

ThreeKHz        = $2001
VGHalted        = $2002
ShieldSwitch    = $2003
FireSwitch      = $2004
SlamSwitch      = $2006

SelfTestSwitch  = $2007
LCoinSwitch     = $2400
CCoinSwitch     = $2401
RCoinSwitch     = $2402
OnePlayerStartSwitch   = $2403
TwoPlayerStartSwitch   = $2404
ThrustSwitch    = $2405
RotRightSwitch  = $2406
RotLeftSwitch   = $2407

OptionSwitch87  = $2800
OptionSwitch65  = $2801
OptionSwitch43  = $2802
OptionSwitch21  = $2803

; POKEY Write registers
PKY_AUDF1       = $2C00
PKY_AUDC1       = $2C01
PKY_AUDF2       = $2C02
PKY_AUDC2       = $2C03
PKY_AUDF3       = $2C04
PKY_AUDC3       = $2C05
PKY_AUDF4       = $2C06
PKY_AUDC4       = $2C07
PKY_AUDCTL      = $2C08
PKY_STIMER      = $2C09
PKY_SKRES       = $2C0A
PKY_POTGO       = $2C0B
PKY_SEROUT      = $2C0D
PKY_IRQEN       = $2C0E
PKY_SKCTL       = $2C0F

; POKEY Read registers
PKY_POT0        = $2C00
PKY_POT1        = $2C01
PKY_POT2        = $2C02
PKY_POT3        = $2C03
PKY_POT4        = $2C04
PKY_POT5        = $2C05
PKY_POT6        = $2C06
PKY_POT7        = $2C07
PKY_ALLPOT      = $2C08
PKY_KBCODE      = $2C09
PKY_RANDOM      = $2C0A
PKY_SERIN       = $2C0D
PKY_IRQST       = $2C0E
PKY_SKSTAT      = $2C0F

EAROM           = $2C40
StartVG         = $3000
LatchEA         = $3200
ResetWDog       = $3400
ExplosionDat    = $3600
ResetVG         = $3800
EACtlLatch      = $3A00
OnePlayerLED           = $3C00
TwoPlayerLED           = $3C01
ThrustSnd       = $3C03
BankSel         = $3C04
LCoinCounter    = $3C05
CCoinCounter    = $3C06
RCoinCounter    = $3C07
NoiseGenReset   = $3E00

VectorRam       = $4000
VectorROM       = $4800

VROM506A		= $506a
VROM506B		= $506b
VROM53BC		= $53bc
VROM56F6		= $56f6
VROM56F8		= $56f8
VROM56F9		= $56f9


; Vector generator commands
VGOP_VCTR_0 	= 00
VGOP_VCTR_1 	= 10
VGOP_VCTR_2 	= 20
VGOP_VCTR_3 	= 30
VGOP_VCTR_4 	= 40
VGOP_VCTR_5 	= 50
VGOP_VCTR_6 	= 60
VGOP_VCTR_7 	= 70
VGOP_VCTR_8 	= 80
VGOP_VCTR_9 	= 90
VGOP_LABS 		= $a0
VGOP_HALT 		= $b0
VGOP_JSRL 		= $c0
VGOP_RTSL 		= $d0
VGOP_JMPL 		= $e0
VGOP_SVEC 		= $f0


RomBase         = $6000

*		= RomBase

; Main game - jump here after basic initialisation and selftest switch check

            jsr Sub_7BE3         ; 6000 20 E3 7B 
            lda $00CD          ; 6003 A5 CD 
            bne L06011         ; 6005 D0 0A 
            sta $00CC          ; 6007 85 CC 
            sta $00CB          ; 6009 85 CB 
            sta $00CE          ; 600B 85 CE 
            lda #$20           ; 600D A9 20 
            sta $00CD          ; 600F 85 CD 
L06011      jsr L06959         ; 6011 20 59 69 
L06014      bit VGHalted       ; 6014 2C 02 20 
            bmi L06014         ; 6017 30 FB 
            jsr $4BA2          ; 6019 20 A2 4B 
L0601C      lsr $0075          ; 601C 46 75 
            bcc L0601C         ; 601E 90 FC 
            lda $0076          ; 6020 A5 76 
            and #$01           ; 6022 29 01 
            asl                ; 6024 0A 
            tax                ; 6025 AA 
            eor #$02           ; 6026 49 02 
            tay                ; 6028 A8 
            lda L07799,x       ; 6029 BD 99 77 
            sta VectorRam      ; 602C 8D 00 40 
            lda L0779A,x       ; 602F BD 9A 77 
            sta $4001         ; 6032 8D 01 40 
            lda L07799,y       ; 6035 B9 99 77 
            asl                ; 6038 0A 
            sta $0003         ; 6039 85 03 
            lda L0779A,y       ; 603B B9 9A 77 
            rol                ; 603E 2A 
            and #$1F           ; 603F 29 1F 
            ora #$40           ; 6041 09 40 
            sta $0004         ; 6043 85 04 
            sta StartVG        ; 6045 8D 00 30 
            sta ResetWDog      ; 6048 8D 00 34 
            inc $0076         ; 604B E6 76 
            bne L0607F         ; 604D D0 30 
            inc $0077         ; 604F E6 77 
            ldx $0020         ; 6051 A6 20 
            lda $0064,x       ; 6053 B5 64 
            and #$0F           ; 6055 29 0F 
            sta $0010         ; 6057 85 10 
            eor $0064,x       ; 6059 55 64 
            lsr                ; 605B 4A 
            sta $0011         ; 605C 85 11 
            lsr                ; 605E 4A 
            lsr                ; 605F 4A 
            adc $0011         ; 6060 65 11 
            adc $0010         ; 6062 65 10 
            ldx $001F         ; 6064 A6 1F 
            ldy $0077         ; 6066 A4 77 
            cpy #$80           ; 6068 C0 80 
            bne L06074         ; 606A D0 08 
            cmp $00FA,x       ; 606C D5 FA 
            bne L06072         ; 606E D0 02 
            inc $00FB,x       ; 6070 F6 FB 
L06072      sta $00FA,x       ; 6072 95 FA 
L06074      clc                ; 6074 18 
            adc $00FB,x       ; 6075 75 FB 
            cmp #$10           ; 6077 C9 10 
            bcc L0607D         ; 6079 90 02 
            lda #$0F           ; 607B A9 0F 
L0607D      sta $00D3         ; 607D 85 D3 
L0607F      jsr L07AFF         ; 607F 20 FF 7A 
            jsr $4BE1         ; 6082 20 E1 4B 
            bcs L060C9         ; 6085 B0 42 
            jsr L06FD4         ; 6087 20 D4 6F 
            jsr L0656B         ; 608A 20 6B 65 
            bpl L060AD         ; 608D 10 1E 
            jsr L06C9D         ; 608F 20 9D 6C 
            bcs L060AD         ; 6092 B0 19 
            lda $0074         ; 6094 A5 74 
            bne L060A7         ; 6096 D0 0F 
            jsr L06659         ; 6098 20 59 66 
            jsr L06497         ; 609B 20 97 64 
            jsr L06810         ; 609E 20 10 68 
            jsr $4A9E          ; 60A1 20 9E 4A 
            jsr L06322         ; 60A4 20 22 63 
L060A7      jsr L066DC         ; 60A7 20 DC 66 
            jsr L060F7         ; 60AA 20 F7 60 
L060AD      jsr L06A7E         ; 60AD 20 7E 6A 
            jsr L06EF6         ; 60B0 20 F6 6E 
            lda #$7F           ; 60B3 A9 7F 
            tax                ; 60B5 AA 
            jsr L07A1F         ; 60B6 20 1F 7A 
            jsr L079DA         ; 60B9 20 DA 79 
            lda $02EC         ; 60BC AD EC 02 
            beq L060C4         ; 60BF F0 03 
            dec $02EC         ; 60C1 CE EC 02 
L060C4      ora $02E7         ; 60C4 0D E7 02 
            bne L060CC         ; 60C7 D0 03 
L060C9      jmp L06011         ; 60C9 4C 11 60 
L060CC      jmp L06014         ; 60CC 4C 14 60 
            .byt $B2	; 60CF B2 
            lda $0042         ; 60D0 A5 42 
            and $0043         ; 60D2 25 43 
            bmi L060D9         ; 60D4 30 03 
            jmp L07BC7         ; 60D6 4C C7 7B 
L060D9      clc                ; 60D9 18 
            rts                ; 60DA 60 
            lda $008A         ; 60DB A5 8A 
            ora $0013         ; 60DD 05 13 
            bpl L060E8         ; 60DF 10 07 
            lda $0076         ; 60E1 A5 76 
            and #$20           ; 60E3 29 20 
            beq L060E8         ; 60E5 F0 01 
            rts                ; 60E7 60 
L060E8      jmp L07159         ; 60E8 4C 59 71 
            ldy #$01           ; 60EB A0 01 
            jsr L07159         ; 60ED 20 59 71 
            ldy $001E         ; 60F0 A4 1E 
            iny                ; 60F2 C8 
            tya                ; 60F3 98 
            jmp L079EB         ; 60F4 4C EB 79 
L060F7      lsr $0071         ; 60F7 46 71 
            ldx #$07           ; 60F9 A2 07 
L060FB      lda $0219,x       ; 60FB BD 19 02 
            beq L06102         ; 60FE F0 02 
            bpl L0610C         ; 6100 10 0A 
L06102      dex                ; 6102 CA 
            bpl L060FB         ; 6103 10 F6 
            bit $0071         ; 6105 24 71 
            bmi L0610B         ; 6107 30 02 
            stx $0072         ; 6109 86 72 
L0610B      rts                ; 610B 60 
L0610C      ldy #$1A           ; 610C A0 1A 
            cpx #$04           ; 610E E0 04 
            bcs L06119         ; 6110 B0 07 
            dey                ; 6112 88 
            txa                ; 6113 8A 
            bne L06119         ; 6114 D0 03 
L06116      dey                ; 6116 88 
            bmi L06102         ; 6117 30 E9 
L06119      lda $0200,y       ; 6119 B9 00 02 
            beq L06116         ; 611C F0 F8 
            bmi L06116         ; 611E 30 F6 
            sta $000C         ; 6120 85 0C 
            lda $0263,y       ; 6122 B9 63 02 
            sec                ; 6125 38 
            sbc $027C,x       ; 6126 FD 7C 02 
            sbc #$03           ; 6129 E9 03 
            cmp #$FA           ; 612B C9 FA 
            bcc L06116         ; 612D 90 E7 
            lda $0284,y       ; 612F B9 84 02 
            sbc $029D,x       ; 6132 FD 9D 02 
            sbc #$03           ; 6135 E9 03 
            cmp #$FA           ; 6137 C9 FA 
            bcc L06116         ; 6139 90 DB 
            lda $02A5,y       ; 613B B9 A5 02 
            sec                ; 613E 38 
            sbc $02BE,x       ; 613F FD BE 02 
            sta $0009         ; 6142 85 09 
            lda $0263,y       ; 6144 B9 63 02 
            sbc $027C,x       ; 6147 FD 7C 02 
            lsr                ; 614A 4A 
            ror $0009         ; 614B 66 09 
            asl                ; 614D 0A 
            beq L0615E         ; 614E F0 0E 
            bpl L06116         ; 6150 10 C4 
            eor #$FE           ; 6152 49 FE 
            bne L06116         ; 6154 D0 C0 
            lda $0009         ; 6156 A5 09 
            eor #$FF           ; 6158 49 FF 
            adc #$00           ; 615A 69 00 
            sta $0009         ; 615C 85 09 
L0615E      lda $02C6,y       ; 615E B9 C6 02 
            sec                ; 6161 38 
            sbc $02DF,x       ; 6162 FD DF 02 
            sta $000A         ; 6165 85 0A 
            lda $0284,y       ; 6167 B9 84 02 
            sbc $029D,x       ; 616A FD 9D 02 
            lsr                ; 616D 4A 
            ror $000A         ; 616E 66 0A 
            asl                ; 6170 0A 
            beq L06181         ; 6171 F0 0E 
            bpl L06116         ; 6173 10 A1 
            eor #$FE           ; 6175 49 FE 
            bne L06116         ; 6177 D0 9D 
            lda $000A         ; 6179 A5 0A 
            eor #$FF           ; 617B 49 FF 
            adc #$00           ; 617D 69 00 
            sta $000A         ; 617F 85 0A 
L06181      lda #$04           ; 6181 A9 04 
            cpx #$01           ; 6183 E0 01 
            beq L0618E         ; 6185 F0 07 
            bcs L06191         ; 6187 B0 08 
            jsr L061D5         ; 6189 20 D5 61 
            bne L06191         ; 618C D0 03 
L0618E      jsr L061DE         ; 618E 20 DE 61 
L06191      cpy #$19           ; 6191 C0 19 
            bcs L061A5         ; 6193 B0 10 
            adc #$2A           ; 6195 69 2A 
            lsr $000C         ; 6197 46 0C 
            bcs L061AF         ; 6199 B0 14 
            adc #$1E           ; 619B 69 1E 
            lsr $000C         ; 619D 46 0C 
            bcs L061AF         ; 619F B0 0E 
            adc #$3C           ; 61A1 69 3C 
            bcc L061AF         ; 61A3 90 0A 
L061A5      beq L061AC         ; 61A5 F0 05 
            jsr L061DE         ; 61A7 20 DE 61 
            bne L061AF         ; 61AA D0 03 
L061AC      jsr L061D5         ; 61AC 20 D5 61 
L061AF      cmp $0009         ; 61AF C5 09 
            bcc L061D2         ; 61B1 90 1F 
            cmp $000A         ; 61B3 C5 0A 
            bcc L061D2         ; 61B5 90 1B 
            sta $000C         ; 61B7 85 0C 
            lsr                ; 61B9 4A 
            clc                ; 61BA 18 
            adc $000C         ; 61BB 65 0C 
            ror                ; 61BD 6A 
            sta $000C         ; 61BE 85 0C 
            lda $000A         ; 61C0 A5 0A 
            adc $0009         ; 61C2 65 09 
            ror                ; 61C4 6A 
            cmp $000C         ; 61C5 C5 0C 
            bcs L061D2         ; 61C7 B0 09 
            txa                ; 61C9 8A 
            pha                ; 61CA 48 
            jsr L062A1         ; 61CB 20 A1 62 
            pla                ; 61CE 68 
            tax                ; 61CF AA 
            ldy #$00           ; 61D0 A0 00 
L061D2      jmp L06116         ; 61D2 4C 16 61 
L061D5      bit $0073         ; 61D5 24 73 
            bpl L061DB         ; 61D7 10 02 
            adc #$08           ; 61D9 69 08 
L061DB      adc #$1C           ; 61DB 69 1C 
            rts                ; 61DD 60 
L061DE      adc #$1C           ; 61DE 69 1C 
            pha                ; 61E0 48 
            lda $021A         ; 61E1 AD 1A 02 
            lsr                ; 61E4 4A 
            pla                ; 61E5 68 
            bcs L061EA         ; 61E6 B0 02 
            adc #$12           ; 61E8 69 12 
L061EA      rts                ; 61EA 60 
L061EB      lda $0200,y       ; 61EB B9 00 02 
            and #$07           ; 61EE 29 07 
            sta $0009         ; 61F0 85 09 
            lda PKY_RANDOM         ; 61F2 AD 0A 2C 
            and #$18           ; 61F5 29 18 
            ora $0009         ; 61F7 05 09 
            sta $0200,x       ; 61F9 9D 00 02 
            lda $02A5,y       ; 61FC B9 A5 02 
            sta $02A5,x       ; 61FF 9D A5 02 
            lda $0263,y       ; 6202 B9 63 02 
            sta $0263,x       ; 6205 9D 63 02 
            lda $02C6,y       ; 6208 B9 C6 02 
            sta $02C6,x       ; 620B 9D C6 02 
            lda $0284,y       ; 620E B9 84 02 
            sta $0284,x       ; 6211 9D 84 02 
            lda $0221,y       ; 6214 B9 21 02 
            sta $0221,x       ; 6217 9D 21 02 
            lda $0242,y       ; 621A B9 42 02 
            sta $0242,x       ; 621D 9D 42 02 
            rts                ; 6220 60 
L06221      ldy #$FF           ; 6221 A0 FF 
            bit $0012         ; 6223 24 12 
            bmi L06254         ; 6225 30 2D 
L06227      iny                ; 6227 C8 
            iny                ; 6228 C8 
            lda ($0010),y     ; 6229 B1 10 
            eor $000A         ; 622B 45 0A 
            sta ($0003),y     ; 622D 91 03 
            dey                ; 622F 88 
            cmp #$F0           ; 6230 C9 F0 
            bcs L0624B         ; 6232 B0 17 
            lda ($0010),y     ; 6234 B1 10 
            sta ($0003),y     ; 6236 91 03 
            iny                ; 6238 C8 
            iny                ; 6239 C8 
            lda ($0010),y     ; 623A B1 10 
            sta ($0003),y     ; 623C 91 03 
            iny                ; 623E C8 
            lda ($0010),y     ; 623F B1 10 
            eor $0009         ; 6241 45 09 
            sta ($0003),y     ; 6243 91 03 
L06245      dex                ; 6245 CA 
            bpl L06227         ; 6246 10 DF 
            jmp L07A55         ; 6248 4C 55 7A 
L0624B      lda ($0010),y     ; 624B B1 10 
            eor $0009         ; 624D 45 09 
            sta ($0003),y     ; 624F 91 03 
            iny                ; 6251 C8 
            bne L06245         ; 6252 D0 F1 
L06254      iny                ; 6254 C8 
            lda ($0010),y     ; 6255 B1 10 
            sta $0014         ; 6257 85 14 
            iny                ; 6259 C8 
            lda ($0010),y     ; 625A B1 10 
            cmp #$F0           ; 625C C9 F0 
            bcs L0628C         ; 625E B0 2C 
            iny                ; 6260 C8 
            iny                ; 6261 C8 
            eor ($0010),y     ; 6262 51 10 
            and #$0F           ; 6264 29 0F 
            sta $0013         ; 6266 85 13 
            eor ($0010),y     ; 6268 51 10 
            eor $0009         ; 626A 45 09 
            sta ($0003),y     ; 626C 91 03 
            dey                ; 626E 88 
            lda ($0010),y     ; 626F B1 10 
            pha                ; 6271 48 
            lda $0014         ; 6272 A5 14 
            sta ($0003),y     ; 6274 91 03 
            dey                ; 6276 88 
            lda $0013         ; 6277 A5 13 
            eor $000A         ; 6279 45 0A 
            eor ($0010),y     ; 627B 51 10 
            sta ($0003),y     ; 627D 91 03 
            dey                ; 627F 88 
            pla                ; 6280 68 
            sta ($0003),y     ; 6281 91 03 
            iny                ; 6283 C8 
            iny                ; 6284 C8 
L06285      iny                ; 6285 C8 
            dex                ; 6286 CA 
            bpl L06254         ; 6287 10 CB 
            jmp L07A55         ; 6289 4C 55 7A 
L0628C      eor $0014         ; 628C 45 14 
            and #$07           ; 628E 29 07 
            pha                ; 6290 48 
            eor ($0010),y     ; 6291 51 10 
            eor $000A         ; 6293 45 0A 
            sta ($0003),y     ; 6295 91 03 
            dey                ; 6297 88 
            pla                ; 6298 68 
            eor ($0010),y     ; 6299 51 10 
            eor $0009         ; 629B 45 09 
            sta ($0003),y     ; 629D 91 03 
            bcs L06285         ; 629F B0 E4 
L062A1      jsr L074E7         ; 62A1 20 E7 74 
            bcs L06308         ; 62A4 B0 62 
            cpx #$01           ; 62A6 E0 01 
            bne L062B0         ; 62A8 D0 06 
            cpy #$19           ; 62AA C0 19 
            bne L062B6         ; 62AC D0 08 
            dex                ; 62AE CA 
            iny                ; 62AF C8 
L062B0      txa                ; 62B0 8A 
            bne L062C9         ; 62B1 D0 16 
            jsr L062F8         ; 62B3 20 F8 62 
L062B6      lda #$A0           ; 62B6 A9 A0 
            sta $0219,x       ; 62B8 9D 19 02 
            lda #$00           ; 62BB A9 00 
            sta $023A,x       ; 62BD 9D 3A 02 
            sta $025B,x       ; 62C0 9D 5B 02 
            cpy #$19           ; 62C3 C0 19 
            bcc L062D4         ; 62C5 90 0D 
            bcs L06308         ; 62C7 B0 3F 
L062C9      lda #$00           ; 62C9 A9 00 
            sta $0219,x       ; 62CB 9D 19 02 
            cpy #$19           ; 62CE C0 19 
            beq L062F3         ; 62D0 F0 21 
            bcs L06308         ; 62D2 B0 34 
L062D4      jsr L06F62         ; 62D4 20 62 6F 
L062D7      lda $0200,y       ; 62D7 B9 00 02 
            and #$03           ; 62DA 29 03 
            eor #$02           ; 62DC 49 02 
            lsr                ; 62DE 4A 
            ror                ; 62DF 6A 
            ror                ; 62E0 6A 
            ora #$3F           ; 62E1 09 3F 
            sta $007F         ; 62E3 85 7F 
            lda #$A0           ; 62E5 A9 A0 
            sta $0200,y       ; 62E7 99 00 02 
            lda #$00           ; 62EA A9 00 
            sta $0221,y       ; 62EC 99 21 02 
            sta $0242,y       ; 62EF 99 42 02 
            rts                ; 62F2 60 
L062F3      jsr L062F8         ; 62F3 20 F8 62 
            bne L062D7         ; 62F6 D0 DF 
L062F8      txa                ; 62F8 8A 
            ldx $001E         ; 62F9 A6 1E 
            dec $006F,x       ; 62FB D6 6F 
            tax                ; 62FD AA 
            lda #$81           ; 62FE A9 81 



Sub_6300    sta $02EB         	; 6300 8D EB 02 
            lda #$05           	; 6303 A9 05 
            sta $00DE         	; 6305 85 DE 
            rts                	; 6307 60 
L06308      lda $02E9         	; 6308 AD E9 02 
            sta $02E8         	; 630B 8D E8 02 
            lda $0022         	; 630E A5 22 
            beq L062D7         	; 6310 F0 C5 
            lda $021A         	; 6312 AD 1A 02 
            lsr                	; 6315 4A 
            lda #$00           	; 6316 A9 00 
            bcs L0631C         	; 6318 B0 02 
            lda #$20           	; 631A A9 20 
L0631C      jsr L06C44         	; 631C 20 44 6C 
            jmp L062D7         	; 631F 4C D7 62 
L06322      lda $0076         	; 6322 A5 76 
            and #$03           	; 6324 29 03 
            beq L06329         	; 6326 F0 01 
L06328      rts                	; 6328 60 
L06329      lda $021A         	; 6329 AD 1A 02 
            beq L06338         	; 632C F0 0A 
            bpl L06335         	; 632E 10 05 
            ldy #$17           	; 6330 A0 17 
            jmp L0770D         	; 6332 4C 0D 77 
L06335      jmp L063C5         	; 6335 4C C5 63 
L06338      jsr L067FA        	; 6338 20 FA 67 
            lda $0022         	; 633B A5 22 
            beq L06346         	; 633D F0 07 
            lda $0219         	; 633F AD 19 02 
            beq L06328         	; 6342 F0 E4 
            bmi L06328         	; 6344 30 E2 
L06346      lda $02EA         	; 6346 AD EA 02 
            beq L0634E         	; 6349 F0 03 
            dec $02EA         	; 634B CE EA 02 
L0634E      dec $02E8         	; 634E CE E8 02 
            bne L06328         ; 6351 D0 D5 
            lda #$01           ; 6353 A9 01 
            sta $02E8         ; 6355 8D E8 02 
            ldx $02E7         ; 6358 AE E7 02 
            beq L06328         ; 635B F0 CB 
            lda $02EA         ; 635D AD EA 02 
            beq L06367         ; 6360 F0 05 
            cpx $02EE         ; 6362 EC EE 02 
            bcs L06328         ; 6365 B0 C1 
L06367      lda $02E9         ; 6367 AD E9 02 
            sec                ; 636A 38 
            sbc #$06           ; 636B E9 06 
            cmp #$20           ; 636D C9 20 
            bcc L06374         ; 636F 90 03 
            sta $02E9         ; 6371 8D E9 02 
L06374      lda PKY_RANDOM         ; 6374 AD 0A 2C 
            lsr                ; 6377 4A 
            ror $02E0         ; 6378 6E E0 02 
            lsr                ; 637B 4A 
            ror $02E0         ; 637C 6E E0 02 
            lsr                ; 637F 4A 
            ror $02E0         ; 6380 6E E0 02 
            adc #$04           ; 6383 69 04 
            cmp #$12           ; 6385 C9 12 
            bcc L0638B         ; 6387 90 02 
            sbc #$10           ; 6389 E9 10 
L0638B      sta $029E         ; 638B 8D 9E 02 
            lda PKY_RANDOM         ; 638E AD 0A 2C 
            asl                ; 6391 0A 
            lda #$00           ; 6392 A9 00 
            tax                ; 6394 AA 
            ldy #$10           ; 6395 A0 10 
            bcc L0639E         ; 6397 90 05 
            dex                ; 6399 CA 
            lda #$1F           ; 639A A9 1F 
            ldy #$F0           ; 639C A0 F0 
L0639E      sty $023B         ; 639E 8C 3B 02 
            sta $027D         ; 63A1 8D 7D 02 
            stx $02BF         ; 63A4 8E BF 02 
            ldy #$02           ; 63A7 A0 02 
            lda $00D3         ; 63A9 A5 D3 
            beq L063B9         ; 63AB F0 0C 
            lda $02EE         ; 63AD AD EE 02 
            sec                ; 63B0 38 
            sbc $02E7         ; 63B1 ED E7 02 
            cmp #$04           ; 63B4 C9 04 
            bcc L063B9         ; 63B6 90 01 
            dey                ; 63B8 88 
L063B9      sty $021A         ; 63B9 8C 1A 02 
            lda L063C2,y       ; 63BC B9 C2 63 
            tay                ; 63BF A8


 
;            jmp L07716         ; 63C0 4C 16 77
			.byt $4c, $16
L063C2		.byt $77


            .byt $17	; 63C3 17 
            .byt $37	; 63C4 37 
L063C5      lda $0076         ; 63C5 A5 76 
            asl                ; 63C7 0A 
            bne L063D6         ; 63C8 D0 0C 
            lda PKY_RANDOM         ; 63CA AD 0A 2C 
            and #$03           ; 63CD 29 03 
            tax                ; 63CF AA 
            lda L06457,x       ; 63D0 BD 57 64 
            sta $025C         ; 63D3 8D 5C 02 
L063D6      lda $0022         ; 63D6 A5 22 
            beq L063DF         ; 63D8 F0 05 
            lda $02EB         ; 63DA AD EB 02 
            bne L063E4         ; 63DD D0 05 
L063DF      dec $02E8         ; 63DF CE E8 02 
            beq L063E5         ; 63E2 F0 01 
L063E4      rts                ; 63E4 60 
L063E5      lda #$0A           ; 63E5 A9 0A 
            sta $02E8         ; 63E7 8D E8 02 
            ldy #$19           ; 63EA A0 19 
            lda $0022         ; 63EC A5 22 
            beq L063FF         ; 63EE F0 0F 
            ldx #$AA           ; 63F0 A2 AA 
            lda $021A         ; 63F2 AD 1A 02 
            lsr                ; 63F5 4A 
            bcs L063FA         ; 63F6 B0 02 
            ldx #$40           ; 63F8 A2 40 
L063FA      cpx PKY_RANDOM         ; 63FA EC 0A 2C 
            bcs L06402         ; 63FD B0 03 
L063FF      jsr L0645B         ; 63FF 20 5B 64 
L06402      sty $0018         ; 6402 84 18 
            ldx #$1A           ; 6404 A2 1A 
            jsr $4A22         ; 6406 20 22 4A 
            jsr $49E7         ; 6409 20 E7 49 
            sta $0013         ; 640C 85 13 
            lda $000C         ; 640E A5 0C 
            sta $0012         ; 6410 85 12 
            lda $0018         ; 6412 A5 18 
            clc                ; 6414 18 
            adc #$21           ; 6415 69 21 
            tay                ; 6417 A8 
            ldx #$3B           ; 6418 A2 3B 
            jsr $4A22         ; 641A 20 22 4A 
            jsr $49E7         ; 641D 20 E7 49 
            jsr $4A32         ; 6420 20 32 4A 
            sta $007A         ; 6423 85 7A 
            lda $0018         ; 6425 A5 18 
            cmp #$19           ; 6427 C9 19 
            bne L06442         ; 6429 D0 17 
            ldx $00D3         ; 642B A6 D3 
            cpx #$03           ; 642D E0 03 
            bcc L06433         ; 642F 90 02 
            ldx #$03           ; 6431 A2 03 
L06433      lda PKY_RANDOM         ; 6433 AD 0A 2C 
            and L0644F,x       ; 6436 3D 4F 64 
            bpl L0643E         ; 6439 10 03 
            ora L06453,x       ; 643B 1D 53 64 
L0643E      adc $007A         ; 643E 65 7A 
            sta $007A         ; 6440 85 7A 
L06442      ldy #$03           ; 6442 A0 03 
            lda #$01           ; 6444 A9 01 
            sta $000F         ; 6446 85 0F 
            ldx $0018         ; 6448 A6 18 
            sta $0018         ; 644A 85 18 
            jmp L064C8         ; 644C 4C C8 64 
L0644F      .byt $9F	; 644F 9F 
            .byt $8F	; 6450 8F 
            .byt $8F	; 6451 8F 
            .byt $87	; 6452 87 
L06453      rts                ; 6453 60 
            bvs L064C6         ; 6454 70 70 
            sei                ; 6456 78 
L06457      beq L06459         ; 6457 F0 00 
L06459      brk                ; 6459 00

 
            .byt $10 
L0645B		dey
 
L0645C      lda $0200,y       ; 645C B9 00 02 
            bne L06468         ; 645F D0 07 
L06461      dey                ; 6461 88 
            bpl L0645C         ; 6462 10 F8 
            ldy #$19           ; 6464 A0 19 
            sec                ; 6466 38 
            rts                ; 6467 60 
L06468      bmi L06461         ; 6468 30 F7 
            cmp #$40           ; 646A C9 40 
            bcc L06478         ; 646C 90 0A 
            lsr                ; 646E 4A 
            lsr                ; 646F 4A 
            tax                ; 6470 AA 
            lda $02E8,x       ; 6471 BD E8 02 
            bpl L06461         ; 6474 10 EB 
            clc                ; 6476 18 
            rts                ; 6477 60 
L06478      lda $027D         ; 6478 AD 7D 02 
            sec                ; 647B 38 
            sbc $0263,y       ; 647C F9 63 02 
            bpl L06483         ; 647F 10 02 
            eor #$FF           ; 6481 49 FF 
L06483      cmp #$08           ; 6483 C9 08 
            bcs L06461         ; 6485 B0 DA 
            lda $029E         ; 6487 AD 9E 02 
            sec                ; 648A 38 
L0648B      sbc $0284,y       ; 648B F9 84 02 
            bpl L06492         ; 648E 10 02 
            eor #$FF           ; 6490 49 FF 
L06492      cmp #$08           ; 6492 C9 08 
            bcs L06461         ; 6494 B0 CB 
            rts                ; 6496 60 
L06497      ldx #$00           ; 6497 A2 00 
            lda $0022         ; 6499 A5 22 
L0649B      bne L064A0         ; 649B D0 03 
L0649D      stx $00FF         ; 649D 86 FF 
            rts                ; 649F 60 
L064A0      bit $0073         ; 64A0 24 73 
            bmi L0649D         ; 64A2 30 F9 
            bit $00FE         ; 64A4 24 FE 
            bpl L0649D         ; 64A6 10 F5 
            lda $02EB         ; 64A8 AD EB 02 
            bne L0649D         ; 64AB D0 F0 
            inc $00FF         ; 64AD E6 FF 
            lda $00FF         ; 64AF A5 FF 
            cmp #$02           ; 64B1 C9 02 
            bcc L064BA         ; 64B3 90 05 
            cmp #$0F           ; 64B5 C9 0F 
            bcs L0649D         ; 64B7 B0 E4 
            rts                ; 64B9 60 
L064BA      stx $0018         ; 64BA 86 18 
            lda #$03           ; 64BC A9 03 
            sta $000F         ; 64BE 85 0F 
            ldx #$19           ; 64C0 A2 19 
            lda $0079         ; 64C2 A5 79 
            sta $007A         ; 64C4 85 7A 
L064C6      ldy #$07           ; 64C6 A0 07 
L064C8      lda $0219,y       ; 64C8 B9 19 02 
            beq L064D3         ; 64CB F0 06 
            dey                ; 64CD 88 
            cpy $000F         ; 64CE C4 0F 
            bne L064C8         ; 64D0 D0 F6 
            rts                ; 64D2 60 
L064D3      stx $000E         ; 64D3 86 0E 
            lda #$12           ; 64D5 A9 12 
            sta $0219,y       ; 64D7 99 19 02 
            lda $007A         ; 64DA A5 7A 
            jsr L070FE         ; 64DC 20 FE 70 
            ldx $000E         ; 64DF A6 0E 
            cmp #$80           ; 64E1 C9 80 
            ror                ; 64E3 6A 
            sta $000A         ; 64E4 85 0A 
            clc                ; 64E6 18 
            adc $0221,x       ; 64E7 7D 21 02 
            bmi L064F4         ; 64EA 30 08 
            cmp #$70           ; 64EC C9 70 
            bcc L064FA         ; 64EE 90 0A 
            lda #$6F           ; 64F0 A9 6F 
            bne L064FA         ; 64F2 D0 06 
L064F4      cmp #$91           ; 64F4 C9 91 
            bcs L064FA         ; 64F6 B0 02 
            lda #$91           ; 64F8 A9 91 
L064FA      sta $023A,y       ; 64FA 99 3A 02 
            lda $007A         ; 64FD A5 7A 
            jsr L07101         ; 64FF 20 01 71 
            ldx $000E         ; 6502 A6 0E 
            cmp #$80           ; 6504 C9 80 
            ror                ; 6506 6A 
            sta $000D         ; 6507 85 0D 
            clc                ; 6509 18 
            adc $0242,x       ; 650A 7D 42 02 
            bmi L06517         ; 650D 30 08 
            cmp #$70           ; 650F C9 70 
            bcc L0651D         ; 6511 90 0A 
            lda #$6F           ; 6513 A9 6F 
            bne L0651D         ; 6515 D0 06 
L06517      cmp #$91           ; 6517 C9 91 
            bcs L0651D         ; 6519 B0 02 
            lda #$91           ; 651B A9 91 
L0651D      sta $025B,y       ; 651D 99 5B 02 
            ldx #$00           ; 6520 A2 00 
            lda $000A         ; 6522 A5 0A 
            bpl L06527         ; 6524 10 01 
            dex                ; 6526 CA 
L06527      stx $0009         ; 6527 86 09 
            ldx $0018         ; 6529 A6 18 
            cmp #$80           ; 652B C9 80 
            ror                ; 652D 6A 
            clc                ; 652E 18 
            adc $000A         ; 652F 65 0A 
            clc                ; 6531 18 
            adc $02BE,x       ; 6532 7D BE 02 
            sta $02BE,y       ; 6535 99 BE 02 
            lda $0009         ; 6538 A5 09 
            adc $027C,x       ; 653A 7D 7C 02 
            sta $027C,y       ; 653D 99 7C 02 
            ldx #$00           ; 6540 A2 00 
            lda $000D         ; 6542 A5 0D 
            bpl L06547         ; 6544 10 01 
            dex                ; 6546 CA 
L06547      stx $000C         ; 6547 86 0C 
            ldx $0018         ; 6549 A6 18 
            cmp #$80           ; 654B C9 80 
            ror                ; 654D 6A 
            clc                ; 654E 18 
            adc $000D         ; 654F 65 0D 
            clc                ; 6551 18 
            adc $02DF,x       ; 6552 7D DF 02 
            sta $02DF,y       ; 6555 99 DF 02 
            lda $000C         ; 6558 A5 0C 
            adc $029D,x       ; 655A 7D 9D 02 
            sta $029D,y       ; 655D 99 9D 02 
            ldy #$27           ; 6560 A0 27 
            cpx #$01           ; 6562 E0 01 
            bcc L06568         ; 6564 90 02 
            ldy #$1F           ; 6566 A0 1F 
L06568      jmp L07717         ; 6568 4C 17 77 
L0656B      lda $0042         ; 656B A5 42 
            and $0043         ; 656D 25 43 
            bpl L0657B         ; 656F 10 0A 
            lda $0022         ; 6571 A5 22 
            bne L06578         ; 6573 D0 03 
            jsr L07844         ; 6575 20 44 78 
L06578      lda #$FF           ; 6578 A9 FF 
            rts                ; 657A 60 
L0657B      lda $0021         ; 657B A5 21 
            lsr                ; 657D 4A 
            beq L06598         ; 657E F0 18 
            ldy #$01           ; 6580 A0 01 
            jsr L07159         ; 6582 20 59 71 
            ldy #$02           ; 6585 A0 02 
            ldx $0043         ; 6587 A6 43 
            bpl L0658C         ; 6589 10 01 
            dey                ; 658B 88 
L0658C      sty $001E         ; 658C 84 1E 
            lda $0076         ; 658E A5 76 
            and #$10           ; 6590 29 10 
            bne L06598         ; 6592 D0 04 
            tya                ; 6594 98 
            jsr L079EB         ; 6595 20 EB 79 
L06598      lsr $001E         ; 6598 46 1E 
            jsr L06C95         ; 659A 20 95 6C 
            ldy #$02           ; 659D A0 02 
            jsr L07159         ; 659F 20 59 71 
            ldy #$03           ; 65A2 A0 03 
            jsr L07159         ; 65A4 20 59 71 
            ldy #$04           ; 65A7 A0 04 
            jsr L07159         ; 65A9 20 59 71 
            ldy #$05           ; 65AC A0 05 
            jsr L07159         ; 65AE 20 59 71 
            lda #$20           ; 65B1 A9 20 
            sta $0001         ; 65B3 85 01 
            lda #$64           ; 65B5 A9 64 
            ldx #$39           ; 65B7 A2 39 
            jsr L07A1F         ; 65B9 20 1F 7A 
            lda #$70           ; 65BC A9 70 
            jsr L07AEA         ; 65BE 20 EA 7A 
            ldx $001E         ; 65C1 A6 1E 
            ldy $0042,x       ; 65C3 B4 42 
            sty $000C         ; 65C5 84 0C 
            tya                ; 65C7 98 
            clc                ; 65C8 18 
            adc $0041         ; 65C9 65 41 
            sta $000D         ; 65CB 85 0D 
            jsr L066C5         ; 65CD 20 C5 66 
            ldy $000C         ; 65D0 A4 0C 
            iny                ; 65D2 C8 
            jsr L066C5         ; 65D3 20 C5 66 
            ldy $000C         ; 65D6 A4 0C 
            iny                ; 65D8 C8 
            iny                ; 65D9 C8 
            jsr L066C5         ; 65DA 20 C5 66 
            asl ShieldSwitch   ; 65DD 0E 03 20 
            rol $007B         ; 65E0 26 7B 
            lda $007B         ; 65E2 A5 7B 
            and #$1F           ; 65E4 29 1F 
            cmp #$07           ; 65E6 C9 07 
            bne L06618         ; 65E8 D0 2E 
            inc $0041         ; 65EA E6 41 
            lda $0041         ; 65EC A5 41 
            cmp #$03           ; 65EE C9 03 
            bcc L0660C         ; 65F0 90 1A 
            ldx $001E         ; 65F2 A6 1E 
            lda #$FF           ; 65F4 A9 FF 
            sta $0042,x       ; 65F6 95 42 
L065F8      ldx #$00           ; 65F8 A2 00 
            stx $001E         ; 65FA 86 1E 
            stx $0041         ; 65FC 86 41 
            ldx #$F0           ; 65FE A2 F0 
            stx $0077         ; 6600 86 77 
            lda $0042         ; 6602 A5 42 
            bpl L06609         ; 6604 10 03 
            jsr L07BC7         ; 6606 20 C7 7B 
L06609      jmp L06C95         ; 6609 4C 95 6C 
L0660C      inc $000D         ; 660C E6 0D 



			.byt $a6
L0660F		.byt $0d


            lda #$F4           ; 6610 A9 F4 
            sta $0077         ; 6612 85 77 
            lda #$0B           ; 6614 A9 0B 
            sta $0044,x       ; 6616 95 44 
L06618      lda $0077         ; 6618 A5 77 
            bne L06624         ; 661A D0 08 
            lda #$FF           ; 661C A9 FF 
            sta $0042         ; 661E 85 42 
            sta $0043         ; 6620 85 43 
            bmi L065F8         ; 6622 30 D4 
L06624      lda $0076         ; 6624 A5 76 
            and #$07           ; 6626 29 07 
            bne L06656         ; 6628 D0 2C 
            ldx $000D         ; 662A A6 0D 
            ldy $0044,x       ; 662C B4 44 
            bit RotLeftSwitch  ; 662E 2C 07 24 
            bpl L06634         ; 6631 10 01 
            iny                ; 6633 C8 
L06634      bit RotRightSwitch ; 6634 2C 06 24 
            bpl L0663C         ; 6637 10 03 
            dey                ; 6639 88 
            bmi L0664C         ; 663A 30 10 
L0663C      cpy #$0B           ; 663C C0 0B 
            bcs L0664E         ; 663E B0 0E 
            cpy #$01           ; 6640 C0 01 
            beq L06648         ; 6642 F0 04 
            ldy #$00           ; 6644 A0 00 
            beq L06654         ; 6646 F0 0C 
L06648      ldy #$0B           ; 6648 A0 0B 
            bne L06654         ; 664A D0 08 
L0664C      ldy #$24           ; 664C A0 24 
L0664E      cpy #$25           ; 664E C0 25 
            bcc L06654         ; 6650 90 02 
            ldy #$00           ; 6652 A0 00 
L06654      sty $0044,x       ; 6654 94 44 
L06656      lda #$00           ; 6656 A9 00 
            rts                ; 6658 60 
L06659      lsr $0073         ; 6659 46 73 
            lda $0022         ; 665B A5 22 
            beq L06680         ; 665D F0 21 
            lda $0219         ; 665F AD 19 02 
            bmi L06680         ; 6662 30 1C 
            beq L06680         ; 6664 F0 1A 
            lda $02EF         ; 6666 AD EF 02 
            beq L06680         ; 6669 F0 15 
            asl ShieldSwitch   ; 666B 0E 03 20 
            ror $0073         ; 666E 66 73 
            bpl L06680         ; 6670 10 0E 
            ldy #$57           ; 6672 A0 57 
            jsr L07713         ; 6674 20 13 77 
            lda $0076         ; 6677 A5 76 
            and #$03           ; 6679 29 03 
            bne L06680         ; 667B D0 03 
            dec $02EF         ; 667D CE EF 02 
L06680      rts                ; 6680 60 
L06681      bit $0073         ; 6681 24 73 
            bpl L06680         ; 6683 10 FB 
            lda $02EF         ; 6685 AD EF 02 
            and #$F0           ; 6688 29 F0 
            cmp #$60           ; 668A C9 60 
            bcs L06690         ; 668C B0 02 
            lda #$60           ; 668E A9 60 
L06690      pha                ; 6690 48 
            ldx #$12           ; 6691 A2 12 
            lda #$50           ; 6693 A9 50 
            stx $0010         ; 6695 86 10 
            sta $0011         ; 6697 85 11 
            ldx #$00           ; 6699 A2 00 
            stx $0009         ; 669B 86 09 
            stx $000A         ; 669D 86 0A 
            stx $0012         ; 669F 86 12 
            jsr L06221         ; 66A1 20 21 62 
            ldx #$07           ; 66A4 A2 07 
            pla                ; 66A6 68 
            sta $0009         ; 66A7 85 09 
            jsr L06E96         ; 66A9 20 96 6E 
            lda #$00           ; 66AC A9 00 
            sta $0009         ; 66AE 85 09 
            jmp L06E96         ; 66B0 4C 96 6E 
L066B3      lda #$06           ; 66B3 A9 06 
            sta $02EE         ; 66B5 8D EE 02 
            ldx #$00           ; 66B8 A2 00 
            txa                ; 66BA 8A 
L066BB      sta $0200,x       ; 66BB 9D 00 02 
            sta PageThree,x       ; 66BE 9D 00 03 
            inx                ; 66C1 E8 
            bne L066BB         ; 66C2 D0 F7 
            rts                ; 66C4 60 
L066C5      lda $0044,y       ; 66C5 B9 44 00 
            asl                ; 66C8 0A 
            tay                ; 66C9 A8 
            bne L066D9         ; 66CA D0 0D 
            lda $0042         ; 66CC A5 42 
            and $0043         ; 66CE 25 43 
            bmi L066D9         ; 66D0 30 07 
            ldx #$B0           ; 66D2 A2 B0 
            lda #$56           ; 66D4 A9 56 
            jmp L07A18         ; 66D6 4C 18 7A 
L066D9      jmp L079F8         ; 66D9 4C F8 79 
L066DC      ldx #$20           ; 66DC A2 20 
            lda #$00           ; 66DE A9 00 
            sta $0019         ; 66E0 85 19 
L066E2      lda $0200,x       ; 66E2 BD 00 02 
            bne L066EB         ; 66E5 D0 04 
L066E7      dex                ; 66E7 CA 
            bpl L066E2         ; 66E8 10 F8 
            rts                ; 66EA 60 
L066EB      bpl L06750         ; 66EB 10 63 
            jsr L070B6         ; 66ED 20 B6 70 
            lsr                ; 66F0 4A 
            lsr                ; 66F1 4A 
            lsr                ; 66F2 4A 
            lsr                ; 66F3 4A 
            cpx #$19           ; 66F4 E0 19 
            bne L066FF         ; 66F6 D0 07 
            lda $0076         ; 66F8 A5 76 
            and #$01           ; 66FA 29 01 
            lsr                ; 66FC 4A 
            beq L06700         ; 66FD F0 01 
L066FF      sec                ; 66FF 38 
L06700      adc $0200,x       ; 6700 7D 00 02 
            bmi L0672A         ; 6703 30 25 
            cpx #$19           ; 6705 E0 19 
            beq L0671C         ; 6707 F0 13 
            bcs L06722         ; 6709 B0 17 
            dec $02E7         ; 670B CE E7 02 
            bne L06715         ; 670E D0 05 
            ldy #$7F           ; 6710 A0 7F 
            sty $02EC         ; 6712 8C EC 02 
L06715      lda #$00           ; 6715 A9 00 
            sta $0200,x       ; 6717 9D 00 02 
            beq L066E7         ; 671A F0 CB 
L0671C      jsr L06A1B         ; 671C 20 1B 6A 
            jmp L06715         ; 671F 4C 15 67 
L06722      lda $02E9         ; 6722 AD E9 02 
            sta $02E8         ; 6725 8D E8 02 
            bne L06715         ; 6728 D0 EB 
L0672A      sta $0200,x       ; 672A 9D 00 02 
            and #$F0           ; 672D 29 F0 
            clc                ; 672F 18 
            adc #$10           ; 6730 69 10 
            cpx #$19           ; 6732 E0 19 
            bne L06738         ; 6734 D0 02 
            lda #$F0           ; 6736 A9 F0 
L06738      tay                ; 6738 A8 
            lda $02A5,x       ; 6739 BD A5 02 
            sta $0005         ; 673C 85 05 
            lda $0263,x       ; 673E BD 63 02 
            sta $0006         ; 6741 85 06 
            lda $02C6,x       ; 6743 BD C6 02 
            sta $0007         ; 6746 85 07 
            lda $0284,x       ; 6748 BD 84 02 
            sta $0008         ; 674B 85 08 
            jmp L067E5         ; 674D 4C E5 67 
L06750      sta $001B         ; 6750 85 1B 
            asl $001B         ; 6752 06 1B 
            bpl L0676B         ; 6754 10 15 
            cpx #$19           ; 6756 E0 19 
            bcs L0676B         ; 6758 B0 11 
            and #$3C           ; 675A 29 3C 
            lsr                ; 675C 4A 
            lsr                ; 675D 4A 
            tay                ; 675E A8 
            sty $001C         ; 675F 84 1C 
            lda $02F8,y       ; 6761 B9 F8 02 
            sta $001D         ; 6764 85 1D 
            bmi L0676B         ; 6766 30 03 
            jsr $48BE         ; 6768 20 BE 48 
L0676B      ldy #$00           ; 676B A0 00 
            clc                ; 676D 18 
            lda $0221,x       ; 676E BD 21 02 
            bpl L06774         ; 6771 10 01 
            dey                ; 6773 88 
L06774      adc $02A5,x       ; 6774 7D A5 02 
            sta $02A5,x       ; 6777 9D A5 02 
            sta $0005         ; 677A 85 05 
            tya                ; 677C 98 
            adc $0263,x       ; 677D 7D 63 02 
            cmp #$20           ; 6780 C9 20 
            bcc L067AE         ; 6782 90 2A 
            cpx #$1A           ; 6784 E0 1A 
            bne L0678E         ; 6786 D0 06 
            jsr L067EB         ; 6788 20 EB 67 
L0678B      jmp L066E7         ; 678B 4C E7 66 
L0678E      bit $001B         ; 678E 24 1B 
            bpl L067AC         ; 6790 10 1A 
            ldy $001D         ; 6792 A4 1D 
            bmi L067AC         ; 6794 30 16 
            cpy #$04           ; 6796 C0 04 
            bcs L067AC         ; 6798 B0 12 
            dec $02E7         ; 679A CE E7 02 
            dec $02FF         ; 679D CE FF 02 
            ldy $001C         ; 67A0 A4 1C 
            lda #$00           ; 67A2 A9 00 
            sta $0200,x       ; 67A4 9D 00 02 
            sta $02F8,y       ; 67A7 99 F8 02 
            beq L0678B         ; 67AA F0 DF 
L067AC      and #$1F           ; 67AC 29 1F 
L067AE      sta $0263,x       ; 67AE 9D 63 02 
            and #$7F           ; 67B1 29 7F 
            sta $0006         ; 67B3 85 06 
            clc                ; 67B5 18 
            ldy #$00           ; 67B6 A0 00 
            lda $0242,x       ; 67B8 BD 42 02 
            bpl L067BE         ; 67BB 10 01 
            dey                ; 67BD 88 
L067BE      adc $02C6,x       ; 67BE 7D C6 02 
            sta $02C6,x       ; 67C1 9D C6 02 
            sta $0007         ; 67C4 85 07 
            tya                ; 67C6 98 
            adc $0284,x       ; 67C7 7D 84 02 
            cmp #$18           ; 67CA C9 18 
            bcc L067D6         ; 67CC 90 08 
            beq L067D4         ; 67CE F0 04 
            lda #$17           ; 67D0 A9 17 
            bne L067D6         ; 67D2 D0 02 
L067D4      lda #$00           ; 67D4 A9 00 
L067D6      sta $0284,x       ; 67D6 9D 84 02 
            sta $0008         ; 67D9 85 08 
            lda $0200,x       ; 67DB BD 00 02 
            and #$03           ; 67DE 29 03 
            tay                ; 67E0 A8 
            lda L0680C,y       ; 67E1 B9 0C 68 
            tay                ; 67E4 A8 
L067E5      jsr L06BA8         ; 67E5 20 A8 6B 
            jmp L066E7         ; 67E8 4C E7 66 
L067EB      lda $02E9         ; 67EB AD E9 02 
            sta $02E8         ; 67EE 8D E8 02 
            tya                ; 67F1 98 
            pha                ; 67F2 48 
            ldy #$17           ; 67F3 A0 17 
            jsr L0770D         ; 67F5 20 0D 77 
            pla                ; 67F8 68 
            tay                ; 67F9 A8 
L067FA      lda #$00           ; 67FA A9 00 
            sta $021A         ; 67FC 8D 1A 02 
            sta $023B         ; 67FF 8D 3B 02 
            sta $025C         ; 6802 8D 5C 02 
            sta $027D         ; 6805 8D 7D 02 
            sta $02BF         ; 6808 8D BF 02 
L0680B      rts                ; 680B 60 
L0680C      brk                ; 680C 00 
            cpx #$F0           ; 680D E0 F0 

	.byt $e0
L06810	.byt $a5
	
            .byt $22	; 6811 22 
            beq L0680B         ; 6812 F0 F7 
            lda $0219         ; 6814 AD 19 02 
            bmi L0680B         ; 6817 30 F2 
            bne L06831         ; 6819 D0 16 
            dec $02EB         ; 681B CE EB 02 
            bne L0680B         ; 681E D0 EB 
            jsr L068FE         ; 6820 20 FE 68 
            bne L0680B         ; 6823 D0 E6 
            ldx #$01           ; 6825 A2 01 
            stx $0219         ; 6827 8E 19 02 
            stx $008B         ; 682A 86 8B 
            ldy #$47           ; 682C A0 47 
            jmp L07716         ; 682E 4C 16 77 
L06831      lda RotLeftSwitch  ; 6831 AD 07 24 
            bpl L0683A         ; 6834 10 04 
            lda #$03           ; 6836 A9 03 
            bne L06841         ; 6838 D0 07 
L0683A      lda RotRightSwitch ; 683A AD 06 24 
            bpl L06846         ; 683D 10 07 
            lda #$FD           ; 683F A9 FD 
L06841      clc                ; 6841 18 
            adc $0079         ; 6842 65 79 
            sta $0079         ; 6844 85 79 
L06846      lda $0076         ; 6846 A5 76 
            lsr                ; 6848 4A 
            bcs L0680B         ; 6849 B0 C0 
            lda ThrustSwitch   ; 684B AD 05 24 
            bpl L06885         ; 684E 10 35 
            lda #$40           ; 6850 A9 40 
            sta $0012         ; 6852 85 12 
            lda $023A         ; 6854 AD 3A 02 
            jsr L068B6         ; 6857 20 B6 68 
            adc $007C         ; 685A 65 7C 
            tax                ; 685C AA 
            lda $0011         ; 685D A5 11 
            adc $023A         ; 685F 6D 3A 02 
            jsr L068EA         ; 6862 20 EA 68 
            sta $023A         ; 6865 8D 3A 02 
            stx $007C         ; 6868 86 7C 
            lda #$00           ; 686A A9 00 
            sta $0012         ; 686C 85 12 
            lda $025B         ; 686E AD 5B 02 
            jsr L068B6         ; 6871 20 B6 68 
            adc $007D         ; 6874 65 7D 
            tax                ; 6876 AA 
            lda $0011         ; 6877 A5 11 
            adc $025B         ; 6879 6D 5B 02 
            jsr L068EA         ; 687C 20 EA 68 
            sta $025B         ; 687F 8D 5B 02 
            stx $007D         ; 6882 86 7D 
            rts                ; 6884 60 
L06885      lda #$00           ; 6885 A9 00 
            tax                ; 6887 AA 
            sec                ; 6888 38 
            sbc $023A         ; 6889 ED 3A 02 
            asl                ; 688C 0A 
            asl                ; 688D 0A 
            bcc L06892         ; 688E 90 02 
            dex                ; 6890 CA 
            clc                ; 6891 18 
L06892      adc $007C         ; 6892 65 7C 
            sta $007C         ; 6894 85 7C 
            txa                ; 6896 8A 
            adc $023A         ; 6897 6D 3A 02 
            sta $023A         ; 689A 8D 3A 02 
            lda #$00           ; 689D A9 00 
            tax                ; 689F AA 
            sec                ; 68A0 38 
            sbc $025B         ; 68A1 ED 5B 02 
            asl                ; 68A4 0A 
            asl                ; 68A5 0A 
            bcc L068AA         ; 68A6 90 02 
            dex                ; 68A8 CA 
            clc                ; 68A9 18 
L068AA      adc $007D         ; 68AA 65 7D 
            sta $007D         ; 68AC 85 7D 
            txa                ; 68AE 8A 
            adc $025B         ; 68AF 6D 5B 02 
            sta $025B         ; 68B2 8D 5B 02 
            rts                ; 68B5 60 
L068B6      bpl L068BB         ; 68B6 10 03 
            jsr L070B6         ; 68B8 20 B6 70 
L068BB      lsr                ; 68BB 4A 
            lsr                ; 68BC 4A 
            lsr                ; 68BD 4A 
            tax                ; 68BE AA 
            lda L068E1,x       ; 68BF BD E1 68 
            bit $0073         ; 68C2 24 73 
            bpl L068C7         ; 68C4 10 01 
            lsr                ; 68C6 4A 
L068C7      sta $0011         ; 68C7 85 11 
            lda $0079         ; 68C9 A5 79 
            clc                ; 68CB 18 
            adc $0012         ; 68CC 65 12 
            jsr L07101         ; 68CE 20 01 71 
            jsr $49BB         ; 68D1 20 BB 49 
            ldy #$00           ; 68D4 A0 00 
            asl                ; 68D6 0A 
            bcc L068DA         ; 68D7 90 01 
            dey                ; 68D9 88 
L068DA      asl                ; 68DA 0A 
            sty $0011         ; 68DB 84 11 
            rol $0011         ; 68DD 26 11 
            clc                ; 68DF 18 
            rts                ; 68E0 60 
L068E1      .byt $80	; 68E1 80 
            bvs L06944         ; 68E2 70 60 
            bvc L06926         ; 68E4 50 40 
            bmi L06908         ; 68E6 30 20 
            bpl L068FB         ; 68E8 10 11 
L068EA      bmi L068F5         ; 68EA 30 09 
            cmp #$40           ; 68EC C9 40 
            bcc L068FD         ; 68EE 90 0D 
            ldx #$FF           ; 68F0 A2 FF 
            lda #$3F           ; 68F2 A9 3F 
            rts                ; 68F4 60 
L068F5      cmp #$C1           ; 68F5 C9 C1 
            bcs L068FD         ; 68F7 B0 04 
            ldx #$01           ; 68F9 A2 01 
L068FB      lda #$C1           ; 68FB A9 C1 
L068FD      rts                ; 68FD 60 
L068FE      lda $0076         ; 68FE A5 76 
            and #$03           ; 6900 29 03 
            bne L06929         ; 6902 D0 25 
            inc $00DE         ; 6904 E6 DE 
            bne L06929         ; 6906 D0 21 
L06908      lda #$02           ; 6908 A9 02 
            sta $0010         ; 690A 85 10 
L0690C      ldx #$18           ; 690C A2 18 
L0690E      lda $0200,x       ; 690E BD 00 02 
            asl                ; 6911 0A 
            bmi L06922         ; 6912 30 0E 
            and $0010         ; 6914 25 10 
            beq L06922         ; 6916 F0 0A 
            dec $02E7         ; 6918 CE E7 02 
            lda #$00           ; 691B A9 00 
            sta $0200,x       ; 691D 9D 00 02 
            beq L06929         ; 6920 F0 07 
L06922      dex                ; 6922 CA 
            bpl L0690E         ; 6923 10 E9 


			.byt $06
L06926		.byt $10


            bpl L0690C         ; 6927 10 E3 
L06929      ldx #$18           ; 6929 A2 18 
L0692B      lda $0200,x       ; 692B BD 00 02 
            beq L0694E         ; 692E F0 1E 
            lda $0263,x       ; 6930 BD 63 02 
            sec                ; 6933 38 
            sbc $027C         ; 6934 ED 7C 02 
            cmp #$05           ; 6937 C9 05 
            bcc L0693F         ; 6939 90 04 
            cmp #$FB           ; 693B C9 FB 
            bcc L0694E         ; 693D 90 0F 
L0693F      lda $0284,x       ; 693F BD 84 02 
            sec                ; 6942 38 


			.byt $ed
L06944      .byt $9d, $02


            cmp #$05           ; 6946 C9 05 
            bcc L06953         ; 6948 90 09 
            cmp #$FB           ; 694A C9 FB 
            bcs L06953         ; 694C B0 05 
L0694E      dex                ; 694E CA 
            bpl L0692B         ; 694F 10 DA 
            inx                ; 6951 E8 
            rts                ; 6952 60 
L06953      inc $02EB         ; 6953 EE EB 02 
L06956      rts                ; 6956 60 
            .byt $1C	; 6957 1C
            .byt $10
L06959	    ldx #$18
            lda $02EC         ; 695B AD EC 02 
            beq L06963         ; 695E F0 03 
            jmp L069DE         ; 6960 4C DE 69 
L06963      lda $0022         ; 6963 A5 22 
            beq L0696E         ; 6965 F0 07 
            lda $0219         ; 6967 AD 19 02 
            beq L06956         ; 696A F0 EA 
            bmi L06956         ; 696C 30 E8 
L0696E      lda $021A         ; 696E AD 1A 02 
            bne L06956         ; 6971 D0 E3 
            sta $02FF         ; 6973 8D FF 02 
            ldx $001E         ; 6976 A6 1E 
            sta $00D1,x       ; 6978 95 D1 
            ldx #$06           ; 697A A2 06 
L0697C      sta $02F8,x       ; 697C 9D F8 02 
            dex                ; 697F CA 
            bpl L0697C         ; 6980 10 FA 
            lda OptionSwitch65 ; 6982 AD 01 28 
            and #$02           ; 6985 29 02 
            ora #$04           ; 6987 09 04 
            clc                ; 6989 18 
            adc $02F0         ; 698A 6D F0 02 
            tay                ; 698D A8 
            cmp #$0A           ; 698E C9 0A 
            bcc L06994         ; 6990 90 02 
            lda #$0A           ; 6992 A9 0A 
L06994      sta $00D9         ; 6994 85 D9 
            jsr L070B6         ; 6996 20 B6 70 
            sta $00DB         ; 6999 85 DB 
            clc                ; 699B 18 
            adc #$EB           ; 699C 69 EB 
            sta $00DC         ; 699E 85 DC 
            jsr L070B6         ; 69A0 20 B6 70 
            sta $00DA         ; 69A3 85 DA 
            cpy #$3F           ; 69A5 C0 3F 
            bcc L069B0         ; 69A7 90 07 
            lda $0022         ; 69A9 A5 22 
            bne L069B3         ; 69AB D0 06 
            jsr L066B3         ; 69AD 20 B3 66 
L069B0      inc $02F0         ; 69B0 EE F0 02 
L069B3      cpy #$09           ; 69B3 C0 09 
            bcc L069B9         ; 69B5 90 02 
            ldy #$09           ; 69B7 A0 09 
L069B9      sty $02E7         ; 69B9 8C E7 02 
            sty $0009         ; 69BC 84 09 
            ldx #$18           ; 69BE A2 18 
            lda $02EE         ; 69C0 AD EE 02 
            cmp #$0A           ; 69C3 C9 0A 
            bcs L069CA         ; 69C5 B0 03 
            inc $02EE         ; 69C7 EE EE 02 
L069CA      ldy #$1A           ; 69CA A0 1A 
L069CC      jsr L069E7         ; 69CC 20 E7 69 
            dex                ; 69CF CA 
            dec $0009         ; 69D0 C6 09 
            bne L069CC         ; 69D2 D0 F8 
            lda #$7F           ; 69D4 A9 7F 
            sta $02E8         ; 69D6 8D E8 02 
            lda #$30           ; 69D9 A9 30 
            sta $02ED         ; 69DB 8D ED 02 
L069DE      lda #$00           ; 69DE A9 00 
L069E0      sta $0200,x       ; 69E0 9D 00 02 
            dex                ; 69E3 CA 
            bpl L069E0         ; 69E4 10 FA 
            rts                ; 69E6 60 
L069E7      lda PKY_RANDOM         ; 69E7 AD 0A 2C 
            and #$38           ; 69EA 29 38 
            ora #$04           ; 69EC 09 04 
            sta $0200,x       ; 69EE 9D 00 02 
            jsr L06A3B         ; 69F1 20 3B 6A 
            lda PKY_RANDOM         ; 69F4 AD 0A 2C 
            lsr                ; 69F7 4A 
            and #$1F           ; 69F8 29 1F 
            bcc L06A0F         ; 69FA 90 13 
            cmp #$18           ; 69FC C9 18 
            bcc L06A02         ; 69FE 90 02 
            and #$17           ; 6A00 29 17 
L06A02      sta $0284,x       ; 6A02 9D 84 02 
            lda #$00           ; 6A05 A9 00 
            sta $0263,x       ; 6A07 9D 63 02 
            sta $02A5,x       ; 6A0A 9D A5 02 
            beq L06A1A         ; 6A0D F0 0B 
L06A0F      sta $0263,x       ; 6A0F 9D 63 02 
            lda #$00           ; 6A12 A9 00 
            sta $0284,x       ; 6A14 9D 84 02 
            sta $02C6,x       ; 6A17 9D C6 02 
L06A1A      rts                ; 6A1A 60 
L06A1B      lda #$60           ; 6A1B A9 60 
            sta $02BE         ; 6A1D 8D BE 02 
            sta $02DF         ; 6A20 8D DF 02 
            lda #$00           ; 6A23 A9 00 
            sta $023A         ; 6A25 8D 3A 02 
            sta $025B         ; 6A28 8D 5B 02 
            lda #$10           ; 6A2B A9 10 
            sta $027C         ; 6A2D 8D 7C 02 
            lda #$0C           ; 6A30 A9 0C 
            sta $029D         ; 6A32 8D 9D 02 
            lda #$FF           ; 6A35 A9 FF 
            sta $02EF         ; 6A37 8D EF 02 
            rts                ; 6A3A 60 
L06A3B      lda PKY_RANDOM         ; 6A3B AD 0A 2C 
            and #$8F           ; 6A3E 29 8F 
            bpl L06A44         ; 6A40 10 02 
            ora #$F0           ; 6A42 09 F0 
L06A44      clc                ; 6A44 18 
            adc $0221,y       ; 6A45 79 21 02 
            jsr L06A62         ; 6A48 20 62 6A 
            sta $0221,x       ; 6A4B 9D 21 02 
            lda PKY_RANDOM         ; 6A4E AD 0A 2C 
            and #$8F           ; 6A51 29 8F 
            bpl L06A57         ; 6A53 10 02 
            ora #$F0           ; 6A55 09 F0 
L06A57      clc                ; 6A57 18 
            adc $0242,y       ; 6A58 79 42 02 
            jsr L06A62         ; 6A5B 20 62 6A 
            sta $0242,x       ; 6A5E 9D 42 02 
            rts                ; 6A61 60 
L06A62      bpl L06A71         ; 6A62 10 0D 
            cmp $00DC         ; 6A64 C5 DC 
            bcs L06A6A         ; 6A66 B0 02 
L06A68      lda $00DC         ; 6A68 A5 DC 
L06A6A      cmp $00DB         ; 6A6A C5 DB 
            bcc L06A7D         ; 6A6C 90 0F 
            lda $00DB         ; 6A6E A5 DB 
            rts                ; 6A70 60 
L06A71      cmp $00D9         ; 6A71 C5 D9 
            bcs L06A77         ; 6A73 B0 02 
            lda $00D9         ; 6A75 A5 D9 
L06A77      cmp $00DA         ; 6A77 C5 DA 
            bcc L06A7D         ; 6A79 90 02 
            lda $00DA         ; 6A7B A5 DA 
L06A7D      rts                ; 6A7D 60 
L06A7E      lda $0022         ; 6A7E A5 22 
            beq L06A8F         ; 6A80 F0 0D 
            lda #$81           ; 6A82 A9 81 
            ldx #$C3           ; 6A84 A2 C3 
            jsr L07CD5         ; 6A86 20 D5 7C 
            ldy #$00           ; 6A89 A0 00 
            lda $001E         ; 6A8B A5 1E 
            bne L06A91         ; 6A8D D0 02 
L06A8F      ldy #$10           ; 6A8F A0 10 
L06A91      sty $0001         ; 6A91 84 01 
            lda #$19           ; 6A93 A9 19 
            ldx #$DB           ; 6A95 A2 DB 
            jsr L07A1F         ; 6A97 20 1F 7A 
            lda #$70           ; 6A9A A9 70 
            jsr L07AEA         ; 6A9C 20 EA 7A 
            lda $0022         ; 6A9F A5 22 
            cmp #$02           ; 6AA1 C9 02 
            bcc L06AB9         ; 6AA3 90 14 
            lda $001E         ; 6AA5 A5 1E 
            bne L06AB9         ; 6AA7 D0 10 
            lda $0219         ; 6AA9 AD 19 02 
            bne L06AB9         ; 6AAC D0 0B 
            lda $02EB         ; 6AAE AD EB 02 
            bmi L06AB9         ; 6AB1 30 06 
            lda $0076         ; 6AB3 A5 76 
            and #$10           ; 6AB5 29 10 
            beq L06AC1         ; 6AB7 F0 08 
L06AB9      lda #$62           ; 6AB9 A9 62 
            ldy #$03           ; 6ABB A0 03 
            sec                ; 6ABD 38 
            jsr L07C95         ; 6ABE 20 95 7C 
L06AC1      ldy $006F         ; 6AC1 A4 6F 
            lda #$00           ; 6AC3 A9 00 
            jsr L06B74         ; 6AC5 20 74 6B 
            lda #$28           ; 6AC8 A9 28 
            jsr L07C6F         ; 6ACA 20 6F 7C 
            lda $001E         ; 6ACD A5 1E 
            bne L06ADB         ; 6ACF D0 0A 
            lda $0219         ; 6AD1 AD 19 02 
            bmi L06ADB         ; 6AD4 30 05 
            beq L06ADB         ; 6AD6 F0 03 
L06AD8      jsr L06B89         ; 6AD8 20 89 6B 
L06ADB      lda #$00           ; 6ADB A9 00 
            sta $0001         ; 6ADD 85 01 
            ora $0024         ; 6ADF 05 24 
            ora $0025         ; 6AE1 05 25 
            beq L06B03         ; 6AE3 F0 1E 
            lda #$74           ; 6AE5 A9 74 
            ldx #$DB           ; 6AE7 A2 DB 
            jsr L07A1F         ; 6AE9 20 1F 7A 
            lda #$50           ; 6AEC A9 50 
            jsr L07AEA         ; 6AEE 20 EA 7A 
            lda #$23           ; 6AF1 A9 23 
            ldy #$03           ; 6AF3 A0 03 
            sec                ; 6AF5 38 
            jsr L07C95         ; 6AF6 20 95 7C 
            ldy #$00           ; 6AF9 A0 00 
            sty $0013         ; 6AFB 84 13 
            jsr L079F8         ; 6AFD 20 F8 79 
            jsr L06D42         ; 6B00 20 42 6D 
L06B03      lda $0022         ; 6B03 A5 22 
            beq L06B1D         ; 6B05 F0 16 
            lda $00F9         ; 6B07 A5 F9 
            bmi L06B1D         ; 6B09 30 12 
            ldy #$0D           ; 6B0B A0 0D 
            lda #$00           ; 6B0D A9 00 
            jsr L07162         ; 6B0F 20 62 71 
            lda #$68           ; 6B12 A9 68 
            clc                ; 6B14 18 
            adc $0020         ; 6B15 65 20 
            ldy #$03           ; 6B17 A0 03 
            sec                ; 6B19 38 
            jsr L07C95         ; 6B1A 20 95 7C 
L06B1D      ldx #$10           ; 6B1D A2 10 
            lda $0022         ; 6B1F A5 22 
            cmp #$01           ; 6B21 C9 01 
            beq L06B88         ; 6B23 F0 63 
            bcc L06B2C         ; 6B25 90 05 
            lda $001E         ; 6B27 A5 1E 
            bne L06B2C         ; 6B29 D0 01 
            tax                ; 6B2B AA 
L06B2C      stx $0001         ; 6B2C 86 01 
            lda #$C0           ; 6B2E A9 C0 
            ldx #$DB           ; 6B30 A2 DB 
            jsr L07A1F         ; 6B32 20 1F 7A 
            lda #$50           ; 6B35 A9 50 
            jsr L07AEA         ; 6B37 20 EA 7A 
            lda $0022         ; 6B3A A5 22 
            beq L06B52         ; 6B3C F0 14 
            lda $001E         ; 6B3E A5 1E 
            beq L06B52         ; 6B40 F0 10 
            lda $0219         ; 6B42 AD 19 02 
            bne L06B52         ; 6B45 D0 0B 
            lda $02EB         ; 6B47 AD EB 02 
            bmi L06B52         ; 6B4A 30 06 
            lda $0076         ; 6B4C A5 76 
            and #$10           ; 6B4E 29 10 
            beq L06B5A         ; 6B50 F0 08 
L06B52      lda #$65           ; 6B52 A9 65 
            ldy #$03           ; 6B54 A0 03 
            sec                ; 6B56 38 
            jsr L07C95         ; 6B57 20 95 7C 
L06B5A      ldy $0070         ; 6B5A A4 70 
            lda #$01           ; 6B5C A9 01 
            jsr L06B74         ; 6B5E 20 74 6B 
            lda #$CF           ; 6B61 A9 CF 
            jsr L07C6F         ; 6B63 20 6F 7C 
            lda $001E         ; 6B66 A5 1E 
            beq L06B88         ; 6B68 F0 1E 
            lda $0219         ; 6B6A AD 19 02 
            beq L06B88         ; 6B6D F0 19 
            bmi L06B88         ; 6B6F 30 17 
            jmp L06B89         ; 6B71 4C 89 6B 
L06B74      eor $001E         ; 6B74 45 1E 
            beq L06B81         ; 6B76 F0 09 
            ldx V0319         ; 6B78 AE 19 03 
            bmi L06B7F         ; 6B7B 30 02 
            bne L06B80         ; 6B7D D0 01 
L06B7F      iny                ; 6B7F C8 
L06B80      rts                ; 6B80 60 
L06B81      ldx $0219         ; 6B81 AE 19 02 
            beq L06B7F         ; 6B84 F0 F9 
            bmi L06B7F         ; 6B86 30 F7 
L06B88      rts                ; 6B88 60 
L06B89      lda $008B         ; 6B89 A5 8B 
            cmp #$C0           ; 6B8B C9 C0 
            bcc L06B90         ; 6B8D 90 01 
            rts                ; 6B8F 60 
L06B90      pha                ; 6B90 48 
            eor #$FF           ; 6B91 49 FF 
            adc #$C1           ; 6B93 69 C1 
            sta $008B         ; 6B95 85 8B 
            lda $0079         ; 6B97 A5 79 
            pha                ; 6B99 48 
            lda #$40           ; 6B9A A9 40 
            sta $0079         ; 6B9C 85 79 
            jsr L06E20         ; 6B9E 20 20 6E 
            pla                ; 6BA1 68 
            sta $0079         ; 6BA2 85 79 
            pla                ; 6BA4 68 
            sta $008B         ; 6BA5 85 8B 
            rts                ; 6BA7 60 
L06BA8      sty $0001         ; 6BA8 84 01 
            stx $000E         ; 6BAA 86 0E 
            lsr $0006         ; 6BAC 46 06 
            ror $0005         ; 6BAE 66 05 
            lsr $0006         ; 6BB0 46 06 
            ror $0005         ; 6BB2 66 05 
            lsr $0006         ; 6BB4 46 06 
            ror $0005         ; 6BB6 66 05 
            lsr $0008         ; 6BB8 46 08 
            ror $0007         ; 6BBA 66 07 
            lsr $0008         ; 6BBC 46 08 
            ror $0007         ; 6BBE 66 07 
            inc $0008         ; 6BC0 E6 08 
            lsr $0008         ; 6BC2 46 08 
            ror $0007         ; 6BC4 66 07 
            jsr L06C27         ; 6BC6 20 27 6C 
            ldx $000E         ; 6BC9 A6 0E 
            jsr L06BD1         ; 6BCB 20 D1 6B 
            ldx $000E         ; 6BCE A6 0E 
            rts                ; 6BD0 60 
L06BD1      lda $0200,x       ; 6BD1 BD 00 02 
            bpl L06BE9         ; 6BD4 10 13 
            cpx #$19           ; 6BD6 E0 19 
            beq L06BE6         ; 6BD8 F0 0C 
            and #$0C           ; 6BDA 29 0C 
            lsr                ; 6BDC 4A 
            tay                ; 6BDD A8 
            lda $4D80,y       ; 6BDE B9 80 4D 
            ldx $4D81,y       ; 6BE1 BE 81 4D 
            bne L06C05         ; 6BE4 D0 1F 
L06BE6      jmp L06D5B         ; 6BE6 4C 5B 6D 
L06BE9      cpx #$19           ; 6BE9 E0 19 
            beq L06C08         ; 6BEB F0 1B 
            cpx #$1A           ; 6BED E0 1A 
            beq L06C0E         ; 6BEF F0 1D 
            bcs L06C14         ; 6BF1 B0 21 
            asl                ; 6BF3 0A 
            bpl L06BF9         ; 6BF4 10 03 
            jmp $4A75         ; 6BF6 4C 75 4A 
L06BF9      lsr                ; 6BF9 4A 
            lsr                ; 6BFA 4A 
            lsr                ; 6BFB 4A 
            and #$06           ; 6BFC 29 06 
            tay                ; 6BFE A8 
            lda L0779D,y       ; 6BFF B9 9D 77 
            ldx L0779E,y       ; 6C02 BE 9E 77 
L06C05      jmp L07CD5         ; 6C05 4C D5 7C 
L06C08      jsr L06681         ; 6C08 20 81 66 
            jmp L06E20         ; 6C0B 4C 20 6E 
L06C0E      lda #$34           ; 6C0E A9 34 
            ldx #$C7           ; 6C10 A2 C7 
            bne L06C05         ; 6C12 D0 F1 
L06C14      lda #$70           ; 6C14 A9 70 
            ldx #$F0           ; 6C16 A2 F0 
            jsr L07AEC         ; 6C18 20 EC 7A 
            ldx $000E         ; 6C1B A6 0E 
            lda $0076         ; 6C1D A5 76 
            and #$03           ; 6C1F 29 03 
            bne L06C26         ; 6C21 D0 03 
            dec $0200,x       ; 6C23 DE 00 02 
L06C26      rts                ; 6C26 60 
L06C27      ldx #$05           ; 6C27 A2 05 
            jsr L07A38         ; 6C29 20 38 7A 
L06C2C      lda #$70           ; 6C2C A9 70 
            sec                ; 6C2E 38 
            sbc $0001         ; 6C2F E5 01 
L06C31      cmp #$A0           ; 6C31 C9 A0 
            bcc L06C41         ; 6C33 90 0C 
            pha                ; 6C35 48 
            lda #$90           ; 6C36 A9 90 
            jsr L07AEA         ; 6C38 20 EA 7A 
            pla                ; 6C3B 68 
            sec                ; 6C3C 38 
            sbc #$10           ; 6C3D E9 10 
            bne L06C31         ; 6C3F D0 F0 
L06C41      jmp L07AEA         ; 6C41 4C EA 7A 
L06C44      ror                ; 6C44 6A 
            ror $0010         ; 6C45 66 10 
            lsr                ; 6C47 4A 
            ror $0010         ; 6C48 66 10 
            lsr                ; 6C4A 4A 
            ror $0010         ; 6C4B 66 10 
            lsr                ; 6C4D 4A 
            ror $0010         ; 6C4E 66 10 
            sta $0011         ; 6C50 85 11 
            sed                ; 6C52 F8 
            stx $0012         ; 6C53 86 12 
            ldx $0020         ; 6C55 A6 20 
            lda $0010         ; 6C57 A5 10 
            and #$F0           ; 6C59 29 F0 
            clc                ; 6C5B 18 
            adc $0062,x       ; 6C5C 75 62 
            sta $0062,x       ; 6C5E 95 62 
            lda $0011         ; 6C60 A5 11 
            adc $0063,x       ; 6C62 75 63 
            sta $0063,x       ; 6C64 95 63 
            lda #$00           ; 6C66 A9 00 
            adc $0064,x       ; 6C68 75 64 
            sta $0064,x       ; 6C6A 95 64 
            cmp $006A,x       ; 6C6C D5 6A 
            bcc L06C91         ; 6C6E 90 21 
            bne L06C78         ; 6C70 D0 06 
            lda $0063,x       ; 6C72 B5 63 
            cmp $0069,x       ; 6C74 D5 69 
            bcc L06C91         ; 6C76 90 19 
L06C78      clc                ; 6C78 18 
            lda $00F8         ; 6C79 A5 F8 
            adc $0069,x       ; 6C7B 75 69 
            sta $0069,x       ; 6C7D 95 69 
            lda $00F9         ; 6C7F A5 F9 

			.byt $75
L06C82		.byt $6a


            sta $006A,x       ; 6C83 95 6A 
            ldx $001E         ; 6C85 A6 1E 
            lda $006F,x       ; 6C87 B5 6F 
            cmp #$0A           ; 6C89 C9 0A 
            bcs L06C91         ; 6C8B B0 04 
            inc $006F,x       ; 6C8D F6 6F 
            inc $007E         ; 6C8F E6 7E 
L06C91      cld                ; 6C91 D8 
            ldx $0012         ; 6C92 A6 12 
            rts                ; 6C94 60 
L06C95      lda $001E         ; 6C95 A5 1E 
            lsr                ; 6C97 4A 
            ror                ; 6C98 6A 
            sta BankSel        ; 6C99 8D 04 3C 
            rts                ; 6C9C 60 
L06C9D      ldx $0022         ; 6C9D A6 22 
            bne L06CBD         ; 6C9F D0 1C 
            lda $0077         ; 6CA1 A5 77 
            and #$04           ; 6CA3 29 04 
            beq L06CBF         ; 6CA5 F0 18 
L06CA7      stx $0001         ; 6CA7 86 01 
            lda #$70           ; 6CA9 A9 70 
            ldx #$68           ; 6CAB A2 68 
            jsr L07A1F         ; 6CAD 20 1F 7A 
            lda #$70           ; 6CB0 A9 70 
            jsr L07AEA         ; 6CB2 20 EA 7A 
            jsr L07148         ; 6CB5 20 48 71 
            ldy #$07           ; 6CB8 A0 07 
            jsr L07159         ; 6CBA 20 59 71 
L06CBD      clc                ; 6CBD 18 
            rts                ; 6CBE 60 
L06CBF      lda $0023         ; 6CBF A5 23 


			.byt $05
L06CC2		.byt $24



            ora $0025         ; 6CC3 05 25 
            beq L06CA7         ; 6CC5 F0 E0 
            ldy #$00           ; 6CC7 A0 00 
            jsr L07159         ; 6CC9 20 59 71 
            ldx #$00           ; 6CCC A2 00 
            stx $0010         ; 6CCE 86 10 
            stx $0013         ; 6CD0 86 13 
            lda #$A7           ; 6CD2 A9 A7 
            sta $0012         ; 6CD4 85 12 
            ldx #$23           ; 6CD6 A2 23 
L06CD8      lda #$10           ; 6CD8 A9 10 
            sta $0001         ; 6CDA 85 01 
            stx $0011         ; 6CDC 86 11 
            lda ScratchRam,x   ; 6CDE B5 00 
            ora $0001,x       ; 6CE0 15 01 
            ora $0002,x       ; 6CE2 15 02 
            beq L06D40         ; 6CE4 F0 5A 
            lda #$5F           ; 6CE6 A9 5F 
            ldx $0012         ; 6CE8 A6 12 
            jsr L07A1F         ; 6CEA 20 1F 7A 
            lda #$40           ; 6CED A9 40 
            jsr L07AEA         ; 6CEF 20 EA 7A 
            lda $0010         ; 6CF2 A5 10 
            sed                ; 6CF4 F8 
            clc                ; 6CF5 18 
            adc #$01           ; 6CF6 69 01 
            cld                ; 6CF8 D8 
            sta $0010         ; 6CF9 85 10 
            lda #$10           ; 6CFB A9 10 
            sec                ; 6CFD 38 
            ldy #$01           ; 6CFE A0 01 
            jsr L07C95         ; 6D00 20 95 7C 
            lda #$40           ; 6D03 A9 40 
            tax                ; 6D05 AA 
            jsr L07AEC         ; 6D06 20 EC 7A 
            ldy #$00           ; 6D09 A0 00 
            jsr L079F8         ; 6D0B 20 F8 79 
            lda $0011         ; 6D0E A5 11 
            ldy #$03           ; 6D10 A0 03 
            sec                ; 6D12 38 
            jsr L07C95         ; 6D13 20 95 7C 
            ldy #$00           ; 6D16 A0 00 
            jsr L079F8         ; 6D18 20 F8 79 
            jsr L06D42         ; 6D1B 20 42 6D 
            lda $0010         ; 6D1E A5 10 
            cmp #$04           ; 6D20 C9 04 
            bcs L06D30         ; 6D22 B0 0C 
            lda $0012         ; 6D24 A5 12 
            adc #$02           ; 6D26 69 02 
            tax                ; 6D28 AA 
            lda #$B2           ; 6D29 A9 B2 
            ldy #$02           ; 6D2B A0 02 
            jsr L07C71         ; 6D2D 20 71 7C 
L06D30      lda $0012         ; 6D30 A5 12 
            sec                ; 6D32 38 
            sbc #$08           ; 6D33 E9 08 
            sta $0012         ; 6D35 85 12 
            ldx $0011         ; 6D37 A6 11 
            inx                ; 6D39 E8 
            inx                ; 6D3A E8 
            inx                ; 6D3B E8 
            cpx #$41           ; 6D3C E0 41 
            bcc L06CD8         ; 6D3E 90 98 
L06D40      sec                ; 6D40 38 
            rts                ; 6D41 60 
L06D42      jsr L06D48         ; 6D42 20 48 6D 
            jsr L06D48         ; 6D45 20 48 6D 
L06D48      ldy $0013         ; 6D48 A4 13 
            jsr L066C5         ; 6D4A 20 C5 66 
            inc $0013         ; 6D4D E6 13 
            rts                ; 6D4F 60 
L06D50      ldx #$18           ; 6D50 A2 18 
L06D52      lda $0200,x       ; 6D52 BD 00 02 
            beq L06D5A         ; 6D55 F0 03 
            dex                ; 6D57 CA 
            bpl L06D52         ; 6D58 10 F8 
L06D5A      rts                ; 6D5A 60 
L06D5B      lda $0219         ; 6D5B AD 19 02 
            cmp #$A2           ; 6D5E C9 A2 
            bcs L06D9D         ; 6D60 B0 3B 
            ldx #$07           ; 6D62 A2 07 
L06D64      lda PKY_RANDOM         ; 6D64 AD 0A 2C 
            and #$07           ; 6D67 29 07 
            eor $0005         ; 6D69 45 05 
            asl                ; 6D6B 0A 
            sta PageOne,x       ; 6D6C 9D 00 01 
            lda $0006         ; 6D6F A5 06 
            rol                ; 6D71 2A 
            sta $0110,x       ; 6D72 9D 10 01 
            lda PKY_RANDOM         ; 6D75 AD 0A 2C 
            and #$07           ; 6D78 29 07 
            eor $0007         ; 6D7A 45 07 
            asl                ; 6D7C 0A 
            sta $0108,x       ; 6D7D 9D 08 01 
            lda $0008         ; 6D80 A5 08 
            rol                ; 6D82 2A 
            sta $0118,x       ; 6D83 9D 18 01 
            lda PKY_RANDOM         ; 6D86 AD 0A 2C 
            and #$0F           ; 6D89 29 0F 
            adc #$F8           ; 6D8B 69 F8 
            sta $0120,x       ; 6D8D 9D 20 01 
            jsr L06E15         ; 6D90 20 15 6E 
            sta $00DF,x       ; 6D93 95 DF 
            jsr L06E15         ; 6D95 20 15 6E 
            sta $00E7,x       ; 6D98 95 E7 
            dex                ; 6D9A CA 
            bpl L06D64         ; 6D9B 10 C7 
L06D9D      lda #$07           ; 6D9D A9 07 
            sta $0012         ; 6D9F 85 12 
            sta $0015         ; 6DA1 85 15 
L06DA3      ldx $0015         ; 6DA3 A6 15 
            lda $0219         ; 6DA5 AD 19 02 
            jsr L070B6         ; 6DA8 20 B6 70 
            and #$F0           ; 6DAB 29 F0 
            adc #$60           ; 6DAD 69 60 
            sta $0009         ; 6DAF 85 09 
            lda $00EF,x       ; 6DB1 B5 EF 
            adc $0120,x       ; 6DB3 7D 20 01 
            sta $00EF,x       ; 6DB6 95 EF 
            lsr                ; 6DB8 4A 
            lsr                ; 6DB9 4A 
            pha                ; 6DBA 48 
            lsr                ; 6DBB 4A 
            lsr                ; 6DBC 4A 
            lsr                ; 6DBD 4A 
            and #$04           ; 6DBE 29 04 
            sta $000A         ; 6DC0 85 0A 
            ora $0009         ; 6DC2 05 09 
            sta $0009         ; 6DC4 85 09 
            jsr L06DF6         ; 6DC6 20 F6 6D 
            sta $0006         ; 6DC9 85 06 
            lda $0007         ; 6DCB A5 07 
            sta $0005         ; 6DCD 85 05 
            txa                ; 6DCF 8A 
            clc                ; 6DD0 18 
            adc #$08           ; 6DD1 69 08 
            tax                ; 6DD3 AA 
            jsr L06DF6         ; 6DD4 20 F6 6D 
            jsr L06C27         ; 6DD7 20 27 6C 
            pla                ; 6DDA 68 
            and #$1E           ; 6DDB 29 1E 
            tax                ; 6DDD AA 
            lda VROM506A,x       ; 6DDE BD 6A 50 
            asl                ; 6DE1 0A 
            sta $0010         ; 6DE2 85 10 
            lda VROM506B,x       ; 6DE4 BD 6B 50 
            rol                ; 6DE7 2A 
            eor #$C0           ; 6DE8 49 C0 
            sta $0011         ; 6DEA 85 11 
            ldx #$00           ; 6DEC A2 00 
            jsr L06221         ; 6DEE 20 21 62 
            dec $0015         ; 6DF1 C6 15 
            bpl L06DA3         ; 6DF3 10 AE 
            rts                ; 6DF5 60 
L06DF6      ldy #$00           ; 6DF6 A0 00 
            lda $00DF,x       ; 6DF8 B5 DF 
            bpl L06DFD         ; 6DFA 10 01 
            dey                ; 6DFC 88 
L06DFD      clc                ; 6DFD 18 
            adc PageOne,x       ; 6DFE 7D 00 01 
            sta PageOne,x       ; 6E01 9D 00 01 
            sta $0007         ; 6E04 85 07 
            tya                ; 6E06 98 
            adc $0110,x       ; 6E07 7D 10 01 
            and #$07           ; 6E0A 29 07 
            sta $0110,x       ; 6E0C 9D 10 01 
            lsr                ; 6E0F 4A 
            sta $0008         ; 6E10 85 08 
            ror $0007         ; 6E12 66 07 
            rts                ; 6E14 60 
L06E15      lda PKY_RANDOM         ; 6E15 AD 0A 2C 
            and #$07           ; 6E18 29 07 
            clc                ; 6E1A 18 
            adc #$FC           ; 6E1B 69 FC 
            adc #$00           ; 6E1D 69 00 
            rts                ; 6E1F 60 
L06E20      lda $008B         ; 6E20 A5 8B 
            cmp #$C0           ; 6E22 C9 C0 
            bcs L06E70         ; 6E24 B0 4A 
            adc #$06           ; 6E26 69 06 
            sta $008B         ; 6E28 85 8B 
            lda PKY_RANDOM         ; 6E2A AD 0A 2C 
            and #$04           ; 6E2D 29 04 
            sta $0009         ; 6E2F 85 09 
            lda PKY_RANDOM         ; 6E31 AD 0A 2C 
            and #$04           ; 6E34 29 04 
            sta $000A         ; 6E36 85 0A 
            lda PKY_RANDOM         ; 6E38 AD 0A 2C 
            and #$80           ; 6E3B 29 80 
            sta $0012         ; 6E3D 85 12 
            lda #$09           ; 6E3F A9 09 
            sta $0015         ; 6E41 85 15 
            lda #$4D           ; 6E43 A9 4D 
            ldx #$87           ; 6E45 A2 87 
            sta $0011         ; 6E47 85 11 
            stx $0010         ; 6E49 86 10 
            ldy #$00           ; 6E4B A0 00 
            ldx #$00           ; 6E4D A2 00 
L06E4F      jsr L06E96         ; 6E4F 20 96 6E 
            lda PKY_RANDOM         ; 6E52 AD 0A 2C 
            lsr                ; 6E55 4A 
            lda #$00           ; 6E56 A9 00 
            bcc L06E5C         ; 6E58 90 02 
            lda #$70           ; 6E5A A9 70 
L06E5C      ora $0009         ; 6E5C 05 09 
            sta $0009         ; 6E5E 85 09 
            jsr L06E96         ; 6E60 20 96 6E 
            lda $0009         ; 6E63 A5 09 
            and #$04           ; 6E65 29 04 
            sta $0009         ; 6E67 85 09 
            dec $0015         ; 6E69 C6 15 
            bpl L06E4F         ; 6E6B 10 E2 
            jsr L06E96         ; 6E6D 20 96 6E 
L06E70      jsr L06EA5         ; 6E70 20 A5 6E 
            jsr L06221         ; 6E73 20 21 62 
            lda $008B         ; 6E76 A5 8B 
            and #$F0           ; 6E78 29 F0 
            cmp #$60           ; 6E7A C9 60 
            bcs L06E80         ; 6E7C B0 02 
            lda #$60           ; 6E7E A9 60 
L06E80      ora $0009         ; 6E80 05 09 
            sta $0009         ; 6E82 85 09 
            ldx #$07           ; 6E84 A2 07 
            jsr L06E96         ; 6E86 20 96 6E 
            bit ThrustSwitch   ; 6E89 2C 05 24 
            bpl L06EC5         ; 6E8C 10 37 
            lda $0076         ; 6E8E A5 76 
            and #$04           ; 6E90 29 04 
            beq L06EC5         ; 6E92 F0 31 
            ldx #$01           ; 6E94 A2 01 
L06E96      tya                ; 6E96 98 
            sec                ; 6E97 38 
            adc $0010         ; 6E98 65 10 
            sta $0010         ; 6E9A 85 10 
            lda #$00           ; 6E9C A9 00 
            adc $0011         ; 6E9E 65 11 
            sta $0011         ; 6EA0 85 11 
            jmp L06221         ; 6EA2 4C 21 62 
L06EA5      lda $0079         ; 6EA5 A5 79 
            sec                ; 6EA7 38 
            sbc #$40           ; 6EA8 E9 40 
            jsr L06EC6         ; 6EAA 20 C6 6E 
            tya                ; 6EAD 98 
            lsr                ; 6EAE 4A 
            tay                ; 6EAF A8 
            ldx #$53           ; 6EB0 A2 53 
            lda VROM53BC,y       ; 6EB2 B9 BC 53 
            asl                ; 6EB5 0A 
            bcc L06EBA         ; 6EB6 90 02 
            inx                ; 6EB8 E8 
            clc                ; 6EB9 18 
L06EBA      adc #$C6           ; 6EBA 69 C6 
            sta $0010         ; 6EBC 85 10 
            txa                ; 6EBE 8A 
            adc #$00           ; 6EBF 69 00 
            sta $0011         ; 6EC1 85 11 
            ldx #$00           ; 6EC3 A2 00 
L06EC5      rts                ; 6EC5 60 
L06EC6      lsr                ; 6EC6 4A 
            sta $0010         ; 6EC7 85 10 
            lsr                ; 6EC9 4A 
            lsr                ; 6ECA 4A 
            lsr                ; 6ECB 4A 
            lsr                ; 6ECC 4A 
            tay                ; 6ECD A8 
            lda L06EEE,y       ; 6ECE B9 EE 6E 
            asl                ; 6ED1 0A 
            sta $0012         ; 6ED2 85 12 
            lda $0010         ; 6ED4 A5 10 
            and #$0E           ; 6ED6 29 0E 
            bcc L06EDE         ; 6ED8 90 04 
            eor #$0E           ; 6EDA 49 0E 
            adc #$01           ; 6EDC 69 01 
L06EDE      tay                ; 6EDE A8 
            lda $0012         ; 6EDF A5 12 
            and #$04           ; 6EE1 29 04 
            sta $0009         ; 6EE3 85 09 
            asl $0012         ; 6EE5 06 12 
            lda $0012         ; 6EE7 A5 12 
            and #$04           ; 6EE9 29 04 
            sta $000A         ; 6EEB 85 0A 
            rts                ; 6EED 60 
L06EEE      brk                ; 6EEE 00 
            .byt $A3	; 6EEF A3 
            .byt $22	; 6EF0 22 
            sta ($0003,x)     ; 6EF1 81 03 
            ldy #$21           ; 6EF3 A0 21 
            .byt $82	; 6EF5 82 
L06EF6      lsr $0010         ; 6EF6 46 10 
            ldx $0022         ; 6EF8 A6 22 
            beq L06F55         ; 6EFA F0 59 
            ldx $00A2         ; 6EFC A6 A2 
            bne L06F18         ; 6EFE D0 18 
            lda $007E         ; 6F00 A5 7E 
            beq L06F0D         ; 6F02 F0 09 
            ldy #$2F           ; 6F04 A0 2F 
            jsr L07716         ; 6F06 20 16 77 
            stx $007E         ; 6F09 86 7E 
            beq L06F18         ; 6F0B F0 0B 
L06F0D      lda $00DD         ; 6F0D A5 DD 
            beq L06F18         ; 6F0F F0 07 
            ldy #$5F           ; 6F11 A0 5F 
            jsr L07716         ; 6F13 20 16 77 
            stx $00DD         ; 6F16 86 DD 
L06F18      lda $0219         ; 6F18 AD 19 02 
            bmi L06F31         ; 6F1B 30 14 
            beq L06F31         ; 6F1D F0 12 
            asl ThrustSwitch   ; 6F1F 0E 05 24 
            ror $0010         ; 6F22 66 10 
            lda $02E7         ; 6F24 AD E7 02 
            beq L06F31         ; 6F27 F0 08 
            lda $0083         ; 6F29 A5 83 
            beq L06F38         ; 6F2B F0 0B 
            dec $0083         ; 6F2D C6 83 
            bne L06F4D         ; 6F2F D0 1C 
L06F31      lda $02ED         ; 6F31 AD ED 02 
            sta $0084         ; 6F34 85 84 
            bpl L06F4D         ; 6F36 10 15 
L06F38      dec $0084         ; 6F38 C6 84 
            bne L06F4D         ; 6F3A D0 11 
            lda #$04           ; 6F3C A9 04 
            sta $0083         ; 6F3E 85 83 
            inc $0082         ; 6F40 E6 82 
            lda $0082         ; 6F42 A5 82 
            and #$01           ; 6F44 29 01 
            tax                ; 6F46 AA 
            ldy L06F60,x       ; 6F47 BC 60 6F 
            jsr L07713         ; 6F4A 20 13 77 
L06F4D      lda $007F         ; 6F4D A5 7F 
            tax                ; 6F4F AA 
            and #$3F           ; 6F50 29 3F 
            beq L06F55         ; 6F52 F0 01 
            dex                ; 6F54 CA 
L06F55      stx $007F         ; 6F55 86 7F 
            stx ExplosionDat   ; 6F57 8E 00 36 
            lda $0010         ; 6F5A A5 10 
            sta ThrustSnd      ; 6F5C 8D 03 3C 
            rts                ; 6F5F 60 
L06F60      .byt $07	; 6F60 07 
            .byt $0F	; 6F61 0F 
L06F62      stx $000E         ; 6F62 86 0E 
            lda #$50           ; 6F64 A9 50 
            sta $02EA         ; 6F66 8D EA 02 
            lda $0200,y       ; 6F69 B9 00 02 
            and #$78           ; 6F6C 29 78 
            asl                ; 6F6E 0A 
            bpl L06F74         ; 6F6F 10 03 
            jmp VectorROM      ; 6F71 4C 00 48 
L06F74      lsr                ; 6F74 4A 
            sta $000F         ; 6F75 85 0F 
            lda $0200,y       ; 6F77 B9 00 02 
            and #$07           ; 6F7A 29 07 
            lsr                ; 6F7C 4A 
            tax                ; 6F7D AA 
            beq L06F82         ; 6F7E F0 02 
            ora $000F         ; 6F80 05 0F 
L06F82      sta $0200,y       ; 6F82 99 00 02 
            lda $0022         ; 6F85 A5 22 
            beq L06F98         ; 6F87 F0 0F 
            lda $000E         ; 6F89 A5 0E 
            beq L06F91         ; 6F8B F0 04 
            cmp #$04           ; 6F8D C9 04 
            bcc L06F98         ; 6F8F 90 07 

L06F91		.byt $bd, $75
L06F93		.byt $4d


            clc                ; 6F94 18 
            jsr L06C44         ; 6F95 20 44 6C 
L06F98      ldx $0200,y       ; 6F98 BE 00 02 
            beq L06FD1         ; 6F9B F0 34 
            jsr L06D50         ; 6F9D 20 50 6D 
            bmi L06FD1         ; 6FA0 30 2F 
            inc $02E7         ; 6FA2 EE E7 02 
            jsr L061EB         ; 6FA5 20 EB 61 
            jsr L06A3B         ; 6FA8 20 3B 6A 
            lda $0221,x       ; 6FAB BD 21 02 
            and #$1F           ; 6FAE 29 1F 
            asl                ; 6FB0 0A 
            eor $02A5,x       ; 6FB1 5D A5 02 
            sta $02A5,x       ; 6FB4 9D A5 02 
            jsr L06D52         ; 6FB7 20 52 6D 
            bmi L06FD1         ; 6FBA 30 15 
            inc $02E7         ; 6FBC EE E7 02 
            jsr L061EB         ; 6FBF 20 EB 61 
            jsr L06A3B         ; 6FC2 20 3B 6A 
            lda $0242,x       ; 6FC5 BD 42 02 
            and #$1F           ; 6FC8 29 1F 
            asl                ; 6FCA 0A 
            eor $02C6,x       ; 6FCB 5D C6 02 
            sta $02C6,x       ; 6FCE 9D C6 02 
L06FD1      ldx $000E         ; 6FD1 A6 0E 
            rts                ; 6FD3 60 
L06FD4      lda $0022         ; 6FD4 A5 22 
            bpl L07025         ; 6FD6 10 4D 
            sta $0077         ; 6FD8 85 77 
            sta $0042         ; 6FDA 85 42 
            sta $0043         ; 6FDC 85 43 
            sta $00CE         ; 6FDE 85 CE 
            jsr L07844         ; 6FE0 20 44 78 
            ldx #$03           ; 6FE3 A2 03 
L06FE5      ldy #$00           ; 6FE5 A0 00 
L06FE7      lda $0023,y       ; 6FE7 B9 23 00 
            cmp $0062,x       ; 6FEA D5 62 
            lda $0024,y       ; 6FEC B9 24 00 
            sbc $0063,x       ; 6FEF F5 63 
            lda $0025,y       ; 6FF1 B9 25 00 
            sbc $0064,x       ; 6FF4 F5 64 
            bcc L07026         ; 6FF6 90 2E 
            iny                ; 6FF8 C8 
            iny                ; 6FF9 C8 
            iny                ; 6FFA C8 
            cpy #$1E           ; 6FFB C0 1E 
            bcc L06FE7         ; 6FFD 90 E8 
L06FFF      dex                ; 6FFF CA 
            dex                ; 7000 CA 
            dex                ; 7001 CA 
            bpl L06FE5         ; 7002 10 E1 
            lda $0043         ; 7004 A5 43 
            bmi L07016         ; 7006 30 0E 
            cmp $0042         ; 7008 C5 42 
            bcc L07016         ; 700A 90 0A 
            adc #$02           ; 700C 69 02 
            cmp #$1E           ; 700E C9 1E 
            bcc L07014         ; 7010 90 02 
            lda #$FF           ; 7012 A9 FF 
L07014      sta $0043         ; 7014 85 43 
L07016      ldx $00CE         ; 7016 A6 CE 
            bmi L0701F         ; 7018 30 05 
            ldy #$4F           ; 701A A0 4F 
            jsr L07716         ; 701C 20 16 77 
L0701F      lda #$00           ; 701F A9 00 
            sta $0022         ; 7021 85 22 
            sta $0041         ; 7023 85 41 
L07025      rts                ; 7025 60 
L07026      stx $000C         ; 7026 86 0C 
            txa                ; 7028 8A 
            lsr                ; 7029 4A 
            tax                ; 702A AA 
            sty $0042,x       ; 702B 94 42 
            sty $0010         ; 702D 84 10 
            cpy $00CE         ; 702F C4 CE 
            bcs L07059         ; 7031 B0 26 
            sty $00CE         ; 7033 84 CE 
            lda #$00           ; 7035 A9 00 
            sta $0011         ; 7037 85 11 
            tya                ; 7039 98 
L0703A      sec                ; 703A 38 
            sbc #$03           ; 703B E9 03 
            bcc L07043         ; 703D 90 04 
            inc $0011         ; 703F E6 11 
            bne L0703A         ; 7041 D0 F7 
L07043      lda $0011         ; 7043 A5 11 
            eor #$FF           ; 7045 49 FF 
            adc #$04           ; 7047 69 04 
            sta $0011         ; 7049 85 11 
            asl $0011         ; 704B 06 11 
            adc $0011         ; 704D 65 11 
            asl $0011         ; 704F 06 11 
            adc $0011         ; 7051 65 11 
            sta $00CC         ; 7053 85 CC 
            lda #$14           ; 7055 A9 14 
            sta $00CB         ; 7057 85 CB 
L07059      ldx #$1B           ; 7059 A2 1B 
L0705B      cpx $0010         ; 705B E4 10 
            beq L0707C         ; 705D F0 1D 
            lda $0041,x       ; 705F B5 41 
            sta $0044,x       ; 7061 95 44 
            lda $0042,x       ; 7063 B5 42 
            sta $0045,x       ; 7065 95 45 
            lda $0043,x       ; 7067 B5 43 
            sta $0046,x       ; 7069 95 46 
            lda $0020,x       ; 706B B5 20 
            sta $0023,x       ; 706D 95 23 
            lda $0021,x       ; 706F B5 21 
            sta $0024,x       ; 7071 95 24 
            lda $0022,x       ; 7073 B5 22 
            sta $0025,x       ; 7075 95 25 
            dex                ; 7077 CA 
            dex                ; 7078 CA 
            dex                ; 7079 CA 
            bne L0705B         ; 707A D0 DF 
L0707C      lda #$0B           ; 707C A9 0B 
            sta $0044,x       ; 707E 95 44 
            lda #$00           ; 7080 A9 00 
            sta $0045,x       ; 7082 95 45 
            sta $0046,x       ; 7084 95 46 
            lda #$F0           ; 7086 A9 F0 
            sta $0077         ; 7088 85 77 
            ldx $000C         ; 708A A6 0C 
            lda $0064,x       ; 708C B5 64 
            sta $0025,y       ; 708E 99 25 00 
            lda $0063,x       ; 7091 B5 63 
            sta $0024,y       ; 7093 99 24 00 
            lda $0062,x       ; 7096 B5 62 
            sta $0023,y       ; 7098 99 23 00 
            jmp L06FFF         ; 709B 4C FF 6F 
            tya                ; 709E 98 
            bpl L070AA         ; 709F 10 09 
            jsr L070B6         ; 70A1 20 B6 70 
            jsr L070AA         ; 70A4 20 AA 70 
            jmp L070B6         ; 70A7 4C B6 70 
L070AA      tay                ; 70AA A8 
            txa                ; 70AB 8A 
            bpl L070BC         ; 70AC 10 0E 
            jsr L070B6         ; 70AE 20 B6 70 
            jsr L070BC         ; 70B1 20 BC 70 
            eor #$80           ; 70B4 49 80 
L070B6      eor #$FF           ; 70B6 49 FF 
            clc                ; 70B8 18 
            adc #$01           ; 70B9 69 01 
            rts                ; 70BB 60 
L070BC      sta $000D         ; 70BC 85 0D 
            tya                ; 70BE 98 
            cmp $000D         ; 70BF C5 0D 
            bcc L070D1         ; 70C1 90 0E 
            ldy $000D         ; 70C3 A4 0D 
            sta $000D         ; 70C5 85 0D 
            tya                ; 70C7 98 
            jsr L070D1         ; 70C8 20 D1 70 
            sec                ; 70CB 38 
            sbc #$40           ; 70CC E9 40 
            jmp L070B6         ; 70CE 4C B6 70 
L070D1      jsr L070EB         ; 70D1 20 EB 70 
            tax                ; 70D4 AA 
            lda L070D9,x       ; 70D5 BD D9 70 
            rts                ; 70D8 60 
L070D9      brk                ; 70D9 00 
            .byt $02	; 70DA 02 
            ora $0007         ; 70DB 05 07 
            asl                ; 70DD 0A 
            .byt $0C	; 70DE 0C 
            .byt $0F	; 70DF 0F 
            ora ($0013),y     ; 70E0 11 13 
            ora $0017,x       ; 70E2 15 17 
            ora L01C1A,y       ; 70E4 19 1A 1C 
            ora L0201F,x       ; 70E7 1D 1F 20 

			.byt $d5
L070EB		.byt $a0


            .byt $04	; 70EC 04 
L070ED      cmp $000D         ; 70ED C5 0D 
            bcc L070F3         ; 70EF 90 02 
            sbc $000D         ; 70F1 E5 0D 
L070F3      rol $000C         ; 70F3 26 0C 
            asl                ; 70F5 0A 
            dey                ; 70F6 88 
            bpl L070ED         ; 70F7 10 F4 
            lda $000C         ; 70F9 A5 0C 
            and #$1F           ; 70FB 29 1F 
            rts                ; 70FD 60 
L070FE      clc                ; 70FE 18 
            adc #$40           ; 70FF 69 40 
L07101      bpl L0710B         ; 7101 10 08 
            and #$7F           ; 7103 29 7F 
            jsr L0710B         ; 7105 20 0B 71 
            jmp L070B6         ; 7108 4C B6 70 
L0710B      cmp #$41           ; 710B C9 41 
            bcc L07113         ; 710D 90 04 
            eor #$7F           ; 710F 49 7F 
            adc #$00           ; 7111 69 00 
L07113      tax                ; 7113 AA 
            lda $4B61,x       ; 7114 BD 61 4B 
            rts                ; 7117 60 




Sub_7118    lda #$04           	; 7118 A9 04 
            sta $0076         	; 711A 85 76 
L0711C      jsr $4BA2         	; 711C 20 A2 4B 
            dec $0076         	; 711F C6 76 
            bne L0711C         	; 7121 D0 F9 
            ldx #$C9           	; 7123 A2 C9 
            lda #$47           	; 7125 A9 47 
            sta $0004         	; 7127 85 04 
            lda #$02           	; 7129 A9 02 
            sta $0003         	; 712B 85 03 
            lda #$C1           	; 712D A9 C1 
            jsr L07CD5         	; 712F 20 D5 7C 
            lda #$BE           	; 7132 A9 BE 
            sta $0009         	; 7134 85 09 
            lda #$75           	; 7136 A9 75 
            sta $000A         	; 7138 85 0A 
            lda #$00           	; 713A A9 00 
            sta $0001         	; 713C 85 01 
            lda #$70           	; 713E A9 70 
            ldx #$20           	; 7140 A2 20 
            jsr L0717F         	; 7142 20 7F 71 
            jmp L079D6         	; 7145 4C D6 79 
L07148      lda #$C5           	; 7148 A9 C5 
            ldx #$C9           	; 714A A2 C9 
            jsr L07CD5         	; 714C 20 D5 7C 
            ldx #$BE           	; 714F A2 BE 
            lda #$75           	; 7151 A9 75 
            sta $000A         	; 7153 85 0A 
            stx $0009         	; 7155 86 09 
            bne L07187         	; 7157 D0 2E 
L07159      lda OptionSwitch21 	; 7159 AD 03 28 
            and #$03           	; 715C 29 03 
            ldx #$10           	; 715E A2 10 
            stx $0001         	; 7160 86 01 
L07162      asl                ; 7162 0A 
            tax                ; 7163 AA 
            lda L071F1,x       ; 7164 BD F1 71 
            sta $000A         ; 7167 85 0A 
            lda L071F0,x       ; 7169 BD F0 71 
            sta $0009         ; 716C 85 09 
            adc ($0009),y     ; 716E 71 09 
            sta $0009         ; 7170 85 09 
            bcc L07176         ; 7172 90 02 
            inc $000A         ; 7174 E6 0A 
L07176      tya                ; 7176 98 
            asl                ; 7177 0A 
            tay                ; 7178 A8 
            lda L071D4,y       ; 7179 B9 D4 71 
            ldx L071D5,y       ; 717C BE D5 71 
L0717F      jsr L07A1F         ; 717F 20 1F 7A 
            lda #$70           ; 7182 A9 70 
            jsr L07AEA         ; 7184 20 EA 7A 
L07187      ldy #$00           ; 7187 A0 00 
            ldx #$00           ; 7189 A2 00 
L0718B      lda ($0009,x)     ; 718B A1 09 
            sta $000C         ; 718D 85 0C 
            lsr                ; 718F 4A 
            lsr                ; 7190 4A 
            jsr L071B0         ; 7191 20 B0 71 
            lda ($0009,x)     ; 7194 A1 09 
            rol                ; 7196 2A 
            rol $000C         ; 7197 26 0C 
            rol                ; 7199 2A 
            lda $000C         ; 719A A5 0C 
            rol                ; 719C 2A 
            asl                ; 719D 0A 
            jsr L071B6         ; 719E 20 B6 71 
            lda ($0009,x)     ; 71A1 A1 09 
            sta $000C         ; 71A3 85 0C 
            jsr L071B0         ; 71A5 20 B0 71 
            lsr $000C         ; 71A8 46 0C 
            bcc L0718B         ; 71AA 90 DF 
L071AC      dey                ; 71AC 88 
            jmp L07A55         ; 71AD 4C 55 7A 
L071B0      inc $0009         ; 71B0 E6 09 
            bne L071B6         ; 71B2 D0 02 
            inc $000A         ; 71B4 E6 0A 
L071B6      and #$3E           ; 71B6 29 3E 
            bne L071BE         ; 71B8 D0 04 
            pla                ; 71BA 68 
            pla                ; 71BB 68 
            bne L071AC         ; 71BC D0 EE 
L071BE      cmp #$0A           ; 71BE C9 0A 
            bcc L071C4         ; 71C0 90 02 
            adc #$0D           ; 71C2 69 0D 
L071C4      tax                ; 71C4 AA 
            lda VROM56F6,x       ; 71C5 BD F6 56 
            sta ($0003),y     ; 71C8 91 03 
            iny                ; 71CA C8 
            lda L056F7,x       ; 71CB BD F7 56 
            sta ($0003),y     ; 71CE 91 03 
            iny                ; 71D0 C8 
            ldx #$00           ; 71D1 A2 00 
            rts                ; 71D3 60 
L071D4      pla                ; 71D4 68 
L071D5      ldx $0072,y       ; 71D5 B6 72 
            ldx $000C,y       ; 71D7 B6 0C 
            tax                ; 71D9 AA 
            .byt $0C	; 71DA 0C 
            ldx #$0C           ; 71DB A2 0C 
            txs                ; 71DD 9A 
            .byt $0C	; 71DE 0C 
            .byt $92	; 71DF 92 
            pla                ; 71E0 68 
            dec $006E         ; 71E1 C6 6E 
            .byt $A7	; 71E3 A7 
            lsr $0042,x       ; 71E4 56 42 
            .byt $5A	; 71E6 5A 
            .byt $42	; 71E7 42 
            cli                ; 71E8 58 
            .byt $42	; 71E9 42 
            pla                ; 71EA 68 
            .byt $32	; 71EB 32 
            .byt $64	; 71EC 64 
            dec $006E         ; 71ED C6 6E 
            .byt $D2	; 71EF D2 
L071F0      sed                ; 71F0 F8 
L071F1      adc ($00AE),y     ; 71F1 71 AE 
            .byt $72	; 71F3 72 
            adc L03873,x       ; 71F4 7D 73 38 
            .byt $74	; 71F7 74 
            asl L01C16         ; 71F8 0E 16 1C 
            .byt $32	; 71FB 32 
            .byt $44	; 71FC 44 
		
			.byt $58, $70, $78, $7e, $8a, $94, $a0, $a6, $b0, $63
			.byt $56, $60, $6e, $3c, $ec, $4d, $c0, $a4, $0a, $ea
			.byt $6c, $08, $00, $ec, $f2, $b0, $6e, $3c, $ec, $48
			.byt $5a, $b8, $66, $92, $42, $9a, $82, $c3, $12, $0e
			.byt $12, $90, $4c, $4d, $f1, $a4, $12, $2d, $d2, $0a
			.byt $64, $c2, $6c, $0f, $66, $cd, $82, $6c, $9a, $c3
			.byt $4a, $85, $c0, $a5, $92, $bd, $c2, $b4, $f0, $2e
			.byt $12, $0e, $26, $0d, $d2, $82, $4e, $c0, $60, $4e
			.byt $30, $4d, $80, $a5, $92, $bd, $c2, $bb, $1a



			.byt $4c, $10, $0e, $d8, $4c, $82, $82, $70, $c2, $6c
			.byt $0b, $6e, $09, $e6, $b5, $92, $3e, $00, $a5, $92
			.byt $bd, $c2, $be, $0a, $b6, $00, $59, $62, $48, $66
			.byt $d2, $6d, $18, $4e
			
			
L07278		.byt $9b, $64, $09, $02, $3d, $92
			.byt $43, $70, $b8, $00, $18, $4e, $9b, $64, $08, $c2


			.byt $3d, $92, $43, $71, $20, $4e, $9b, $64, $b8, $46
			.byt $09, $ec, $4a, $1a, $c0, $00, $3d, $92, $43, $70
			.byt $b8, $40, $20, $56, $2c, $52, $0c, $5a, $93, $62
			.byt $cc, $40, $34, $e4, $cd, $c2, $2e, $03, $0d, $17
			.byt $1d, $37, $4f, $67, $7d, $8b, $91, $9d, $a9, $b5
			.byt $bb, $64, $d2, $3b, $2e, $c2, $6c, $5a, $4c, $93
			.byt $6f, $bd, $1a, $4c, $12, $b0, $40, $6b, $2c, $0a  
			.byt $6c, $5a, $4c, $93, $6e, $0b, $6e, $c0, $52, $6c
			.byt $92, $b8, $50, $4d, $82, $f2, $58, $90, $4c, $4d
			.byt $f0, $4c, $80, $33, $70, $c2, $42, $5a, $4c, $4c 
			.byt $82, $bb, $52, $0b, $58, $b2, $42, $6c, $9a, $c3

			.byt $4a, $82, $64, $0a, $5a, $90, $00, $f6, $6c, $09
			.byt $b2, $3b, $2e, $c1, $4c, $4c, $b6, $2b, $20, $0d
			.byt $a6, $c1, $70, $48, $50, $b6, $52, $38, $d2, $90
			.byt $00, $da, $64, $90, $4c, $c9, $d8, $8e, $0a, $32
			.byt $42, $9b, $c2, $bb, $1a, $4c, $10, $0a, $2c, $ca
			.byt $4e, $7a, $65, $be, $0a, $b6, $1e, $94, $d2, $a2
			.byt $92, $0a, $2c, $ca, $4e, $7a, $65, $bd, $1a, $4c	
			.byt $12, $92, $13, $18, $62, $ca, $64, $f2, $42, $20
			.byt $6e, $a3, $52, $82, $40, $18, $62, $ca, $64, $f2	
	
			.byt $42, $18, $6e, $a3, $52, $80, $00, $20, $62, $ca
			.byt $64, $f2, $64, $08, $c2, $bd, $1a, $4c, $00, $7d
			.byt $92, $43, $70, $48, $40, $5a, $60, $42, $5a, $96
			.byt $f2, $b2, $82, $56, $52, $b0, $7c, $da, $5a, $0d
			.byt $e8, $6a, $60, $48, $00, $0d, $17, $1b, $33, $43 
 			.byt $59, $71, $7d, $87, $93, $9f, $a8, $b1, $8a, $5a
			.byt $84, $12, $cd, $82, $b9, $e6, $b2, $40, $74, $f2
			.byt $4d, $83, $d4, $f0, $b2, $42, $b9, $e6, $b2, $42
			.byt $4d, $f0, $0e, $64, $0a, $12, $b8, $46, $10, $62
			.byt $4b, $60, $82, $72, $b5, $c0, $be, $a8, $0a, $64
			.byt $c5, $92, $f0, $74, $9d, $c2, $6c, $9a, $c3, $4a


 

            .byt $82	; 73BE 82 
            .byt $6F	; 73BF 6F

			.byt $a4, $f2, $bd, $d2, $f0, $6c, $9e, $0a, $c2, $42
			.byt $a4, $f2, $b0, $74, $9d, $c2, $6c, $9a, $c3, $4a			
			.byt $82, $6f, $a4, $f2, $bd, $d2, $f0, $6e, $63, $52
			.byt $82, $02, $ae, $4a, $92, $02, $82, $70, $c5, $92
			.byt $09, $e6, $b5, $92, $3e, $13, $2d, $28, $cf, $52
			.byt $b0, $6e, $cd, $82, $be, $0a, $b6, $00, $33, $64
			.byt $0a, $12, $0d, $0a, $b6, $1a, $48, $00, $18, $68
			.byt $6a, $4e, $48, $48, $0b, $a6, $ca, $72, $b5, $c0						
			.byt $18, $68, $6a, $4e, $48, $46, $0b, $a6, $ca, $72
			.byt $b0, $00, $20, $68, $6a, $4e, $4d, $c2, $18, $5c
			.byt $9e, $52, $cd, $80, $3d, $92, $43, $70, $b8, $40
			.byt $20, $5c, $4e, $78, $0c, $5a, $93, $62, $cc, $40
			.byt $0d, $13, $19, $33, $47, $61, $6b, $73, $7d, $89
			.byt $93, $9f, $a5, $b2, $4e, $9d, $90, $b8, $00, $76
			.byt $56, $2a, $26, $b0, $40, $be, $42, $a6, $64, $c1
			.byt $5c, $48, $52, $be, $0a, $0a, $64, $c5, $92, $0c


			.byt $26, $b8, $50, $6a, $7c, $0c, $52, $74, $ec, $4d
			.byt $c0, $a4, $ec, $0a, $8a, $d4, $ec, $0a, $64, $c5
			.byt $92, $0d, $f2, $b8, $5a, $93, $4e, $69, $60, $4d
			.byt $c0, $9d, $2c, $6c, $4a, $0d, $a6, $c1, $70, $48


			.byt $68, $2d, $8a, $0d, $d2, $82, $4e, $4b
			.byt $66, $91, $6c, $0c, $0a, $0c, $12, $c5
			.byt $8b, $9d, $2c, $6c, $4a, $0d, $d8, $6a
			.byt $60, $40, $00, $a6, $60, $b9, $6c, $0d
			.byt $f0, $2d, $b1, $76, $52, $5c, $c2, $c2
			.byt $6c, $8b, $64, $2a, $27, $18, $54, $69
			.byt $d8, $28, $48, $0b, $b2, $4a, $e6, $b8
			.byt $00, $18, $54, $69, $d8, $28, $46, $0b
			.byt $b2, $4a, $e7, $20, $54, $69, $d8, $2d
			.byt $c2, $18, $5c, $ca, $56, $98, $00, $3d
			.byt $92, $43, $70, $9d, $c3, $20, $5c, $ca
			.byt $56, $2d, $c2, $8b, $64, $6c, $67



			
L074E7		bit $73
            bpl L07514         ; 74E9 10 29 
            txa                ; 74EB 8A 
            beq L074F5         ; 74EC F0 07 
            cpy #$19           ; 74EE C0 19 
            bne L07514         ; 74F0 D0 22 
            adc #$18           ; 74F2 69 18 
            tay                ; 74F4 A8 
L074F5      lda $0200,y       ; 74F5 B9 00 02 
            cpy #$1A           ; 74F8 C0 1A 
            beq L07516         ; 74FA F0 1A 
            bcc L07528         ; 74FC 90 2A 
            lda #$00           ; 74FE A9 00 
            sta $0200,y       ; 7500 99 00 02 
            ldx #$E1           ; 7503 A2 E1 
L07505      pla                ; 7505 68 
            pla                ; 7506 68 
L07507      txa                ; 7507 8A 
            adc $02EF         ; 7508 6D EF 02 
            bcs L07511         ; 750B B0 04 
            lda #$00           ; 750D A9 00 
            sta $0073         ; 750F 85 73 
L07511      sta $02EF         ; 7511 8D EF 02 
L07514      clc                ; 7514 18 
            rts                ; 7515 60 
L07516      ldx #$80           ; 7516 A2 80 
            cpx $02EF         ; 7518 EC EF 02 
            bcc L07523         ; 751B 90 06 
            ldx #$00           ; 751D A2 00 
            ldy #$1A           ; 751F A0 1A 
            clc                ; 7521 18 
            rts                ; 7522 60 
L07523      jsr L07507         ; 7523 20 07 75 
            sec                ; 7526 38 
            rts                ; 7527 60 
L07528      ldx #$F8           ; 7528 A2 F8 
            sec                ; 752A 38 
            ror $0071         ; 752B 66 71 
            cpy $0072         ; 752D C4 72 
            beq L07505         ; 752F F0 D4 
            sty $0072         ; 7531 84 72 
            asl                ; 7533 0A 
            and #$86           ; 7534 29 86 
            sta $0010         ; 7536 85 10 
            bmi L07554         ; 7538 30 1A 
            beq L07554         ; 753A F0 18 
            lda $0221,y       ; 753C B9 21 02 
            cmp #$80           ; 753F C9 80 
            ror                ; 7541 6A 
            jsr L06A62         ; 7542 20 62 6A 
            sta $0221,y       ; 7545 99 21 02 
            lda $0242,y       ; 7548 B9 42 02 
            cmp #$80           ; 754B C9 80 
            ror                ; 754D 6A 
            jsr L06A62         ; 754E 20 62 6A 
            sta $0242,y       ; 7551 99 42 02 
L07554      ldx #$00           ; 7554 A2 00 
            jsr L07567         ; 7556 20 67 75 
            ldx #$21           ; 7559 A2 21 
            tya                ; 755B 98 
            clc                ; 755C 18 
            adc #$21           ; 755D 69 21 
            tay                ; 755F A8 
            jsr L07567         ; 7560 20 67 75 
            ldx #$B0           ; 7563 A2 B0 
            bne L07505         ; 7565 D0 9E 
L07567      stx $0011         ; 7567 86 11 
            lda $0221,y       ; 7569 B9 21 02 
            bmi L07581         ; 756C 30 13 
            lda $023A,x       ; 756E BD 3A 02 
            bmi L0758B         ; 7571 30 18 
            jsr L075B1         ; 7573 20 B1 75 
            bcc L0758B         ; 7576 90 13 
L07578      lda #$00           ; 7578 A9 00 
            sec                ; 757A 38 
            sbc $0221,y       ; 757B F9 21 02 
            jmp L0759A         ; 757E 4C 9A 75 
L07581      lda $023A,x       ; 7581 BD 3A 02 
            bpl L0758B         ; 7584 10 05 
            jsr L075B1         ; 7586 20 B1 75 
            bcc L07578         ; 7589 90 ED 
L0758B      lda $0221,y       ; 758B B9 21 02 
            bit $0010         ; 758E 24 10 
            bpl L07599         ; 7590 10 07 
            cmp #$80           ; 7592 C9 80 
            ror                ; 7594 6A 
            adc $0221,y       ; 7595 79 21 02 
            lsr                ; 7598 4A 
L07599      rol                ; 7599 2A 
L0759A      jsr L068EA         ; 759A 20 EA 68 
            bpl L075A5         ; 759D 10 06 
            cmp #$FB           ; 759F C9 FB 
            bcc L075AB         ; 75A1 90 08 
            lda #$FA           ; 75A3 A9 FA 
L075A5      cmp #$06           ; 75A5 C9 06 
            bcs L075AB         ; 75A7 B0 02 
            lda #$06           ; 75A9 A9 06 
L075AB      ldx $0011         ; 75AB A6 11 
            sta $023A,x       ; 75AD 9D 3A 02 
            rts                ; 75B0 60 
L075B1      lda $02A5,y       ; 75B1 B9 A5 02 
            cmp $02BE,x       ; 75B4 DD BE 02


; -------------------------------------------------------------------------------------------
 
L075B7      lda $0263,y       ; 75B7 B9 63 02 
            sbc $027C,x       ; 75BA FD 7C 02 
            rts                ; 75BD 60 

			.byt $1f, $c4

x7590		.byt $09, $70, $2d, $9a, $0b, $64, $38, $00
			.byt $00, $00, $00, $00, $00, $00, $01, $08
			.byt $00, $00, $00, $00, $00, $00, $0f, $08
			.byt $00, $00, $00, $00, $16, $21, $00, $00
x75b0		.byt $00, $00, $2f, $36, $00, $00, $00, $00 
			.byt $3d, $44, $00, $00, $00, $00, $00, $00 
			.byt $00, $00, $00, $00, $00, $00, $4b, $52
			.byt $00, $00, $00, $00, $28, $21, $00, $00
x75d0		.byt $00, $00, $5d, $64, $00, $00, $00, $00
			.byt $00, $00, $00, $00, $6f, $7a, $85, $7a
			.byt $90, $a5, $00, $00, $00, $00, $90, $b6
			.byt $d3, $de, $00, $00, $00, $00, $00, $00 
x75f0		.byt $00, $00, $00, $00, $00, $00, $c1, $cc
			.byt $00, $01, $04, $e0, $03, $05, $00, $00
x7600		.byt $01, $04, $a8, $ff, $05, $00, $00, $01
			.byt $04, $d0, $03, $05, $00, $00, $7f, $01
			.byt $04, $01, $10, $01, $14, $ff, $10, $00 
			.byt $00, $7f, $20, $a2, $00, $01, $00, $00
x7620		.byt $7f, $01, $10, $01, $20, $00, $00, $01
			.byt $02, $0c, $01, $18, $00, $00, $01, $10
			.byt $a4, $ff, $03, $00, $00, $01, $01, $04 
			.byt $01, $40, $00, $00, $01, $10, $a4, $ff
x7640		.byt $04, $00, $00, $01, $f0, $06, $00, $02
			.byt $00, $00, $0f, $10, $a8, $00, $01, $10
;			.byt $a0, $00, $01, $00, $00, $01, $02, $50
x7650		.byt $a0, $00, $01, $00, $00, $01, $02, $50
			.byt $01, $70, $00, $00, $07, $10, $a8, $00 
x7660		.byt $01, $10, $a0, $00, $01, $00, $00, $0c	
			
			
 
            .byt $02	; 7698 02 
            asl                ; 7699 0A 
            ora ($0003,x)     ; 769A 01 03 
            .byt $04	; 769C 04 
            .byt $0C	; 769D 0C 
            .byt $FF	; 769E FF 
            .byt $02	; 769F 02 
            brk                ; 76A0 00 
            brk                ; 76A1 00 
            ora ($0018,x)     ; 76A2 01 18 
            ldy #$01           ; 76A4 A0 01 
            ora $0018         ; 76A6 05 18 
            lda $00FF         ; 76A8 A5 FF 
            ora ScratchRam     ; 76AA 05 00 
            brk                ; 76AC 00 
            clc                ; 76AD 18 

			.byt $02, $0a, $01, $02, $02, $0b, $00, $02, $00, $00
			.byt $10, $0c, $21, $00, $01, $0c, $18, $00, $01, $00
			.byt $01, $02, $20, $10, $ff, $04, $30, $02, $30, $00
			.byt $00, $01, $30, $21, $01, $08, $00, $01, $80, $84
			.byt $01, $04, $30, $85, $ff, $04, $00, $00, $04, $18
					





            lda $00FF         ; 76E0 A5 FF 
            .byt $04	; 76E2 04 
            clc                ; 76E3 18 
            lda ($0001,x)     ; 76E4 A1 01 
            .byt $04	; 76E6 04 
            brk                ; 76E7 00 
            brk                ; 76E8 00 
            bpl L076F7         ; 76E9 10 0C 
            eor #$00           ; 76EB 49 00 
            ora ($000C,x)     ; 76ED 01 0C 
            bmi L076F1         ; 76EF 30 00 
L076F1      ora (ScratchRam,x) ; 76F1 01 00 


; -------------------------------------------------------------------------------------------
            brk                ; 76F3 00 
            .byt $02	; 76F4 02 
            cpy #$A4           ; 76F5 C0 A4 
L076F7      brk                ; 76F7 00 
            ora (ScratchRam,x) ; 76F8 01 00 
            brk                ; 76FA 00 
            ora ($0001,x)     ; 76FB 01 01 
            beq L07700         ; 76FD F0 01 
            .byt $0F	; 76FF 0F 
L07700      ora ($00FF,x)     ; 7700 01 FF 
            .byt $FF	; 7702 FF 
            .byt $0F	; 7703 0F 
            brk                ; 7704 00 
            brk                ; 7705 00 
            ora ($001F,x)     ; 7706 01 1F 
            ldx #$00           ; 7708 A2 00 
            ora (ScratchRam,x) ; 770A 01 00 
            brk                ; 770C 00 
L0770D      bit L07741         ; 770D 2C 41 77 
            clc                ; 7710 18 
            bcc L0771C         ; 7711 90 09 
L07713      sec                ; 7713 38 
            bcs L07717         ; 7714 B0 01 
L07716      clc                ; 7716 18 
L07717      lda $0022         ; 7717 A5 22 
            beq L0770D         ; 7719 F0 F2 
            clv                ; 771B B8 
L0771C      txa                ; 771C 8A 
            pha                ; 771D 48 
            ldx #$07           ; 771E A2 07 




x7720		.byt $90, $04, $b5, $9b, $d0, $15, $b9, $c8
			.byt $75, $f0, $10, $50, $02, $a9, $00, $48
x7730		.byt $a9, $00, $95, $9b, $a9, $80, $95, $bb
			.byt $68, $95, $9b, $88, $ca, $10, $e1, $68
x7740		.byt $aa

L07741		.byt $60
L07742		.byt $a2, $07, $b4, $9b, $f0, $3a
			.byt $b5, $bb, $30, $41, $d6, $ab, $d0, $34
x7750		.byt $d6, $b3, $f0, $09, $b5, $a3, $18, $79
			.byt $2a, $76, $4c, $6b, $77, $c8, $c8, $c8
x7760		.byt $c8, $94, $9b, $b9, $2b, $76, $95, $b3
			.byt $b9, $29, $76, $95, $a3, $b9, $28, $76
x7770		.byt $95, $ab, $d0, $10, $b4, $c3, $d6, $bb
			.byt $d0, $e7, $b4, $9b, $c8, $d0, $0e, $a8
x7780		.byt $94, $9b, $94, $a3, $b5, $a3, $9d, $00
			.byt $2c, $ca, $10, $b8, $60, $b9, $28, $76
x7790		.byt $f0, $ed, $95, $bb, $c8, $94, $c3, $d0
			.byt $c8
			
L07799		.byt $01
L0779A		.byt $e0, $c0, $e1 
L0779D		.byt $e5
L0779E		.byt $c3, $ca
			
x77a0		.byt $c3, $af, $c3, $94, $c3

L077A5		.byt $7f, $02, $04

			.byt $04, $05, $03, $7f, $7f, $ff, $ff, $ff




            .byt $FF	; 77B0 FF 
            .byt $FF	; 77B1 FF 
            .byt $FF	; 77B2 FF 
            .byt $FF	; 77B3 FF 
            .byt $FF	; 77B4 FF 
            .byt $FF	; 77B5 FF 
            .byt $FF	; 77B6 FF 
            .byt $FF	; 77B7 FF 
            .byt $FF	; 77B8 FF 
            .byt $FF	; 77B9 FF 
            .byt $FF	; 77BA FF 
            .byt $FF	; 77BB FF 
            .byt $FF	; 77BC FF 
            .byt $FF	; 77BD FF 
            .byt $FF	; 77BE FF 
            .byt $FF	; 77BF FF 
            .byt $FF	; 77C0 FF 
            .byt $FF	; 77C1 FF 
            .byt $FF	; 77C2 FF 
            .byt $FF	; 77C3 FF 
            .byt $FF	; 77C4 FF 
            .byt $FF	; 77C5 FF 
            .byt $FF	; 77C6 FF 
            .byt $FF	; 77C7 FF 
            .byt $FF	; 77C8 FF 
            .byt $FF	; 77C9 FF 
            .byt $FF	; 77CA FF 
            .byt $FF	; 77CB FF 
            .byt $FF	; 77CC FF 
            .byt $FF	; 77CD FF 
            .byt $FF	; 77CE FF 
            .byt $FF	; 77CF FF 
            .byt $FF	; 77D0 FF 
            .byt $FF	; 77D1 FF 
            .byt $FF	; 77D2 FF 
            .byt $FF	; 77D3 FF 
            .byt $FF	; 77D4 FF 
            .byt $FF	; 77D5 FF 
            .byt $FF	; 77D6 FF 
            .byt $FF	; 77D7 FF 
            .byt $FF	; 77D8 FF 
            .byt $FF	; 77D9 FF 
            .byt $FF	; 77DA FF 
            .byt $FF	; 77DB FF 
            .byt $FF	; 77DC FF 
            .byt $FF	; 77DD FF 
            .byt $FF	; 77DE FF 
            .byt $FF	; 77DF FF 
            .byt $FF	; 77E0 FF 
            .byt $FF	; 77E1 FF 
            .byt $FF	; 77E2 FF 
            .byt $FF	; 77E3 FF 
            .byt $FF	; 77E4 FF 
            .byt $FF	; 77E5 FF 
            .byt $FF	; 77E6 FF 
            .byt $FF	; 77E7 FF 
            .byt $FF	; 77E8 FF 
            .byt $FF	; 77E9 FF 
            .byt $FF	; 77EA FF 
            .byt $FF	; 77EB FF 
            .byt $FF	; 77EC FF 
            .byt $FF	; 77ED FF 
            .byt $FF	; 77EE FF 
            .byt $FF	; 77EF FF 
            .byt $FF	; 77F0 FF 
            .byt $FF	; 77F1 FF 
            .byt $FF	; 77F2 FF 
            .byt $FF	; 77F3 FF 
            .byt $FF	; 77F4 FF 
            .byt $FF	; 77F5 FF 
            .byt $FF	; 77F6 FF 
            .byt $FF	; 77F7 FF 
            .byt $FF	; 77F8 FF 
            .byt $FF	; 77F9 FF 
            .byt $FF	; 77FA FF 
            .byt $FF	; 77FB FF 
            .byt $FF	; 77FC FF 
            .byt $FF	; 77FD FF 
            .byt $FF	; 77FE FF 
            .byt $FF	; 77FF FF 
            .byt $FF	; 7800 FF 
            .byt $FF	; 7801 FF 
            .byt $FF	; 7802 FF 
            .byt $FF	; 7803 FF 
            .byt $FF	; 7804 FF 
            .byt $FF	; 7805 FF 
            .byt $FF	; 7806 FF 
            .byt $FF	; 7807 FF 
            .byt $FF	; 7808 FF 
            .byt $FF	; 7809 FF 
            .byt $FF	; 780A FF 
            .byt $FF	; 780B FF 
            .byt $FF	; 780C FF 
            .byt $FF	; 780D FF 
            .byt $FF	; 780E FF 
            .byt $FF	; 780F FF 
            .byt $FF	; 7810 FF 
            .byt $FF	; 7811 FF 
            .byt $FF	; 7812 FF 
            .byt $FF	; 7813 FF 
            .byt $FF	; 7814 FF 
            .byt $FF	; 7815 FF 
            .byt $FF	; 7816 FF 
            .byt $FF	; 7817 FF 
            .byt $FF	; 7818 FF 
            .byt $FF	; 7819 FF 
            .byt $FF	; 781A FF 
            .byt $FF	; 781B FF 
            .byt $FF	; 781C FF 
            .byt $FF	; 781D FF 
            .byt $FF	; 781E FF 
            .byt $FF	; 781F FF 
            .byt $FF	; 7820 FF 
            .byt $FF	; 7821 FF 
            .byt $FF	; 7822 FF 
            .byt $FF	; 7823 FF 
            .byt $FF	; 7824 FF 
            .byt $FF	; 7825 FF 
            .byt $FF	; 7826 FF 
            .byt $FF	; 7827 FF 
            .byt $FF	; 7828 FF 
            .byt $FF	; 7829 FF 
            .byt $FF	; 782A FF 
            .byt $FF	; 782B FF 
            .byt $FF	; 782C FF 
            .byt $FF	; 782D FF 
            .byt $FF	; 782E FF 
            .byt $FF	; 782F FF 
            .byt $FF	; 7830 FF 
            .byt $FF	; 7831 FF 
            .byt $FF	; 7832 FF 
            .byt $FF	; 7833 FF 
            .byt $FF	; 7834 FF 
            .byt $FF	; 7835 FF 
            .byt $FF	; 7836 FF 
            .byt $FF	; 7837 FF 
            .byt $FF	; 7838 FF 
            .byt $FF	; 7839 FF 
            .byt $FF	; 783A FF 
            .byt $FF	; 783B FF 
            .byt $FF	; 783C FF 
            .byt $FF	; 783D FF 
            .byt $FF	; 783E FF 
            .byt $FF	; 783F FF 
            .byt $FF	; 7840 FF 
            .byt $FF	; 7841 FF 
            .byt $FF	; 7842 FF 
            .byt $FF	; 7843 FF 
L07844      lda #$00           ; 7844 A9 00 
            ldx #$07           ; 7846 A2 07 
L07848      sta $009B,x       ; 7848 95 9B 
            dex                ; 784A CA 
            bpl L07848         ; 784B 10 FB 
            sta PKY_AUDCTL         ; 784D 8D 08 2C 
            rts                ; 7850 60 
            
NMI_ISR            
            bit $01FF         ; 7851 2C FF 01 
            bpl L07857         ; 7854 10 01 
            rti                ; 7856 40 
L07857      pha                ; 7857 48 
            tya                ; 7858 98 
            pha                ; 7859 48 
            txa                ; 785A 8A 
            pha                ; 785B 48 
            cld                ; 785C D8 
            lda $01FF         ; 785D AD FF 01 
            ora $01D0         ; 7860 0D D0 01 
L07863      bne L07863         ; 7863 D0 FE 
            inc $0078         ; 7865 E6 78 
            lda $0078         ; 7867 A5 78 
            and #$03           ; 7869 29 03 
            bne L0787A         ; 786B D0 0D 
            inc $0075         ; 786D E6 75 
            lda $0075         ; 786F A5 75 
            cmp #$03           ; 7871 C9 03 
            bcc L0787A         ; 7873 90 05 
L07875      bne L07875         ; 7875 D0 FE 
            sta ResetVG        ; 7877 8D 00 38 
L0787A      ldx $0022         ; 787A A6 22 
            bne L0789B         ; 787C D0 1D 
            lda $0042         ; 787E A5 42 
            and $0043         ; 7880 25 43 
            bpl L0789B         ; 7882 10 17 
            stx PKY_SKCTL         ; 7884 8E 0F 2C 
            ldx #$04           ; 7887 A2 04 
L07889      dex                ; 7889 CA 
            bne L07889         ; 788A D0 FD 
            ldx #$07           ; 788C A2 07 
            stx PKY_SKCTL         ; 788E 8E 0F 2C 
            stx PKY_POTGO         ; 7891 8E 0B 2C 
            lda PKY_ALLPOT         ; 7894 AD 08 2C 
            eor #$FF           ; 7897 49 FF 
            sta $008D         ; 7899 85 8D 
L0789B      jsr L07742         ; 789B 20 42 77 
            ldx #$02           ; 789E A2 02 
L078A0      lda LCoinSwitch,x  ; 78A0 BD 00 24 
            asl                ; 78A3 0A 
            lda $0096,x       ; 78A4 B5 96 
            and #$1F           ; 78A6 29 1F 
            bcc L078E1         ; 78A8 90 37 
            beq L078BC         ; 78AA F0 10 
            cmp #$1B           ; 78AC C9 1B 
            bcs L078BA         ; 78AE B0 0A 
            tay                ; 78B0 A8 
            lda $0078         ; 78B1 A5 78 
            and #$07           ; 78B3 29 07 
            cmp #$07           ; 78B5 C9 07 
            tya                ; 78B7 98 
            bcc L078BC         ; 78B8 90 02 
L078BA      sbc #$01           ; 78BA E9 01 
L078BC      sta $0096,x       ; 78BC 95 96 
            lda SlamSwitch     ; 78BE AD 06 20 
            and #$80           ; 78C1 29 80 
            beq L078C9         ; 78C3 F0 04 
            lda #$F0           ; 78C5 A9 F0 
            sta $008E         ; 78C7 85 8E 
L078C9      lda $008E         ; 78C9 A5 8E 
            beq L078D5         ; 78CB F0 08 
            dec $008E         ; 78CD C6 8E 
            lda #$00           ; 78CF A9 00 
            sta $0096,x       ; 78D1 95 96 
            sta $0093,x       ; 78D3 95 93 
L078D5      clc                ; 78D5 18 
            lda $0093,x       ; 78D6 B5 93 
            beq L078FD         ; 78D8 F0 23 
            dec $0093,x       ; 78DA D6 93 
            bne L078FD         ; 78DC D0 1F 
            sec                ; 78DE 38 
            bcs L078FD         ; 78DF B0 1C 
L078E1      cmp #$1B           ; 78E1 C9 1B 
            bcs L078EE         ; 78E3 B0 09 
            lda $0096,x       ; 78E5 B5 96 
            adc #$20           ; 78E7 69 20 
            bcc L078BC         ; 78E9 90 D1 
            beq L078EE         ; 78EB F0 01 
            clc                ; 78ED 18 
L078EE      lda #$1F           ; 78EE A9 1F 
            bcs L078BC         ; 78F0 B0 CA 
            sta $0096,x       ; 78F2 95 96 
            lda $0093,x       ; 78F4 B5 93 
            beq L078F9         ; 78F6 F0 01 
            sec                ; 78F8 38 
L078F9      lda #$78           ; 78F9 A9 78 
            sta $0093,x       ; 78FB 95 93 
L078FD      bcc L07929         ; 78FD 90 2A 
            lda #$00           ; 78FF A9 00 
            cpx #$01           ; 7901 E0 01 
            bcc L0791B         ; 7903 90 16 
            beq L07913         ; 7905 F0 0C 
            lda $008D         ; 7907 A5 8D 
            and #$0C           ; 7909 29 0C 
            lsr                ; 790B 4A 
            lsr                ; 790C 4A 
            beq L0791B         ; 790D F0 0C 
            adc #$02           ; 790F 69 02 
            bne L0791B         ; 7911 D0 08 
L07913      lda $008D         ; 7913 A5 8D 
            and #$10           ; 7915 29 10 
            beq L0791B         ; 7917 F0 02 
            lda #$01           ; 7919 A9 01 
L0791B      sec                ; 791B 38 
            pha                ; 791C 48 
            adc $0099         ; 791D 65 99 
            sta $0099         ; 791F 85 99 
            pla                ; 7921 68 
            sec                ; 7922 38 
            adc $008F         ; 7923 65 8F 
            sta $008F         ; 7925 85 8F 
            inc $0090,x       ; 7927 F6 90 
L07929      dex                ; 7929 CA 
            bmi L0792F         ; 792A 30 03 
            jmp L078A0         ; 792C 4C A0 78 
L0792F      lda $008D         ; 792F A5 8D 
            lsr                ; 7931 4A 
            lsr                ; 7932 4A 
            lsr                ; 7933 4A 
            lsr                ; 7934 4A 
            lsr                ; 7935 4A 
            tay                ; 7936 A8 
            lda $0099         ; 7937 A5 99 
            sec                ; 7939 38 
            sbc L077A5,y       ; 793A F9 A5 77 
            bmi L07949         ; 793D 30 0A 
            sta $0099         ; 793F 85 99 
            inc $009A         ; 7941 E6 9A 
            cpy #$03           ; 7943 C0 03 
            bne L07949         ; 7945 D0 02 
            inc $009A         ; 7947 E6 9A 
L07949      lda $008D         ; 7949 A5 8D 
            and #$03           ; 794B 29 03 
            tay                ; 794D A8 
            beq L0796A         ; 794E F0 1A 
            lsr                ; 7950 4A 
            adc #$00           ; 7951 69 00 
            eor #$FF           ; 7953 49 FF 
            sec                ; 7955 38 
            adc $008F         ; 7956 65 8F 
            bcs L07962         ; 7958 B0 08 
            adc $009A         ; 795A 65 9A 
            bmi L0796C         ; 795C 30 0E 
            sta $009A         ; 795E 85 9A 
            lda #$00           ; 7960 A9 00 
L07962      cpy #$02           ; 7962 C0 02 
            bcs L07968         ; 7964 B0 02 
            inc $008C         ; 7966 E6 8C 
L07968      inc $008C         ; 7968 E6 8C 
L0796A      sta $008F         ; 796A 85 8F 
L0796C      lda $0078         ; 796C A5 78 
            lsr                ; 796E 4A 
            bcs L07998         ; 796F B0 27 
            ldy #$00           ; 7971 A0 00 
            ldx #$02           ; 7973 A2 02 
L07975      lda $0090,x       ; 7975 B5 90 
            beq L07982         ; 7977 F0 09 
            cmp #$10           ; 7979 C9 10 
            bcc L07982         ; 797B 90 05 
            adc #$EF           ; 797D 69 EF 
            iny                ; 797F C8 
            sta $0090,x       ; 7980 95 90 
L07982      dex                ; 7982 CA 
            bpl L07975         ; 7983 10 F0 
            tya                ; 7985 98 
            bne L07998         ; 7986 D0 10 
            ldx #$02           ; 7988 A2 02 
L0798A      lda $0090,x       ; 798A B5 90 
            beq L07995         ; 798C F0 07 
            clc                ; 798E 18 
            adc #$EF           ; 798F 69 EF 
            sta $0090,x       ; 7991 95 90 
            bmi L07998         ; 7993 30 03 
L07995      dex                ; 7995 CA 
            bpl L0798A         ; 7996 10 F2 
L07998      lda $0090         ; 7998 A5 90 
            sta LCoinCounter   ; 799A 8D 05 3C 
            lda $0091         ; 799D A5 91 
            sta CCoinCounter   ; 799F 8D 06 3C 
            lda $0092         ; 79A2 A5 92 
            sta RCoinCounter   ; 79A4 8D 07 3C 
            lda $0085         ; 79A7 A5 85 
            eor #$03           ; 79A9 49 03 
            ror                ; 79AB 6A 
            ror                ; 79AC 6A 
            sta OnePlayerLED          ; 79AD 8D 00 3C 
            ror                ; 79B0 6A 
            sta TwoPlayerLED          ; 79B1 8D 01 3C 
            lda $008E         ; 79B4 A5 8E 
            beq L079C2         ; 79B6 F0 0A 
            lda #$08           ; 79B8 A9 08 
            ldy #$AF           ; 79BA A0 AF 
            sta PKY_AUDF1          ; 79BC 8D 00 2C 
            sty PKY_AUDC1         ; 79BF 8C 01 2C 
L079C2      lda #$00           ; 79C2 A9 00 
            asl FireSwitch     ; 79C4 0E 04 20 
            bcc L079CE         ; 79C7 90 05 
            lda $00FE         ; 79C9 A5 FE 
            ora #$08           ; 79CB 09 08 
            asl                ; 79CD 0A 
L079CE      sta $00FE         ; 79CE 85 FE 
            pla                ; 79D0 68 
            tax                ; 79D1 AA 
            pla                ; 79D2 68 
            tay                ; 79D3 A8 
            pla                ; 79D4 68 
            rti                ; 79D5 40 
L079D6      lda #$D0           ; 79D6 A9 D0 
            bne L079DC         ; 79D8 D0 02 
L079DA      lda #$B0           ; 79DA A9 B0 
L079DC      ldy #$00           ; 79DC A0 00 
            sta ($0003),y     ; 79DE 91 03 
            iny                ; 79E0 C8 
            sta ($0003),y     ; 79E1 91 03 
            bne L07A55         ; 79E3 D0 70 
L079E5      bcc L079EB         ; 79E5 90 04 
            and #$0F           ; 79E7 29 0F 
            beq L079F0         ; 79E9 F0 05 
L079EB      and #$0F           ; 79EB 29 0F 
            clc                ; 79ED 18 
            adc #$01           ; 79EE 69 01 
L079F0      php                ; 79F0 08 
            asl                ; 79F1 0A 
            tay                ; 79F2 A8 
            jsr L079F8         ; 79F3 20 F8 79 
            plp                ; 79F6 28 
            rts                ; 79F7 60 
L079F8      cpy #$4A           ; 79F8 C0 4A 
            bcc L079FE         ; 79FA 90 02 
            ldy #$00           ; 79FC A0 00 
L079FE      ldx VROM56F9,y       ; 79FE BE F9 56 
            lda VROM56F8,y       ; 7A01 B9 F8 56 
            jmp L07CD5         ; 7A04 4C D5 7C 
            lsr                ; 7A07 4A 
            and #$0F           ; 7A08 29 0F 
            ora #$E0           ; 7A0A 09 E0 
L07A0C      ldy #$01           ; 7A0C A0 01 
            sta ($0003),y     ; 7A0E 91 03 
            dey                ; 7A10 88 
            txa                ; 7A11 8A 
            ror                ; 7A12 6A 
            sta ($0003),y     ; 7A13 91 03 
            iny                ; 7A15 C8 
            bne L07A55         ; 7A16 D0 3D 
L07A18      lsr                ; 7A18 4A 
            and #$0F           ; 7A19 29 0F 
            ora #$C0           ; 7A1B 09 C0 
            bne L07A0C         ; 7A1D D0 ED 
L07A1F      ldy #$00           ; 7A1F A0 00 
            sty $0006         ; 7A21 84 06 
            sty $0008         ; 7A23 84 08 
            asl                ; 7A25 0A 
            rol $0006         ; 7A26 26 06 
            asl                ; 7A28 0A 
            rol $0006         ; 7A29 26 06 
            sta $0005         ; 7A2B 85 05 
            txa                ; 7A2D 8A 
            asl                ; 7A2E 0A 
            rol $0008         ; 7A2F 26 08 
            asl                ; 7A31 0A 
            rol $0008         ; 7A32 26 08 
            sta $0007         ; 7A34 85 07 
            ldx #$05           ; 7A36 A2 05 
L07A38      lda $0002,x       ; 7A38 B5 02 
            ldy #$00           ; 7A3A A0 00 
            sta ($0003),y     ; 7A3C 91 03 
            lda $0003,x       ; 7A3E B5 03 
            and #$0F           ; 7A40 29 0F 
            ora #$A0           ; 7A42 09 A0 
            iny                ; 7A44 C8 
            sta ($0003),y     ; 7A45 91 03 
            lda ScratchRam,x   ; 7A47 B5 00 
            iny                ; 7A49 C8 
            sta ($0003),y     ; 7A4A 91 03 
            lda $0001,x       ; 7A4C B5 01 
            and #$0F           ; 7A4E 29 0F 
            ora $0001         ; 7A50 05 01 
            iny                ; 7A52 C8 
            sta ($0003),y     ; 7A53 91 03 
L07A55      tya                ; 7A55 98 
            sec                ; 7A56 38 
            adc $0003         ; 7A57 65 03 
            sta $0003         ; 7A59 85 03 
            bcc L07A5F         ; 7A5B 90 02 
            inc $0004         ; 7A5D E6 04 
L07A5F      rts                ; 7A5F 60 
            ldx #$00           ; 7A60 A2 00 
            lda $0006         ; 7A62 A5 06 
            cmp #$80           ; 7A64 C9 80 
            bcc L07A72         ; 7A66 90 0A 
            txa                ; 7A68 8A 
            sbc $0005         ; 7A69 E5 05 
            sta $0005         ; 7A6B 85 05 
            txa                ; 7A6D 8A 
            sbc $0006         ; 7A6E E5 06 
            sta $0006         ; 7A70 85 06 
L07A72      rol $0009         ; 7A72 26 09 
            lda $0008         ; 7A74 A5 08 
            cmp #$80           ; 7A76 C9 80 
            bcc L07A84         ; 7A78 90 0A 
            txa                ; 7A7A 8A 
            sbc $0007         ; 7A7B E5 07 
            sta $0007         ; 7A7D 85 07 
            txa                ; 7A7F 8A 
            sbc $0008         ; 7A80 E5 08 
            sta $0008         ; 7A82 85 08 
L07A84      rol $0009         ; 7A84 26 09 
            ora $0006         ; 7A86 05 06 
            beq L07A92         ; 7A88 F0 08 
            cmp #$02           ; 7A8A C9 02 
            bcs L07AB2         ; 7A8C B0 24 
            ldy #$01           ; 7A8E A0 01 
            bne L07AA2         ; 7A90 D0 10 
L07A92      ldy #$02           ; 7A92 A0 02 
            ldx #$09           ; 7A94 A2 09 
            lda $0005         ; 7A96 A5 05 
            ora $0007         ; 7A98 05 07 
            beq L07AB2         ; 7A9A F0 16 
            bmi L07AA2         ; 7A9C 30 04 
L07A9E      iny                ; 7A9E C8 
            asl                ; 7A9F 0A 
            bpl L07A9E         ; 7AA0 10 FC 
L07AA2      tya                ; 7AA2 98 
            tax                ; 7AA3 AA 
            lda $0006         ; 7AA4 A5 06 
L07AA6      asl $0005         ; 7AA6 06 05 
            rol                ; 7AA8 2A 
            asl $0007         ; 7AA9 06 07 
            rol $0008         ; 7AAB 26 08 
            dey                ; 7AAD 88 
            bne L07AA6         ; 7AAE D0 F6 
            sta $0006         ; 7AB0 85 06 
L07AB2      txa                ; 7AB2 8A 
            sec                ; 7AB3 38 
            sbc #$0A           ; 7AB4 E9 0A 
            eor #$FF           ; 7AB6 49 FF 
            asl                ; 7AB8 0A 
            ror $0009         ; 7AB9 66 09 
            rol                ; 7ABB 2A 
            ror $0009         ; 7ABC 66 09 
            rol                ; 7ABE 2A 
            asl                ; 7ABF 0A 
            sta $0009         ; 7AC0 85 09 
            ldy #$00           ; 7AC2 A0 00 
            lda $0007         ; 7AC4 A5 07 
            sta ($0003),y     ; 7AC6 91 03 
            lda $0009         ; 7AC8 A5 09 
            and #$F4           ; 7ACA 29 F4 
            ora $0008         ; 7ACC 05 08 
            iny                ; 7ACE C8 
            sta ($0003),y     ; 7ACF 91 03 
            lda $0005         ; 7AD1 A5 05 
            iny                ; 7AD3 C8 
            sta ($0003),y     ; 7AD4 91 03 
            lda $0009         ; 7AD6 A5 09 
            and #$02           ; 7AD8 29 02 
            asl                ; 7ADA 0A 
            ora $0002         ; 7ADB 05 02 
            ora $0006         ; 7ADD 05 06 
            iny                ; 7ADF C8 
            sta ($0003),y     ; 7AE0 91 03 
            jmp L07A55         ; 7AE2 4C 55 7A 
L07AE5      jsr L07A1F         ; 7AE5 20 1F 7A 
            lda #$70           ; 7AE8 A9 70 
L07AEA      ldx #$00           ; 7AEA A2 00 
L07AEC      ldy #$01           ; 7AEC A0 01 
            sta ($0003),y     ; 7AEE 91 03 
            dey                ; 7AF0 88 
            tya                ; 7AF1 98 
            sta ($0003),y     ; 7AF2 91 03 
            iny                ; 7AF4 C8 
            iny                ; 7AF5 C8 
            sta ($0003),y     ; 7AF6 91 03 
            iny                ; 7AF8 C8 
            txa                ; 7AF9 8A 
            sta ($0003),y     ; 7AFA 91 03 
            jmp L07A55         ; 7AFC 4C 55 7A 
L07AFF      lda $0078         ; 7AFF A5 78 
            and #$0C           ; 7B01 29 0C 
            bne L07B26         ; 7B03 D0 21 
            lda $00CD         ; 7B05 A5 CD 
            beq L07B23         ; 7B07 F0 1A 
            bpl L07B27         ; 7B09 10 1C 
            ldx $00CB         ; 7B0B A6 CB 
            lda #$06           ; 7B0D A9 06 
            sta EACtlLatch     ; 7B0F 8D 00 3A 
            sta LatchEA,x      ; 7B12 9D 00 32 
            lda #$0E           ; 7B15 A9 0E 
            dec $00CB         ; 7B17 C6 CB 
            dec $00CC         ; 7B19 C6 CC 
            bne L07B23         ; 7B1B D0 06 
            ldx #$40           ; 7B1D A2 40 
            stx $00CD         ; 7B1F 86 CD 
L07B21      inc $00CB         ; 7B21 E6 CB 
L07B23      sta EACtlLatch     ; 7B23 8D 00 3A 
L07B26      rts                ; 7B26 60 
L07B27      ldy $00CC         ; 7B27 A4 CC 
            bne L07B2D         ; 7B29 D0 02 
            sty $00CF         ; 7B2B 84 CF 
L07B2D      asl                ; 7B2D 0A 
            bpl L07B65         ; 7B2E 10 35 
            lda L07BC0,y       ; 7B30 B9 C0 7B 
            bpl L07B41         ; 7B33 10 0C 
            sta $00CC         ; 7B35 85 CC 
            inc $00CE         ; 7B37 E6 CE 
            inc $00CE         ; 7B39 E6 CE 
            inc $00CE         ; 7B3B E6 CE 
            lda $00CF         ; 7B3D A5 CF 
            bcc L07B4C         ; 7B3F 90 0B 
L07B41      adc $00CE         ; 7B41 65 CE 
            tax                ; 7B43 AA 
            lda $0023,x       ; 7B44 B5 23 
            adc $00CF         ; 7B46 65 CF 
            sta $00CF         ; 7B48 85 CF 
            lda $0023,x       ; 7B4A B5 23 
L07B4C      inc $00CC         ; 7B4C E6 CC 
            ldx $00CB         ; 7B4E A6 CB 
            sta LatchEA,x      ; 7B50 9D 00 32 
            lda #$04           ; 7B53 A9 04 
            sta EACtlLatch     ; 7B55 8D 00 3A 
            lda #$0C           ; 7B58 A9 0C 
L07B5A      inx                ; 7B5A E8 
            cpx #$15           ; 7B5B E0 15 
            bcc L07B21         ; 7B5D 90 C2 
            ldx #$00           ; 7B5F A2 00 
            stx $00CD         ; 7B61 86 CD 
            beq L07B23         ; 7B63 F0 BE 
L07B65      ldx $00CB         ; 7B65 A6 CB 
            lda #$08           ; 7B67 A9 08 
            sta LatchEA,x      ; 7B69 9D 00 32 
            sta EACtlLatch     ; 7B6C 8D 00 3A 
            lda #$09           ; 7B6F A9 09 
            sta EACtlLatch     ; 7B71 8D 00 3A 
            lda #$08           ; 7B74 A9 08 
            sta EACtlLatch     ; 7B76 8D 00 3A 
            ldx #$00           ; 7B79 A2 00 
            lda L07BC0,y       ; 7B7B B9 C0 7B 
            bpl L07BAA         ; 7B7E 10 2A 
            stx $00CC         ; 7B80 86 CC 
            lda EAROM          ; 7B82 AD 40 2C 
            stx EACtlLatch     ; 7B85 8E 00 3A 
            eor $00CF         ; 7B88 45 CF 
            beq L07BA2         ; 7B8A F0 16 
            ldy #$02           ; 7B8C A0 02 
            tya                ; 7B8E 98 
            adc $00CE         ; 7B8F 65 CE 
            tax                ; 7B91 AA 
            lda #$00           ; 7B92 A9 00 
L07B94      sta $0023,x       ; 7B94 95 23 
            sta $0044,x       ; 7B96 95 44 
            dex                ; 7B98 CA 
            dey                ; 7B99 88 
            bpl L07B94         ; 7B9A 10 F8 
            inc $00D0         ; 7B9C E6 D0 
L07B9E      ldx $00CB         ; 7B9E A6 CB 
            bpl L07B5A         ; 7BA0 10 B8 
L07BA2      inc $00CE         ; 7BA2 E6 CE 
            inc $00CE         ; 7BA4 E6 CE 
            inc $00CE         ; 7BA6 E6 CE 
            bpl L07B9E         ; 7BA8 10 F4 
L07BAA      adc $00CE         ; 7BAA 65 CE 
            tay                ; 7BAC A8 
            lda EAROM          ; 7BAD AD 40 2C 
            stx EACtlLatch     ; 7BB0 8E 00 3A 
            sta $0023,y       ; 7BB3 99 23 00 
            adc $00CF         ; 7BB6 65 CF 
            sta $00CF         ; 7BB8 85 CF 
            inc $00CC         ; 7BBA E6 CC 
            lda #$00           ; 7BBC A9 00 
            beq L07B9E         ; 7BBE F0 DE 
L07BC0      brk                ; 7BC0 00 
            ora ($0002,x)     ; 7BC1 01 02 
            and ($0022,x)     ; 7BC3 21 22 
            .byt $23         ; 7BC5 23 
            .byt $FF         ; 7BC6 FF 
L07BC7      ldy $00CD         ; 7BC7 A4 CD 
            bne L07BE2         ; 7BC9 D0 17 
            lda #$08           ; 7BCB A9 08 
            cmp $00CE         ; 7BCD C5 CE 
            bcc L07BE2         ; 7BCF 90 11 
            ror $00CD         ; 7BD1 66 CD 
            lda $00D0         ; 7BD3 A5 D0 
            beq L07BE2         ; 7BD5 F0 0B 
            ldx #$15           ; 7BD7 A2 15 
            stx $00CC         ; 7BD9 86 CC 
            dex                ; 7BDB CA 
            stx $00CB         ; 7BDC 86 CB 
            sty $00CE         ; 7BDE 84 CE 
            sty $00D0         ; 7BE0 84 D0 
L07BE2      rts                ; 7BE2 60 


Sub_7BE3    bit VGHalted  		; 7BE3 2C 02 20 - Wait for the vector generator to finish
            bmi Sub_7BE3        ; 7BE6 30 FB 

            jsr Sub_7118     	; 7BE8 20 18 71
 
            lda #VGOP_HALT      ; 7BEB A9 B0

; Why VectorRam+3 ??? 
            sta VectorRam+3     ; 7BED 8D 03 40 - Write halt instruction to vector generator memory

            lda #$00           	; 7BF0 A9 00 
            ldx #$03           	; 7BF2 A2 03
 
@           sta $0061,x       	; 7BF4 95 61 
            sta $0064,x       	; 7BF6 95 64 
            dex                	; 7BF8 CA 
            bne <@         		; 7BF9 D0 F9 

; clear page 2
;   x - previously set to 0
@      		sta $0200,x       	; 7BFB 9D 00 02 
            inx                	; 7BFE E8 
            bne <@         		; 7BFF D0 FA

; looks like we're initialising a load of variables to 0
            sta $00DD         	; 7C01 85 DD 
            sta $00D3         	; 7C03 85 D3 
            sta $00FA         	; 7C05 85 FA 
            sta $00FC         	; 7C07 85 FC

            lda OptionSwitch65 	; 7C09 AD 01 28 
            and #$02           	; 7C0C 29 02 
            sta $00FB         	; 7C0E 85 FB 
            sta $00FD         	; 7C10 85 FD

            lda #$01           	; 7C12 A9 01 
            jsr Sub_6300        ; 7C14 20 00 63

            lda #$98           	; 7C17 A9 98 
            sta $02E9         	; 7C19 8D E9 02 
            sta $02E8         	; 7C1C 8D E8 02 
            lda #$7F           	; 7C1F A9 7F 
            sta $02EC         	; 7C21 8D EC 02 
            lda #$06           	; 7C24 A9 06 
            sta $02EE         	; 7C26 8D EE 02 
            lda #$FF           	; 7C29 A9 FF 
            sta $0042         	; 7C2B 85 42 
            sta $0043        	; 7C2D 85 43 
            lda #$30           	; 7C2F A9 30 
            sta $02ED         	; 7C31 8D ED 02 
L07C34      jsr L07FCF         	; 7C34 20 CF 7F 
            lda OptionSwitch87 	; 7C37 AD 00 28 
            and #$03           	; 7C3A 29 03 
            tay                	; 7C3C A8 
            lda L07C6B,y       	; 7C3D B9 6B 7C 
            sta $00F8         	; 7C40 85 F8 
            sta $0069         	; 7C42 85 69 
            sta $006C         	; 7C44 85 6C 
            bmi L07C4A         	; 7C46 30 02 
            lda #$01           	; 7C48 A9 01 
L07C4A      sta $006A         	; 7C4A 85 6A 
            sta $00F9         	; 7C4C 85 F9 
            sta $006D         	; 7C4E 85 6D 
            lda #$03           	; 7C50 A9 03 
            and OptionSwitch43 	; 7C52 2D 02 28 
            tax                	; 7C55 AA 
            inx                	; 7C56 E8 
            inx                	; 7C57 E8 
            cpy #$03           	; 7C58 C0 03 
            bne L07C5D         	; 7C5A D0 01 
            inx                	; 7C5C E8 
L07C5D      lda $008D         	; 7C5D A5 8D 
            and #$03           	; 7C5F 29 03 
            cmp #$03           	; 7C61 C9 03 
            bne L07C66         	; 7C63 D0 01 
            inx                	; 7C65 E8 
L07C66      stx $006E         	; 7C66 86 6E 
            jmp L07844         	; 7C68 4C 44 78 
L07C6B      brk                	; 7C6B 00 
            jsr L0FF50         	; 7C6C 20 50 FF 
L07C6F      ldx #$D5           	; 7C6F A2 D5 
L07C71      sty $0009         	; 7C71 84 09 
            ldy #$E0           	; 7C73 A0 E0 
            sty $0001         	; 7C75 84 01 
            jsr L07A1F         	; 7C77 20 1F 7A 
            lda #$70           	; 7C7A A9 70 
            jsr L07AEA         	; 7C7C 20 EA 7A 
            jmp L07C89         	; 7C7F 4C 89 7C 
L07C82      ldx #$CA           	; 7C82 A2 CA 
            lda #$A4           	; 7C84 A9 A4 
            jsr L07CD5         	; 7C86 20 D5 7C 
L07C89      dec $0009         	; 7C89 C6 09 
            beq L07C8F         	; 7C8B F0 02 
            bpl L07C82         	; 7C8D 10 F3 
L07C8F      rts                	; 7C8F 60 



L07C90      lda #$F7           ; 7C90 A9 F7 
            ldy #$03           ; 7C92 A0 03 
            sec                ; 7C94 38 
L07C95      php                ; 7C95 08 
            stx $001D         ; 7C96 86 1D 
            dey                ; 7C98 88 
            sty $001C         ; 7C99 84 1C 
            clc                ; 7C9B 18 
            adc $001C         ; 7C9C 65 1C 
            sta $001B         ; 7C9E 85 1B 
            plp                ; 7CA0 28 
            tax                ; 7CA1 AA 
L07CA2      php                ; 7CA2 08 
            lda ScratchRam,x   ; 7CA3 B5 00 
            lsr                ; 7CA5 4A 
            lsr                ; 7CA6 4A 
            lsr                ; 7CA7 4A 
            lsr                ; 7CA8 4A 
            plp                ; 7CA9 28 
            jsr L079E5         ; 7CAA 20 E5 79 
            lda $001C         ; 7CAD A5 1C 
            bne L07CB2         ; 7CAF D0 01 
            clc                ; 7CB1 18 
L07CB2      ldx $001B         ; 7CB2 A6 1B 
            lda ScratchRam,x   ; 7CB4 B5 00 
            jsr L079E5         ; 7CB6 20 E5 79 
            dec $001B         ; 7CB9 C6 1B 
            ldx $001B         ; 7CBB A6 1B 
            dec $001C         ; 7CBD C6 1C 
            bpl L07CA2         ; 7CBF 10 E1 
            rts                ; 7CC1 60 
L07CC2      jsr L07CC5         ; 7CC2 20 C5 7C 
L07CC5      ldx $000C         ; 7CC5 A6 0C 
            dec $000C         ; 7CC7 C6 0C 
            lda OptionSwitch87,x ; 7CC9 BD 00 28 
            and #$03           ; 7CCC 29 03 
L07CCE      jsr L079EB         ; 7CCE 20 EB 79 
            lda #$0A           ; 7CD1 A9 0A 
            ldx #$CB           ; 7CD3 A2 CB 
L07CD5      ldy #$00           ; 7CD5 A0 00 
            sta ($0003),y     ; 7CD7 91 03 
            iny                ; 7CD9 C8 
            txa                ; 7CDA 8A 
            sta ($0003),y     ; 7CDB 91 03 
            jmp L07A55         ; 7CDD 4C 55 7A 






;-------------------------------------------------------------------------
; This is where it all kicks off
;
RESET_ISR
            ldx #$FE                        ; 7CE0 A2 FE    - Reset SP
            txs                             ; 7CE2 9A
             
            cld                             ; 7CE3 D8
             
            ldx #$00                        ; 7CE4 A2 00    - Reset POKEY
            stx PKY_SKCTL                   ; 7CE6 8E 0F 2C
       
; Walking bit RAM test      
RamTest
NextByte 
            lda #$11                        ; 7CE9 A9 11 
WalkBit     sta PageOne,x                   ; 7CEB 9D 00 01 
            tay                             ; 7CEE A8 
            eor PageOne,x                   ; 7CEF 5D 00 01 
            bne BadRam                      ; 7CF2 D0 54 
            tya                             ; 7CF4 98 
            asl                             ; 7CF5 0A 
            bcc WalkBit                     ; 7CF6 90 F3
            
; Clear all RAM 
            txa                             ; 7CF8 8A 
            sta ScratchRam,x                ; 7CF9 95 00 
            sta ScratchRam+$0100,x          ; 7CFB 9D 00 01 
            sta ScratchRam+$0200,x          ; 7CFE 9D 00 02 
            sta ScratchRam+$0300,x          ; 7D01 9D 00 03
             
            sta VectorRam,x                 ; 7D04 9D 00 40 
            sta VectorRam+$0100,x           ; 7D07 9D 00 41 
            sta VectorRam+$0200,x           ; 7D0A 9D 00 42 
            sta VectorRam+$0300,x           ; 7D0D 9D 00 43 
            sta VectorRam+$0400,x           ; 7D10 9D 00 44 
            sta VectorRam+$0500,x           ; 7D13 9D 00 45 
            sta VectorRam+$0600,x           ; 7D16 9D 00 46 
            sta VectorRam+$0700,x           ; 7D19 9D 00 47 
            dex                             ; 7D1C CA
             
            bne NextByte                    ; 7D1D D0 CA
             
            sta ResetWDog                   ; 7D1F 8D 00 34 - Reset watchdog timer
            
; Fill page 1 with an incrementing count and check it reads back ok
; quit the test early if the selftest switch is active
; on entry:
;  X = 0
;
L07D22      txa                				; 7D22 8A 
TestNextByte
    		eor PageOne,x       			; 7D23 5D 00 01 
            bne BadRam         				; 7D26 D0 20 
            sta PageOne,x       			; 7D28 9D 00 01 
            inx                				; 7D2B E8 
            bit TestSwitchActive			; 7D2C 2C 07 20 
            bpl TestSwitchActive			; 7D2F 10 05 
            txa                				; 7D31 8A 
            bne TestNextByte    			; 7D32 D0 EF 
            beq L07D3C         				; 7D34 F0 06


; Self test switch active
;L07D36
TestSwitchActive
	      	cpx #$FB           				; 7D36 E0 FB 
            bcc L07D22         				; 7D38 90 E8 
            ldx #$00           				; 7D3A A2 00 
L07D3C      txa                				; 7D3C 8A 
            eor ScratchRam,x   				; 7D3D 55 00 
            bne L07D48         				; 7D3F D0 07 
            lda #$11           				; 7D41 A9 11 
L07D43      sta ScratchRam,x   				; 7D43 95 00 
            tay                				; 7D45 A8 
            eor ScratchRam,x   				; 7D46 55 00
          
          
            
;-------------------------------------------------------------------------
; Memory test error? Stuck bit?
;
BadRam
L07D48      bne BadRam2        				; 7D48 D0 41 
            tya                				; 7D4A 98 
            asl               				; 7D4B 0A 
            bcc L07D43         				; 7D4C 90 F5 
            ldy #$00           				; 7D4E A0 00 
            sty ScratchRam,x   				; 7D50 94 00 
            dex                				; 7D52 CA 
            bne L07D3C         				; 7D53 D0 E7 
            sta ResetWDog      				; 7D55 8D 00 34 
            lda #$02           				; 7D58 A9 02 
L07D5A      sta $0001         				; 7D5A 85 01 
L07D5C      tya                				; 7D5C 98 
            eor (ScratchRam),y 				; 7D5D 51 00 
            bne L07D8F         				; 7D5F D0 2E 
            lda #$11           				; 7D61 A9 11 
L07D63      sta (ScratchRam),y 				; 7D63 91 00 
            tax                				; 7D65 AA 
            eor (ScratchRam),y 				; 7D66 51 00 
            bne L07D8F         				; 7D68 D0 25 
            sta (ScratchRam),y 				; 7D6A 91 00 
            txa                				; 7D6C 8A 
            asl                				; 7D6D 0A 
            bcc L07D63         				; 7D6E 90 F3 
            iny               				; 7D70 C8 
L07D71      bne L07D5C         				; 7D71 D0 E9 
            sta ResetWDog      				; 7D73 8D 00 34 
            inc $0001         				; 7D76 E6 01 
            ldx $0001         				; 7D78 A6 01 
            cpx #$04           				; 7D7A E0 04 
            bcc L07D5C         				; 7D7C 90 DE 
            lda #$40           				; 7D7E A9 40 
            cpx #$40           				; 7D80 E0 40 
            bcc L07D5A         				; 7D82 90 D6 
            cpx #$48           				; 7D84 E0 48 
            bcc L07D5C         				; 7D86 90 D4 
            bcs L07DFC         				; 7D88 B0 72 

; One or other of the previous two instructions will branch, so we never
; get here.
            .byt $64						; 7D8A 64

; Looks like we end up here when a RAM bit error has been detected
BadRam2
L07D8B      ldy #$00           				; 7D8B A0 00 
            beq L07D9D         				; 7D8D F0 0E 
L07D8F      ldy #$00           				; 7D8F A0 00 
            ldx $0001         				; 7D91 A6 01 
            cpx #$04           				; 7D93 E0 04 
            bcc L07D9D         				; 7D95 90 06 
            iny                				; 7D97 C8 
            cpx #$44           				; 7D98 E0 44 
            bcc L07D9D         				; 7D9A 90 01 
            iny                				; 7D9C C8 
            
L07D9D      cmp #$10           				; 7D9D C9 10 
            rol                				; 7D9F 2A 
            and #$1F           				; 7DA0 29 1F 
            cmp #$02           				; 7DA2 C9 02 
            rol                				; 7DA4 2A 
            and #$03           				; 7DA5 29 03
             
L07DA7      dey                				; 7DA7 88 
            bmi L07DAE         				; 7DA8 30 04 
            asl                				; 7DAA 0A 
            asl                				; 7DAB 0A 
            bcc L07DA7         				; 7DAC 90 F9
             
L07DAE      lsr                				; 7DAE 4A 

            ldy #$07           				; 7DAF A0 07     - Fast POT, Enable key scan, Enable debounce
            sty PKY_SKCTL      				; 7DB1 8C 0F 2C 
            ldx #$20           				; 7DB4 A2 20 
            bcc L07DBA         				; 7DB6 90 02 
            ldx #$80           				; 7DB8 A2 80     - Divide by 129
L07DBA      stx PKY_AUDF1      				; 7DBA 8E 00 2C 

            ldx #$A8           				; 7DBD A2 A8     - Pure tone - N - 2, Half volume
            stx PKY_AUDC1      				; 7DBF 8E 01 2C 
            
            ldx #$00           				; 7DC2 A2 00 
L07DC4      bit ThreeKHz           			; 7DC4 2C 01 20 
            bpl L07DC4         				; 7DC7 10 FB 
L07DC9      bit ThreeKHz           			; 7DC9 2C 01 20 
            bmi L07DC9         				; 7DCC 30 FB 
            dex                				; 7DCE CA 
            sta ResetWDog      				; 7DCF 8D 00 34 
            bne L07DC4         				; 7DD2 D0 F0 
            dey                				; 7DD4 88 
            bpl L07DC4         				; 7DD5 10 ED 
            stx PKY_AUDC1         			; 7DD7 8E 01 2C 
            ldy #$08           				; 7DDA A0 08 
L07DDC      bit ThreeKHz           			; 7DDC 2C 01 20 
            bpl L07DDC         				; 7DDF 10 FB 
L07DE1      bit ThreeKHz           			; 7DE1 2C 01 20 
            bmi L07DE1         				; 7DE4 30 FB 
            dex                				; 7DE6 CA 
            sta ResetWDog      				; 7DE7 8D 00 34 
            bne L07DDC         				; 7DEA D0 F0 
            dey                				; 7DEC 88 
            bne L07DDC         				; 7DED D0 ED 
            tax                				; 7DEF AA 
            bne L07DAE         				; 7DF0 D0 BC
             
L07DF2      sta ResetWDog      				; 7DF2 8D 00 34 
            lda SelfTestSwitch 				; 7DF5 AD 07 20  - Top bit only used. Assume lower 7 bits will read as 0 
            bmi L07DF2         				; 7DF8 30 F8     
L07DFA      bpl L07DFA         				; 7DFA 10 FE     - Loop forever if top bit not set and bottom bits not 0 (should never happen)

L07DFC      lda #$00           				; 7DFC A9 00 
            tay                				; 7DFE A8 
            tax                				; 7DFF AA 
            lda #$48           				; 7E00 A9 48 
L07E02      sta $000A         				; 7E02 85 0A 
            lda #$07           				; 7E04 A9 07 
            sta $000C         				; 7E06 85 0C 
            lda #$55           				; 7E08 A9 55 
            clc                				; 7E0A 18 
L07E0B      adc ($0009),y     				; 7E0B 71 09 
            iny                				; 7E0D C8 
            bne L07E0B         				; 7E0E D0 FB 
            inc $000A         				; 7E10 E6 0A 
            dec $000C         				; 7E12 C6 0C 
            bpl L07E0B         				; 7E14 10 F5 
            sta $0010,x       				; 7E16 95 10 
            inx                				; 7E18 E8 
            sta ResetWDog      				; 7E19 8D 00 34 
            lda $000A         				; 7E1C A5 0A 
            cmp #$58           				; 7E1E C9 58 
            bcc L07E02         				; 7E20 90 E0 
            bne L07E26         				; 7E22 D0 02 
            lda #$60           				; 7E24 A9 60 
L07E26      cmp #$80           				; 7E26 C9 80 
            bcc L07E02         				; 7E28 90 D8 
            sta PageThree         			; 7E2A 8D 00 03 
            sta BankSel       				; 7E2D 8D 04 3C 
            cmp $0200         				; 7E30 CD 00 02 
            beq L07E37         				; 7E33 F0 02 
            inc $001A         				; 7E35 E6 1A 
L07E37      lda PageThree         			; 7E37 AD 00 03 
            beq L07E3E         				; 7E3A F0 02 
            inc $001A         				; 7E3C E6 1A




             
L07E3E      lda #$10           				; 7E3E A9 10 
            sta $0001         				; 7E40 85 01 
            sta BankSel        				; 7E42 8D 04 3C 
            ldx #$24           				; 7E45 A2 24

L07E47      lda ThreeKHz           			; 7E47 AD 01 20 
            bpl L07E47         				; 7E4A 10 FB

L07E4C      lda ThreeKHz           			; 7E4C AD 01 20 
            bmi L07E4C         				; 7E4F 30 FB 
            dex                				; 7E51 CA 
            bpl L07E47         				; 7E52 10 F3
 
L07E54      bit VGHalted       				; 7E54 2C 02 20 
            bmi L07E54         				; 7E57 30 FB 

            sta ResetWDog      				; 7E59 8D 00 34 

            lda #$00           				; 7E5C A9 00 
            sta $0003         				; 7E5E 85 03 
            lda #$40           				; 7E60 A9 40 
            sta $0004         				; 7E62 85 04 

            lda SelfTestSwitch 				; 7E64 AD 07 20 
            and #$80           				; 7E67 29 80 
            bne L07E71         				; 7E69 D0 06 
            sta $01FF         				; 7E6B 8D FF 01 
            jmp $6000          				; 7E6E 4C 00 60 



; Selftest
L07E71      lda $001A         				; 7E71 A5 1A 
            beq L07E7C         				; 7E73 F0 07 
            ldx #$CC           				; 7E75 A2 CC 
            lda #$57           				; 7E77 A9 57 
            jsr L07A18         				; 7E79 20 18 7A 
L07E7C      ldx #$96           				; 7E7C A2 96 
            stx $000D         				; 7E7E 86 0D 
            ldx #$05           				; 7E80 A2 05 
L07E82      lda $0010,x       				; 7E82 B5 10 
            beq L07EA7         				; 7E84 F0 21 
            stx $000C         				; 7E86 86 0C 
            ldx $000D         				; 7E88 A6 0D 
            txa                ; 7E8A 8A 
            sec                ; 7E8B 38 
            sbc #$08           ; 7E8C E9 08 
            sta $000D         ; 7E8E 85 0D 
            lda #$20           ; 7E90 A9 20 
            jsr L07AE5         ; 7E92 20 E5 7A 
            ldx $000C         ; 7E95 A6 0C 
            ldy L07FEE,x       ; 7E97 BC EE 7F 
            jsr L079F8         ; 7E9A 20 F8 79 
            ldx $000C         ; 7E9D A6 0C 
            ldy L07FF4,x       ; 7E9F BC F4 7F 
            jsr L079F8         ; 7EA2 20 F8 79 
            ldx $000C         ; 7EA5 A6 0C 
L07EA7      dex                ; 7EA7 CA 
            bpl L07E82         ; 7EA8 10 D8 
            lda #$57           ; 7EAA A9 57 
            ldx #$44           ; 7EAC A2 44 
            jsr L07A18         ; 7EAE 20 18 7A 
            lda #$93           ; 7EB1 A9 93 
            ldx #$A0           ; 7EB3 A2 A0 
            jsr L07AE5         ; 7EB5 20 E5 7A 
            ldx #$03           ; 7EB8 A2 03 
            stx $000C         ; 7EBA 86 0C 
            jsr L07CC2         ; 7EBC 20 C2 7C 
            dec $000C         ; 7EBF C6 0C 
            lda OptionSwitch65 ; 7EC1 AD 01 28 
            pha                ; 7EC4 48 
            and #$01           ; 7EC5 29 01 
            jsr L07CCE         ; 7EC7 20 CE 7C 
            pla                ; 7ECA 68 
            and #$02           ; 7ECB 29 02 
            lsr                ; 7ECD 4A 
            jsr L07CCE         ; 7ECE 20 CE 7C 
            jsr L07CC5         ; 7ED1 20 C5 7C 
            lda #$93           ; 7ED4 A9 93 
            ldx #$B0           ; 7ED6 A2 B0 
            jsr L07AE5         ; 7ED8 20 E5 7A 
            lda #$07           ; 7EDB A9 07 
            sta PKY_SKCTL         ; 7EDD 8D 0F 2C 
            sta PKY_POTGO         ; 7EE0 8D 0B 2C 
            lda PKY_ALLPOT         ; 7EE3 AD 08 2C 
            eor #$FF           ; 7EE6 49 FF 
            sta $000C         ; 7EE8 85 0C 
            sta $008D         ; 7EEA 85 8D 
            asl                ; 7EEC 0A 
            rol                ; 7EED 2A 
            rol                ; 7EEE 2A 
            rol                ; 7EEF 2A 
            rol $000D         ; 7EF0 26 0D 
            and #$07           ; 7EF2 29 07 
            jsr L07CCE         ; 7EF4 20 CE 7C 
            lda $000D         ; 7EF7 A5 0D 
            and #$01           ; 7EF9 29 01 
            jsr L07CCE         ; 7EFB 20 CE 7C 
            lda $000C         ; 7EFE A5 0C 
            lsr                ; 7F00 4A 
            lsr                ; 7F01 4A 
            and #$03           ; 7F02 29 03 
            jsr L07CCE         ; 7F04 20 CE 7C 
            lda $000C         ; 7F07 A5 0C 
            and #$03           ; 7F09 29 03 
            jsr L07CCE         ; 7F0B 20 CE 7C 
            jsr L07C34         ; 7F0E 20 34 7C 
            ldy $006E         ; 7F11 A4 6E 
            lda #$96           ; 7F13 A9 96 
            ldx #$94           ; 7F15 A2 94 
            iny                ; 7F17 C8 
            jsr L07C71         ; 7F18 20 71 7C 
            lda #$10           ; 7F1B A9 10 
            sta $0001         ; 7F1D 85 01 
            lda $00F9         ; 7F1F A5 F9 
            bmi L07F2D         ; 7F21 30 0A 
            lda #$8E           ; 7F23 A9 8E 
            ldx #$83           ; 7F25 A2 83 
            jsr L07AE5         ; 7F27 20 E5 7A 
            jsr L07C90         ; 7F2A 20 90 7C 
L07F2D      lda $00D4         ; 7F2D A5 D4 
            beq L07F38         ; 7F2F F0 07 
            ldx #$CB           ; 7F31 A2 CB 
            lda #$F4           ; 7F33 A9 F4 
            jsr L07CD5         ; 7F35 20 D5 7C 
L07F38      inc $0076         ; 7F38 E6 76 

; Note, buttons are active low, so will read as a "1" (b1xxxxxxx) when not
; being pressed.
            lda FireSwitch     ; 7F3A AD 04 20 
            and ThrustSwitch   ; 7F3D 2D 05 24 
            and RotLeftSwitch  ; 7F40 2D 07 24 
            and RotRightSwitch ; 7F43 2D 06 24 
            bpl L07F57         ; 7F46 10 0F 
            bit $0021         ; 7F48 24 21 
            bmi L07F57         ; 7F4A 30 0B 
            sta $00CD         ; 7F4C 85 CD 
            sta $0021         ; 7F4E 85 21 
L0FF50      ldx #$15           ; 7F50 A2 15 
            stx $00CC         ; 7F52 86 CC 
            dex                ; 7F54 CA 
            stx $00CB         ; 7F55 86 CB 
L07F57      lda $0021         ; 7F57 A5 21 
            beq L07F77         ; 7F59 F0 1C 
            lda $00CD         ; 7F5B A5 CD 
            beq L07F77         ; 7F5D F0 18 
            lda #$94           ; 7F5F A9 94 
            ldx #$72           ; 7F61 A2 72 
            jsr L07AE5         ; 7F63 20 E5 7A 
            ldx #$F2           ; 7F66 A2 F2 
            lda #$57           ; 7F68 A9 57 
            jsr L07A18         ; 7F6A 20 18 7A 
            jsr L07AFF         ; 7F6D 20 FF 7A 
            lda $0078         ; 7F70 A5 78 
            clc                ; 7F72 18 
            adc #$04           ; 7F73 69 04 
            sta $0078         ; 7F75 85 78 
L07F77      lda #$7F           ; 7F77 A9 7F 
            tax                ; 7F79 AA 
            jsr L07A1F         ; 7F7A 20 1F 7A 
            jsr L079DA         ; 7F7D 20 DA 79 
            ldy #$08           ; 7F80 A0 08 
            lda #$00           ; 7F82 A9 00 
L07F84      sta L02BFF,y       ; 7F84 99 FF 2B 
            dey                ; 7F87 88 
            bne L07F84         ; 7F88 D0 FA 	- NOT OK
            jsr L07FA4         ; 7F8A 20 A4 7F 
            ora $0088         ; 7F8D 05 88 
            ora $0087         ; 7F8F 05 87 
            ora $0086         ; 7F91 05 86 
            beq L07F97         ; 7F93 F0 02  	- OK
            lda #$A4           ; 7F95 A9 A4 
L07F97      sta PKY_AUDC1         ; 7F97 8D 01 2C 
            lsr                ; 7F9A 4A 
            sta PKY_AUDF1          ; 7F9B 8D 00 2C 
            sta StartVG        ; 7F9E 8D 00 30  - Tell the DVG to draw
            jmp L07E3E         ; 7FA1 4C 3E 7E 



            
L07FA4      jsr L07FA7         ; 7FA4 20 A7 7F 
L07FA7      ldx #$07           ; 7FA7 A2 07 
L07FA9      rol LCoinSwitch,x  ; 7FA9 3E 00 24 
            ror                ; 7FAC 6A 
            dex                ; 7FAD CA 
            bpl L07FA9         ; 7FAE 10 F9 
            jsr L07FC4         ; 7FB0 20 C4 7F 
            ldx #$04           ; 7FB3 A2 04 
L07FB5      rol ShieldSwitch,x ; 7FB5 3E 03 20 
            ror                ; 7FB8 6A 
            dex                ; 7FB9 CA 
            bpl L07FB5         ; 7FBA 10 F9 
            stx BankSel        ; 7FBC 8E 04 3C 
            ldx #$64           ; 7FBF A2 64 
L07FC1      dex                ; 7FC1 CA 
            bpl L07FC1         ; 7FC2 10 FD 
L07FC4      tax                ; 7FC4 AA 
            eor $0016,y       ; 7FC5 59 16 00 
            sta $0086,y       ; 7FC8 99 86 00 
            stx $0016,y       ; 7FCB 96 16 
            iny                ; 7FCD C8 
            rts                ; 7FCE 60 
L07FCF      lda $0076         ; 7FCF A5 76 
            and #$03           ; 7FD1 29 03 
            tax                ; 7FD3 AA 
            lda PKY_RANDOM         ; 7FD4 AD 0A 2C 
            sta $00D5,x       ; 7FD7 95 D5 
            ldy #$00           ; 7FD9 A0 00 
            ldx #$04           ; 7FDB A2 04 
L07FDD      cmp $00D4,x       ; 7FDD D5 D4 
            bne L07FE2         ; 7FDF D0 01 
            iny                ; 7FE1 C8 
L07FE2      dex                ; 7FE2 CA 
            bne L07FDD         ; 7FE3 D0 F8 
            cpy #$04           ; 7FE5 C0 04 
            lda PKY_ALLPOT         ; 7FE7 AD 08 2C 
            ror                ; 7FEA 6A 
            sta $00D4         ; 7FEB 85 D4 
            rts                ; 7FED 60 

L07FEE	    .byt $38
			.byt $30
			
			.byt $1c
			.byt $1e
			.byt $24
			.byt $28
L07FF4		.byt $06
			.byt $06
            .byt $04
            .byt $04 
            .byt $04
            .byt $04 
            
*		    = $7ffa
            
NMIVec      .word NMI_ISR      ; 7FFA 51 78 
RSTVec      .word RESET_ISR    ; 7FFC E0 7C 
IRQVec      .word RESET_ISR    ; 7FFE E0 7C 
