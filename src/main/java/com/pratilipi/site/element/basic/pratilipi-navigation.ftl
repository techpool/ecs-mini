
<style>
	.navigation {
		padding-top: 10px;
		padding-bottom: 10px;
		display: block;
		text-align: center;
	}
</style>

<div class="secondary-500 pratilipi-shadow box">
	<#list navigationList as navigation>
		<h3 style="margin-top: 20px;" class="pratilipi-red text-center">${ navigation.getTitle() }</h3>
			<#list navigation.linkList as Link>
				<a class="navigation" href="${ Link.getUrl() }">${ Link.getName() }</a>
			</#list>
			<div style="min-height: 10px;"></div>
	</#list>
</div>