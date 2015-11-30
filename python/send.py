'''
This script can be used to send emails
''' 

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

# sender
sender = sys.argv[2]
print 'From: ', sender

# receiver
recv = sys.argv[3]
print 'To: ', recv

# construct message
message = sys.argv[4]
message = message.replace("_", " ");
message = message.replace("\\n", "<br>");
html = '<html><head><style>h3,p, span {color: black}</style></head><body>' + message + '</body></html>'

# Create a text/plain message
msg = MIMEMultipart('multipart')

# text
txt = MIMEText(html, 'html')
msg.attach(txt)

# attachments
path = sys.argv[1]
text = '.txt';
png = '.png';
fileNames = sys.argv[5:]
for fileName in fileNames:  # this shouldn't break the service even if the file doesn't exist
	try:
		if text in fileName:
			f = file(path + fileName)
			attachment = MIMEText(f.read())
			attachment.add_header('Content-Disposition', 'attachment', filename=fileName)           
			msg.attach(attachment)
		if png in fileName:
			fp = open(path + fileName, 'rb')
			img = MIMEImage(fp.read())
			fp.close()
			msg.attach(img)
	except Exception, e:
		continue

# me == the sender's email address
# you == the recipient's email address
me = sender
you = recv
msg['Subject'] = 'Moto demo'
msg['From'] = me
msg['To'] = you

# Send the message via SMTP server
s = smtplib.SMTP(host='email-smtp.us-west-2.amazonaws.com', port='587')
s.ehlo()
s.starttls()
s.login('AKIAJKR7IGWULDP7ULQQ', 'Ar88H0SSaP4/K5wM+SJ/FEYxKDfNaYbv9nePs66YpLXU')
s.sendmail(me, you, msg.as_string())
s.quit()