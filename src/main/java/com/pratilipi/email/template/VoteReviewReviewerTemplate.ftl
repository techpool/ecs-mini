${r' <!-- <#if rating??><#if rating <= 0.5 ><#assign img_name="0_5"><#elseif rating <= 1 ><#assign img_name="1_0"><#elseif rating <= 1.5 ><#assign img_name="1_5"><#elseif rating <= 2 ><#assign img_name="2_0"><#elseif rating <= 2.5 ><#assign img_name="2_5"><#elseif rating <= 3 ><#assign img_name="3_0"><#elseif rating <= 3.5 ><#assign img_name="3_5"><#elseif rating <= 4 ><#assign img_name="4_0"><#elseif rating <= 4.5 ><#assign img_name="4_5"><#elseif rating <= 5 ><#assign img_name="5_0"></#if></#if> --> '}

<!-- SENDER_NAME: Team Pratilipi -->
<!-- SENDER_EMAIL: contact@pratilipi.com -->

<div subject data-key="email_vote_review_reviewer_subject" style="display: none;">${ email_vote_review_reviewer_subject }</div>

<hr style="border: 1px solid #eeeeee;">
<table align="left" table-layout="fixed;" style="width: 100%; height: 100%;">
	<tr>
		<td>
			<p data-key="email_vote_review_reviewer_heading" style="margin: 15px 0 10px 0; color: #000; font-size: 16px;">${ email_vote_review_reviewer_heading }</p>
		</td>
	</tr>
</table>
<table style="width: 100%; margin-left: auto; margin-right: auto; border: solid 1px #eeeeee; border-radius: 2px; padding: 10px;">
	<tr>
		<td>
			<table align="left" style="border-collapse: collapse;" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<a href="${"$"}{ reviewer_page_url }" target="_blank">
							<img src="${"$"}{ reviewer_image_url }" style="border:0; width: 60px; height: 60px; border-radius: 50%;" />
						</a>
					</td>
					<td width="10">
						&nbsp;&nbsp;&nbsp;
					</td>
					<td width="100%">
						<table style="border-collapse: collapse;">
							<tr>
								<td>
									<a href="${"$"}{ reviewer_page_url }" style="color: #000; text-decoration: none; font-size: 14px; line-height: 19px; font-weight: bold;" target="_blank">
										${"$"}{ reviewer_name }
									</a>
								</td>
							</tr>
							${r' <#if rating??> '}
								<tr>
									<td>
										<img src="http://public.pratilipi.com/email/images/stars_${"$"}{ img_name }.png" style="height: 16px; max-height: 16px;" height="16" />
									</td>
								</tr>
							${r' </#if> '}
							<tr>
								<td>
									<span style="font-size: 14px; line-height: 19px; color: #c7c7c7;">${"$"}{ review_creation_date }</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<#if review_review ??>
				<table style="width: 100%; padding: 12px 0;">
					<tr>
						<td>
							${"$"}{ review_review }
						</td>
					</tr>
				</table>
			</#if>
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
				<span data-key="email_vote_review_reviewer_button" style="color: #ffffff; padding-left: 15px; padding-right: 15px;">
					${ email_vote_review_reviewer_button }
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