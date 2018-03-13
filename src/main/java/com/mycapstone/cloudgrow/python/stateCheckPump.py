
import RPi.GPIO as GPIO
 
# Set GPIO light is connected to
gpio=26

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(gpio,GPIO.IN)

def main():
	
	if (GPIO.input(gpio) == 0):
		print ("0")
	else:
		print ("1")
		
		
main()
