'''
This script can be used to send emails
We don't need this as long as PHPMailer works fine
Use this method as an alternative
''' 

#!/usr/bin/python

# Import sys for taking in arguments
import sys

# print 'args len: ' , len(sys.argv)
# for i in range(0, len(sys.argv)):
# 	print str(sys.argv[i])

recv = sys.argv[1]
message = ""
for i in range(2, len(sys.argv)):
	message += str(sys.argv[i]) + " "
message = message.rstrip()

import datetime
message += "\n"
message += str(datetime.datetime.now())

# Import smtplib for the actual sending function
import smtplib

# Import the email modules we'll need
from email.mime.text import MIMEText

# Create a text/plain message
msg = MIMEText(message)

me = 'ningli@ec2-52-24-22-108.us-west-2.compute.amazonaws.com'
you = recv

# me == the sender's email address
# you == the recipient's email address
msg['Subject'] = 'Python Send Email Test'
msg['From'] = me
msg['To'] = you

# Send the message via our own SMTP server, but don't include the
# envelope header.
s = smtplib.SMTP('localhost')
s.sendmail(me, [you], msg.as_string())
s.quit()

print ''
print '#####send email to user#####'
print 'From: ', me
print 'To: ', you
print 'Message: ', message
print 'OK'