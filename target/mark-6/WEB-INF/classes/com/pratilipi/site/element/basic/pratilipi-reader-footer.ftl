
<script>
	function pageNoChanged() {
		var pageNo = document.getElementById( 'pageNoInput' ).value;
		if( pageNo >=1 && pageNo <= ${ pageCount } )
			gotoPage( pageNo );
		else
			document.getElementById( 'pageNoInput' ).value = ${ pageNo };
	}
	$( document ).ready(function() {
		$( "#pageNoInput" ).after( " / ${ pageCount }" );
	});
</script>

<div class="div-center text-center" style="padding: 15px;">
	<#if pageNo gt 1>
		<a onClick="loadPrevious()" style="display: inline-block; cursor: pointer;">
			<div class="sprites-icon reader-navigation-icon chevron-left-icon"></div>
		</a>
	</#if>
	<div style="border: 1px solid #000; width: 98px; line-height: 1.4; display: inline-block; text-align: left;">
		<input id="pageNoInput" type="number" style="width: 48px;" value="${ pageNo }" onChange="pageNoChanged()">
	</div>
	<#if pageNo lt pageCount>
		<a onClick="loadNext()" style="display: inline-block; cursor: pointer;">
			<div class="sprites-icon reader-navigation-icon chevron-right-icon"></div>
		</a>
	</#if>
</div>

