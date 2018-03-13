
import RPi.GPIO as GPIO
 
# Set GPIO fan is connected to
gpio=19

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(gpio,GPIO.IN)

def main():
	
	if (GPIO.input(gpio) == 0):
		print ("0")
	else:
		print ("1")
		
		
main()
