<style>		
	<#if contentType == "PRATILIPI">
		#reader-content * {
			font-size: ${ fontSize }px!important;
			text-align: left!important;
			-webkit-font-smoothing: antialiased;
			line-height: 1.6em!important;
			overflow: hidden;
		}
		#reader-content h1, #reader-content h2 {
			text-align: center;
		}
		#reader-content img {
			max-width: 100%!important;
		}
		#reader-content h2 {
			font-size: ${ fontSize + 4 }px!important;
		}
		#reader-content h1 {
			font-size: ${ fontSize + 8 }px!important;
		}
	<#elseif contentType == "IMAGE">
		#reader-content {
			overflow: scroll;
		}
		#reader-content img {
			width: ${ imageSize }px!important;
		}
	</#if>
	
	.reader-icon-alert {
		width: 24px;
		height: 24px;
		margin-left: 10px;
	}
	
	/* Bootstrap override */
	.alert {
		margin: 64px auto;
	}
	.alert-success {
		color: #333;
		background-color: #ffffff;
		border-color: #333;
	}
	.alert-success .alert-link,.alert-success .alert-link:hover, .alert-success .alert-link:focus {
		color: #107FE5;
		font-weight: inherit;
	}
</style>

<script>
	$( document ).ready(function() {
		$( "#reader-content" ).css( 'min-height', ( window.innerHeight - 64 ) + "px" );
	});
</script>
	
<div class="secondary-500 pratilipi-shadow box" style="margin-bottom: 5px; padding: 16px 22px; width: 100%;">
	<div id="reader-content">
		${ content! }
	</div>
	<#if pageNo == pageCount && pratilipi.getState() == "PUBLISHED" >
		<div class="alert alert-success" role="alert">
			<#if user.isGuest() || pratilipi.getAuthor().getId() != user.getAuthor().getId() >
				<h3 class="text-center" style="margin-top: 24px;">${ _strings.reader_enjoyed_reading_part1 }</h3>
				<div class="text-center div-center">
					<a <#if user.isGuest() == true>href="/login?ret=${ pratilipi.getPageUrl() }?review=write%26ret=/${ pageUrl }?id=${ pratilipi.getId()?c }%26pageNo=${ pageNo }"<#else>href="${ pratilipi.getPageUrl() }?review=write&ret=/${ pageUrl }?id=${ pratilipi.getId()?c }%26pageNo=${ pageNo }"</#if> >
						<h6 style="margin: 15px 0px; display: inline-block;">${ _strings.reader_enjoyed_review_book }</h6>
						<div class="sprites-icon size-24-icon edit-icon"></div>
					</a>
				</div>
			</#if>	
			<div class="text-center div-center">
				<h6 style="margin: 15px;display: inline-block;">${ _strings.reader_enjoyed_reading_part2 } : </h6>
				<div style="white-space: nowrap; display: inline-block;"></div>
					<a style="cursor: pointer;" onCLick="shareOnFacebook( 'container' )">
						<div class="sprites-icon reader-footer-icon fb-black-icon"></div>
					</a>
					<a style="cursor: pointer;" onCLick="shareOnTwitter( 'container' )">
						<div class="sprites-icon reader-footer-icon twitter-black-icon"></div>
					</a>
					<a style="cursor: pointer;" onCLick="shareOnGplus( 'container' )">
						<div class="sprites-icon reader-footer-icon gplus-black-icon"></div>
					</a>
					<a style="cursor: pointer;" href="whatsapp://send?text=%22${ pratilipi.getTitle()?url('UTF-8') }%22${ _strings.whatsapp_read_story?url('UTF-8') }%20http%3A%2F%2F${ language?lower_case }.pratilipi.com${ pratilipi.getPageUrl()?url('UTF-8') }%3Futm_source%3Dweb_mini%26utm_campaign%3Dcontent_share%0A${ _strings.whatsapp_read_unlimited_stories }">
						<div class="sprites-icon reader-footer-icon whatsapp-black-icon"></div>
					</a>
				</div>
			</div>
		</div>
	</#if>
</div>