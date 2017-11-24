<div id="androidSubsribeAlert" class="container alert alert-warning alert-dismissible fade in" style="z-index: 1;margin-bottom: 0; border-radius: 0; cursor: pointer; padding: 0; color: #fff; background-color: #1e1e1e; border: none; margin-left: -5px; margin-right: -5px; display: none;" role="alert">
	<button style="outline: none; top:0;right: 0px; margin-top: 10px; opacity: 1; color: #fff !important;margin-right: 10px;" type="button" class="close pull-right" data-dismiss="alert" aria-label="Close" onclick="androidBannerCrossed();">
		<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
	</button>
	<div style="padding: 7px 7px 7px 10px; width: 95%; width: calc(100% - 23px); display: flex;justify-content: space-between;" onclick="androidBannerClicked(); window.open( 'https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_mini_web%26utm_medium%3Dweb_bottom_strip%26utm_campaign%3Dapp_download' );">
		<span style="font-weight: 400; align-self: center;">
			<span style="font-size: 20px;">${ _strings.pratilipi_app_click_download }</span>
		</span>
		<img style="max-width: 40px; max-height: 40px; margin-right: 12px; align-self: center;" src="https://0.ptlp.co/resource-all/home-page/google_playstore_40.png"/>
	</div>
</div>
