<div class="secondary-500 pratilipi-shadow pratilipi-new-header" style="display: block; padding: 0px 5px 0px 5px;border-top: none;">
	<#include "pratilipi-android-launch.ftl">
	<div class="row" style="text-align: center; margin: 0 auto;">
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
			<a href="/">
			<div class="sprites-icon pratilipi-logo-icon">
			</div>
			</a>
		</div>
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2" >
			<a style="display: block;" href="/navigation">
				<div class="sprites-icon header-sprite-icon menu-icon"></div>
			</a>
		</div>
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
			<a style="display: block;" onclick="showPopup();" >
				<div class="sprites-icon header-sprite-icon edit-icon"></div>
			</a>
		</div>
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
			<a style="display: block;" <#if user.isGuest() == true>href="/login?ret=/library"<#else>href="/library"</#if>>
				<div class="sprites-icon header-sprite-icon library-books-icon"></div>
			</a>
		</div>
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2" style="position: relative;">
			<a style="display: block;" <#if user.isGuest() == true>href="/login?ret=/notifications"<#else>href="/notifications?ret=${requestUrl}" </#if>>
				<div class="sprites-icon header-sprite-icon notifications-icon"></div>
			</a>
		</div>				
		<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
			<a style="display: block;" <#if user.isGuest() == true>href="/login?ret=${ requestUrl }"<#else>href="${ user.getProfilePageUrl() }"</#if>>
				<#if user.isGuest() == true>
					<div class="sprites-icon header-sprite-icon account-circle-icon"></div>
				<#else>
					<img style="width: 24px; height: 24px; margin: 10px auto;" src="${ user.getProfileImageUrl( 24 ) }" />
				</#if>
				
			</a>
		</div>
	</div>
		<div class="row">
	  	  <div class="pull-left" style="padding-left: 15px;padding-right: 15px;">	
	  		<div class="form-group" style="margin-bottom: 5px;">
			  <select class="form-control" id="select-language" style="box-shadow:none;padding:0px;border:none;color:red;height:30px;">
			  	<option selected disabled>${ _strings.pratilipi }</option>
			  </select>
			</div>
		  </div>
		  <div style="padding-left: 2px;padding-right: 15px;">
			<form style="" method="get" action="/search">
				<div class="form-group" style="margin-bottom:0px;">
					<div class="input-group">
						<input type="text" class="form-control" name="q" placeholder="${ _strings.search }" style="display: table-cell; width:100%;" maxlength="120" <#if pratilipiListSearchQuery?? >value="${ pratilipiListSearchQuery }"</#if> >

						<div style="background: #f5f5f5;    padding: 3px 4px 3px 5px;" class="input-group-addon">
						    <button class="search-button" type="submit">
							    <div class="sprites-icon search-icon"></div>
						    </button>
						</div>
					</div>
				</div>
			</form>
	   </div>		
	</div>
</div>
<script>
	$( document ).ready(function() {
	  	function generateLanguageOptions() {
			var $select = $("#select-language");
			var language_map = ${ languageMap };
			$.each(language_map, function( key, value ) {
			  	var $option = $("<option>",{
			  		value: key.toLowerCase(),
			  	}).html(value);
			  	$select.append( $option );
			});
		}
		generateLanguageOptions();
	});
	$( "#select-language" ).change(function() {
		var $this = $(this);
		window.location = ( "https://" + $this.val() + ".pratilipi.com" );
		
	});
	function showPopup() {
		window.alert( "${ _strings.write_on_desktop_only }" );
	}
</script>
