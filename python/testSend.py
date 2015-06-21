#!/usr/bin/python
import smtplib
from email.mime.text import MIMEText

me = 'ningli@vt.edu'
you = 'lininglovebayern@gmail.com'

msg = MIMEText('Test Email')
msg['Subject'] = 'AWS SES Email Test'
msg['From'] = me
msg['To'] = you

s = smtplib.SMTP(host='email-smtp.us-west-2.amazonaws.com', port='587')
s.ehlo()
s.starttls()
s.login('AKIAJKR7IGWULDP7ULQQ', 'Ar88H0SSaP4/K5wM+SJ/FEYxKDfNaYbv9nePs66YpLXU')
print("Sending mail..")
s.sendmail(me, you, msg.as_string())
s.quit()
print("Mail Sent")