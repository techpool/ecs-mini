<div style="display: none;background-color: white;padding: 10px;box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12), 0 3px 1px -2px rgba(0, 0, 0, 0.2);" id="androidAlert" class="alert alert-dismissible fade in android-banner" role="alert">
		<button style="outline: none; top:0;right: 0px; opacity: 1;" type="button" class="close pull-right" data-dismiss="alert" aria-label="Close" onclick="androidBannerCrossed();" on-click="hide">
			<iron-icon icon="icons:close"></iron-icon> 
		</button>
  <div style="display: flex">
    <img style="width: 44px; height: 44px; align-self: center;" onclick="androidBannerClicked(); window.open( 'https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_main_web%26utm_medium%3Dweb_bottom_strip%26utm_campaign%3Dapp_download' );" src="http://public.pratilipi.com/pratilipi-logo/png/Logo-2C-RGB-80px.png"/>
    <div class="">
      <h4 style="margin-top: 5px;margin-bottom: 0;font-size: <#if language == "TAMIL" || language == "MALAYALAM">16px;<#else>20px;</#if>" onclick="androidBannerClicked(); window.open( 'https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_main_web%26utm_medium%3Dweb_bottom_strip%26utm_campaign%3Dapp_download' );">${ _strings.pratilipi_android_application }</h4>
      <img style="width: 90px;height: 18px;"  src="http://public.pratilipi.com/images/Stars-for-App-Install-Strip.png" alt="">
    </div>
  </div>
  <h5 style="color: rgba(0, 0, 0, 0.541);font-size: <#if language == "TAMIL">13px;<#else>16px;</#if>">${ _strings.read_stories_without_internet }</h5>
  <button type="button" name="button" class="pratilipi-light-blue-button" style="width: 100%;font-size: 16px;border: 0;background: #00a651;color: white;text-shadow: none;" onclick="androidBannerClicked(); window.open( 'https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_main_web%26utm_medium%3Dweb_bottom_strip%26utm_campaign%3Dapp_download' );">${ _strings.android_download }</button>
</div> 