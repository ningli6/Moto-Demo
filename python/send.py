'''
This script can be used to send emails
We don't need this as long as PHPMailer works fine
Use this method as an alternative
''' 
print 'Sending email...'

# Import sys for taking in arguments
import sys
# Import smtplib for the actual sending function
import smtplib
# Import the email modules we'll need
from email.mime.text import MIMEText
from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart
import datetime

# print 'args len: ' , len(sys.argv)
# for i in range(0, len(sys.argv)):
# 	print str(sys.argv[i])

recv = sys.argv[1]
message = sys.argv[2]
message = message.replace("_", " ");
message = message.replace("#", "\n");
message += "\n\n"
message += ('Current time: ' + str(datetime.datetime.now()))

# Create a text/plain message
msg = MIMEMultipart()

txt = MIMEText(message, 'plain')
msg.attach(message)

fileName = '/var/www/html/Project/output/ec2-user_Demo_probability_0.png'
# Open the files in binary mode.
fp = open(fileName, 'rb')
img = MIMEImage(fp.read())
fp.close()
msg.attach(img)

# me == the sender's email address
# you == the recipient's email address
me = 'ningli@ec2-52-24-22-108.us-west-2.compute.amazonaws.com'
you = recv
msg['Subject'] = 'Python Send Email Test'
msg['From'] = me
msg['To'] = you

# Send the message via our own SMTP server, but don't include the
# envelope header.
s = smtplib.SMTP('localhost')
s.sendmail(me, [you], msg.as_string())
s.quit()

# print ''
# print '#####send email to user#####'
# print 'From: ', me
# print 'To: ', you
# print message
# print 'OK'