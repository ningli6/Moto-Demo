'''
This script can be used to send emails

To call this routine:

python send.py sender receiver channels icq gMap message
@param sender
@param receiver 
@param channels
@param icq         whether to plot ic vs q
@param gMap        whether to plot google map
@param message     email content
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
sender = sys.argv[1]
print 'From: ', sender

# receiver
recv = sys.argv[2]
print 'To: ', recv

# Number of channel
nc = int(sys.argv[3])

ICvsQ = sys.argv[4]

gMap = sys.argv[5]

# construct message
message = sys.argv[6]
message = message.replace("_", " ");

html = '<html><head><style>h3,p, span {color: black}</style></head><body>' + message + '</body></html>'


# Create a text/plain message
msg = MIMEMultipart('multipart')
# text
txt = MIMEText(html, 'html')
msg.attach(txt)


# image
if (gMap == 'YgMap'):
	for i in range(nc):
		# fileName = '/var/www/html/Project/output/ec2-user_Demo_probability_0.png'
		fileName = 'C:\Users\Administrator\Desktop\motoPlot\Simulation_result_channel_' + str(i) + '.png'
		fp = open(fileName, 'rb')
		img = MIMEImage(fp.read())
		fp.close()
		msg.attach(img)

if (ICvsQ == 'YICvsQ'):
	fileName = 'C:\Users\Administrator\Desktop\motoPlot\ICvsQ.png'
	fp = open(fileName, 'rb')
	img = MIMEImage(fp.read())
	fp.close()
	msg.attach(img)


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