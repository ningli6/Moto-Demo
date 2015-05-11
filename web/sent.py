'''
This script can be used to send emails
We don't need this as long as PHPMailer works fine
Use this method as an alternative
''' 

# Import smtplib for the actual sending function
import smtplib

# Import the email modules we'll need
from email.mime.text import MIMEText

# Create a text/plain message
message = 'Life is good'
msg = MIMEText(message)

me = 'ningli@ec2-52-24-22-108.us-west-2.compute.amazonaws.com'
you = 'ningli@vt.edu'

# me == the sender's email address
# you == the recipient's email address
msg['Subject'] = 'Test'
msg['From'] = me
msg['To'] = you

# Send the message via our own SMTP server, but don't include the
# envelope header.
s = smtplib.SMTP('localhost')
s.sendmail(me, [you], msg.as_string())
s.quit()
print 'Success!'