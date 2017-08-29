<!-- SENDER_NAME: Team Pratilipi -->
<!-- SENDER_EMAIL: contact@pratilipi.com -->

<div subject data-key="email_comment_review_reviewer_subject" style="display: none;">${ email_comment_review_reviewer_subject }</div>

<hr style="border: 1px solid #eeeeee;">
<table align="left" table-layout="fixed;" style="width: 100%; height: 100%;">
	<tr>
		<td>
			<p data-key="email_comment_review_reviewer_heading" style="margin: 15px 0 10px 0; color: #000; font-size: 16px;">${ email_comment_review_reviewer_heading }</p>
		</td>
	</tr>
</table>
<table style="border: solid 1px #eeeeee; border-radius: 2px; padding: 10px; border-collapse: collapse;" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<a href="${"$"}{ reviewer_page_url }" target="_blank">
				<img width="64" src="${"$"}{ reviewer_image_url }" style="border: 0; border-radius: 50%; width: 64px; margin-left: 12px; margin-top: 12px;" />
			</a>
		</td>
		<td width="10">
			&nbsp;&nbsp;&nbsp;
		</td>
		<td width="100%">
			<table style="border-collapse:collapse" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<a href="${"$"}{ reviewer_page_url }" style="color:#000000; text-decoration: none; font-size: 16px; line-height: 21px;" target="_blank">
							${"$"}{ reviewer_name }
						</a>
					</td>
				</tr>
				<tr>
					<td>
						<span style="font-size: 14px; line-height: 19px;">${"$"}{ review_date }</span>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<div style="font-size: 16px; line-height: 21px; color: #000000; margin: 10px 12px;">
				${"$"}{ review_review }
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<hr style="border: 1px solid #eeeeee;">
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<table>
				<tr>
					<td style="width: 10px;" width="10">
						&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<a href="${"$"}{ commentor_page_url }" target="_blank">
							<img src="${"$"}{ commentor_image_url }" style="border: 0; border-radius: 50%; width: 50px;" width="50" />
						</a>
					</td>
					<td style="width: 10px;" width="10">
						&nbsp;&nbsp;&nbsp;
					</td>
					<td width="100%">
						<table style="border-collapse: collapse;" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<a href="${"$"}{ commentor_page_url }" style="color: #000000; text-decoration: none; font-size: 16px; line-height: 21px;" target="_blank">
										${"$"}{ commentor_name }
									</a>
								</td>
							</tr>
							<tr>
								<td>
									<span style="font-size: 14px; line-height: 19px;">${"$"}{ comment_date }</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="width: 12px;" width="12">
						&nbsp;&nbsp;&nbsp;
					</td>
					<td colspan="3">
						<div style="font-size: 15px; line-height: 21px; color: #000000; margin: 10px 12px;">
							${"$"}{ comment_comment }
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table align="center" cellspacing="0" cellpadding="0" table-layout="fixed" style="margin: 0 auto; width:100%;"> 
	<tr>
		<td height="10">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#D0021B" style="-webkit-border-radius: 5px; -moz-border-radius: 5px; border-radius: 5px; color: #ffffff; display: block; width: 100%; margin: auto;">
			<a href="${"$"}{ pratilipi_page_url }" target="_blank" style="font-size: 18px; text-decoration: none; line-height: 40px; width: 100%; display: inline-block;">
				<span data-key="email_comment_review_reviewer_button" style="color: #ffffff; padding-left: 15px; padding-right: 15px;">
					${ email_comment_review_reviewer_button }
				</span>
			</a>
		</td>
	</tr>
	<tr>
		<td height="32">
			&nbsp;
		</td>
	</tr>
</table>