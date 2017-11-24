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
	
	<h3 style="margin-top: 20px;" class="pratilipi-red text-center">${ pratilipi.getTitle()!pratilipi.getTitleEn() }</h3>
	
	<#-- Increase and Decrease size buttons -->
	<#if contentType == "PRATILIPI">
		<a class="menu-item" style="cursor: pointer;" onCLick="increaseFontSize()">
			<div class="sprites-icon reader-setting-icon zoomin-icon"></div>
			<span>${ _strings.increase_font_size }</span>
		</a>
		<a class="menu-item" style="cursor: pointer;" onCLick="decreaseFontSize()">
			<div class="sprites-icon reader-setting-icon zoomout-icon"></div>
			<span>${ _strings.decrease_font_size }</span>
		</a>
	<#elseif contentType == "IMAGE">
		<a class="menu-item" style="cursor: pointer;" onCLick="increaseImageSize()">
			<div class="sprites-icon reader-setting-icon zoomin-icon"></div>
			<span>${ _strings.increase_image_size }</span>
		</a>
		<a class="menu-item" style="cursor: pointer;" onCLick="decreaseImageSize()">
			<div class="sprites-icon reader-setting-icon zoomout-icon"></div>
			<span>${ _strings.decrease_image_size }</span>
		</a>
	</#if>
	
	<div style="min-height: 1px;"></div>
	
	<#-- Library Section -->
	<#if user.isGuest() == true>
		<a class="menu-item" href="/login?ret=/${ pageUrl }?id=${ pratilipi.getId()?c }%26addToLib=true%26pageNo=${ pageNo }">
			<div class="sprites-icon reader-setting-icon library-plus-icon"></div>
			<span>${ _strings.add_to_library }</span>
		</a>
	<#else>
		<#if pratilipi.getState() == "PUBLISHED" && ( user.isGuest() || pratilipi.getAuthor().getId() != user.getAuthor().getId() )>
			<#if userpratilipi?? && userpratilipi.isAddedtoLib()??>
				<#if userpratilipi.isAddedtoLib() == true>
					<a class="menu-item" style="cursor: pointer;" onCLick="removeFromLibrary()">
						<div class="sprites-icon reader-setting-icon library-minus-icon"></div>
						<span>${ _strings.remove_from_library }</span>
					</a>							
				<#else>
					<a class="menu-item" style="cursor: pointer;" onClick="addToLibrary()">
						<div class="sprites-icon reader-setting-icon library-plus-icon"></div>
						<span>${ _strings.add_to_library }</span>
					</a>
				</#if>
			<#else>
				<a class="menu-item" style="cursor: pointer;" onClick="addToLibrary()">
					<div class="sprites-icon reader-setting-icon library-plus-icon"></div>
					<span>${ _strings.add_to_library }</span>
				</a>
			</#if>
		</#if>
	</#if>
	
	<a class="menu-item" <#if user.isGuest() == true>href="/login?ret=/library"<#else>href="/library"</#if>>
		<div class="sprites-icon reader-setting-icon my-library-icon"></div>
		<span>${ _strings.my_library }</span>
	</a>
	
	<div style="min-height: 1px;"></div>
	
	<#-- Redirects -->
	<a class="menu-item" href="${ pratilipi.getPageUrl() }">
		<div class="sprites-icon reader-setting-icon description-icon"></div>
		<span>${ _strings.reader_goto_content_page }</span>
	</a>				
	<a class="menu-item" href="${ pratilipi.getAuthor().getPageUrl() }">
		<div class="sprites-icon reader-setting-icon user-icon"></div>
		<span>${ _strings.reader_goto_author_profile }</span>
	</a>
	<a class="menu-item" href="/">
		<div class="sprites-icon reader-setting-icon homepage-icon"></div>
		<span>${ _strings.reader_goto_home_page }</span>
	</a>
</div>