<style>
	.menu-item {
		margin:32px 16px;
		display: block;
		text-align: left;
	}
	.menu-item img {
		margin-right: 12px;
		width: 20px;
		height: 20px;
	}
	.menu-item span {
		font-size: 14px;
	}
</style>

<div class="secondary-500 pratilipi-shadow box">
	<a class="menu-item" style="cursor: pointer;" onCLick="shareOnFacebook( 'menu' )">
		<div class="sprites-icon reader-setting-icon fb-black-icon"></div>
		<span>${ _strings.share_on_facebook }</span>
	</a>
	<a class="menu-item" style="cursor: pointer;" onCLick="shareOnTwitter( 'menu' )">
		<div class="sprites-icon reader-setting-icon twitter-black-icon"></div>
		<span>${ _strings.share_on_twitter }</span>
	</a>
	<a class="menu-item" style="cursor: pointer;" onCLick="shareOnGplus( 'menu' )">
		<div class="sprites-icon reader-setting-icon gplus-black-icon"></div>
		<span>${ _strings.share_on_gplus }</span>
	</a>
	<a class="menu-item" style="cursor: pointer;" href="whatsapp://send?text=%22${ pratilipi.getTitle()?url('UTF-8') }%22${ _strings.whatsapp_read_story?url('UTF-8') }%20http%3A%2F%2F${ language?lower_case }.pratilipi.com${ pratilipi.getPageUrl()?url('UTF-8') }%3Futm_source%3Dweb_mini%26utm_campaign%3Dcontent_share%0A${ _strings.whatsapp_read_unlimited_stories }">
		<div class="sprites-icon reader-setting-icon whatsapp-black-icon"></div>
		<span>${ _strings.share_on_whatsapp }</span>
	</a>	
</div>