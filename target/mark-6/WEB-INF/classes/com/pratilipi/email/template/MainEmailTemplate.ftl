<!-- SENDER_NAME: Team Pratilipi -->
<!-- SENDER_EMAIL: contact@pratilipi.com -->

<!DOCTYPE html>
<html>
<body style="padding: 0; margin: 0;">
	<table width="100%" align="center" table-layout="fixed;" style="width: 100%; table-layout: fixed; margin: auto; max-width: 600px;">
		<tr>
			<td>
				<table align="center" width="100%" style="margin: auto; padding: 20px; padding-top: 8px; border: 1px solid #c3c3c3; background-color: #ffffff; width: 100%;">
					<tr>
						<td>
							<table style="margin: 0 auto; width: 100%; text-align: left;" width="100%">
								<tr>
									<td width="60">
										<img width="60" height="60" style="width: 60px; height: 60px;" src="http://public.pratilipi.com/pratilipi-logo/png/Logo-2C-RGB-80px.png" />
									</td>
									<td width="128" align="right" style="width: 128px; text-align: right;">
										<a href="https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_email%26utm_medium%3Dtransactional_email%26utm_campaign%3Dapp_download">
											<img width="128" style="width: 128px;" src="http://public.pratilipi.com/resource-all/image/google-play-badge.png" />
										</a>
									</td>
								</tr>
							</table>
							<table style="width: 100%;">
								<tr>
									<td width="100%">
										${ emailBody }
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<center>
					<div style="font-size: 12px; color:#646464;">
						<br/>
						<br/>
						<p>
							This email was sent to you by Pratilipi, if you don't want to receive such emails, unsubscribe 
							<a href="http://${ language?lower_case }.pratilipi.com/notifications?action=settings" style="color: #646464; text-decoration: underline;" target="_blank">here</a>.
						</p>
						<p>
							Contact us at
							<a href="mailto:${ contact_email }" style="color: #646464;" target="_blank">
								${ contact_email }
							</a>
						</p>
						<br/>
						<p>Jai Plaza Elite, House 1064, 1st Floor, 7th A main, 3rd block,<br/>Koramangala, Bengaluru, Karnataka 560076, India</p>
						<p>
							<a href="http://${ language?lower_case }.pratilipi.com/terms-of-service" style="color: #646464;" target="_blank">Terms of Service</a>
							|
							<a href="http://${ language?lower_case }.pratilipi.com/privacy-policy" style="color: #646464;" target="_blank">Privacy Policy</a>
						</p>
					</div>
				</center>
			</td>
		</tr>
	</table>
</body>
</html>