'''
This script can be used to send emails
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
# Import data module
import datetime

# # print 'args len: ' , len(sys.argv)
# # for i in range(0, len(sys.argv)):
# # 	print str(sys.argv[i])

# receiver
recv = sys.argv[1]
print 'Send to: ', recv

# construct message
message = sys.argv[2]
message = message.replace("_", " ");
message = message.replace("#", "\n");
message += "\n\n"
message += ('Current time: ' + str(datetime.datetime.now()))

# Create a text/plain message
msg = MIMEMultipart('multipart')
# text
txt = MIMEText(message, 'plain')
msg.attach(txt)
# image
fileName = '/var/www/html/Project/output/ec2-user_Demo_probability_0.png'
fp = open(fileName, 'rb')
img = MIMEImage(fp.read())
fp.close()
msg.attach(img)

# me == the sender's email address
# you == the recipient's email address
me = 'ningli@vt.edu'
you = recv
msg['Subject'] = 'AWS SES Test'
msg['From'] = me
msg['To'] = you

# Send the message via SMTP server
s = smtplib.SMTP(host='email-smtp.us-west-2.amazonaws.com', port='587')
s.ehlo()
s.starttls()
s.login('AKIAJKR7IGWULDP7ULQQ', 'Ar88H0SSaP4/K5wM+SJ/FEYxKDfNaYbv9nePs66YpLXU')
s.sendmail(me, you, msg.as_string())
s.quit()