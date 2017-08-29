
<style>
	.reader-icon {
		width: 24px;
		height: 24px;
		margin-top: 14px;
		margin-left: 10px;
		margin-right: 10px;
	}
</style>

<div class="secondary-500 pratilipi-shadow" style="display: block; padding: 5px; height: 64px;">
	<table style="min-width: 100%;">
		<tr>
			<td align="left">
				<#-- Back Button -->
				<a onClick="exitReader()" style="cursor: pointer;"><div class="sprites-icon reader-sprite-icon black-arrow-left-icon"></div></a>
				<#-- Index Button if content has Index -->
				<#if indexList?? && indexList?has_content>
					<a onClick="gotoNavigation()" style="cursor: pointer;"><div class="sprites-icon reader-sprite-icon list-icon"></div></a>
				</#if>
			</td>
			<td align="right">
				<#-- Write Review -->
				<a <#if user.isGuest() == true>href="/login?ret=${ pratilipi.getPageUrl() }?review=write%26ret=/${ pageUrl }?id=${ pratilipi.getId()?c }%26pageNo=${ pageNo }"<#else>href="${ pratilipi.getPageUrl() }?review=write&ret=/${ pageUrl }?id=${ pratilipi.getId()?c }%26pageNo=${ pageNo }"</#if> >
					<div class="sprites-icon reader-sprite-icon edit-icon"></div>
				</a>
				<#-- Share buttons -->
				<#if pratilipi.getState() == "PUBLISHED" >
					<a onClick="gotoShare()" style="cursor: pointer;">
						<div class="sprites-icon reader-sprite-icon share-icon"></div>
					</a>
				</#if>	
				<#-- Menu button -->
				<a onClick="gotoSetting()" style="cursor: pointer;">
					<div class="sprites-icon reader-sprite-icon settings-icon"></div>
				</a>
			</td>
		</tr>
	</table>
</div>