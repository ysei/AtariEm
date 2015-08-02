ScratchRam      		= $0000

PageOne					= $0100
PageThree				= $0300

ThreeKHz        		= $2001
VGHalted        		= $2002
ShieldSwitch    		= $2003
FireSwitch      		= $2004
SlamSwitch      		= $2006

SelfTestSwitch  		= $2007
LCoinSwitch     		= $2400
CCoinSwitch     		= $2401
RCoinSwitch     		= $2402
OnePlayerStartSwitch   	= $2403
TwoPlayerStartSwitch   	= $2404
ThrustSwitch    		= $2405
RotRightSwitch  		= $2406
RotLeftSwitch   		= $2407

OptionSwitch87  		= $2800
OptionSwitch65  		= $2801
OptionSwitch43  		= $2802
OptionSwitch21  		= $2803

; POKEY Write registers
PKY_AUDF1       		= $2C00
PKY_AUDC1       		= $2C01
PKY_AUDF2       		= $2C02
PKY_AUDC2       		= $2C03
PKY_AUDF3       		= $2C04
PKY_AUDC3       		= $2C05
PKY_AUDF4       		= $2C06
PKY_AUDC4       		= $2C07
PKY_AUDCTL      		= $2C08
PKY_STIMER      		= $2C09
PKY_SKRES       		= $2C0A
PKY_POTGO       		= $2C0B
PKY_SEROUT      		= $2C0D
PKY_IRQEN       		= $2C0E
PKY_SKCTL       		= $2C0F

; POKEY Read registers
PKY_POT0        		= $2C00
PKY_POT1        		= $2C01
PKY_POT2        		= $2C02
PKY_POT3        		= $2C03
PKY_POT4        		= $2C04
PKY_POT5        		= $2C05
PKY_POT6        		= $2C06
PKY_POT7        		= $2C07
PKY_ALLPOT      		= $2C08
PKY_KBCODE      		= $2C09
PKY_RANDOM      		= $2C0A
PKY_SERIN       		= $2C0D
PKY_IRQST       		= $2C0E
PKY_SKSTAT      		= $2C0F

EAROM           		= $2C40
StartVG         		= $3000
LatchEA         		= $3200
ResetWDog       		= $3400
ExplosionDat    		= $3600
ResetVG         		= $3800
EACtlLatch      		= $3A00
OnePlayerLED           	= $3C00
TwoPlayerLED           	= $3C01
ThrustSnd       		= $3C03
BankSel         		= $3C04
LCoinCounter    		= $3C05
CCoinCounter    		= $3C06
RCoinCounter    		= $3C07
NoiseGenReset   		= $3E00

VectorRam       		= $4000
VectorROM       		= $4800

Vectors					= $7ffa

RomBase         		= $6000

*						= RomBase

NMI_Vec					nop
RESET_Vec				nop
BRK_Vec					nop

*						= Vectors
						.word	NMI_Vec
						.word	RESET_Vec
						.word	BRK_Vec