<script>
	function confirmAndChangePratilipiState(bookId, state) {
		var confirm_dialog;
		if( state == "DELETED" ) {
			confirm_dialog = "${ _strings.pratilipi_confirm_delete_content }";
		}
		else if( state == "DRAFTED" ) {
			confirm_dialog = "${ _strings.pratilipi_confirm_move_to_drafts_body }";
		}
		
		var confirm_ans = window.confirm( confirm_dialog );
		
		if( confirm_ans ) {
			changePratilipiState(bookId, state);
		}
	}
	
	function changePratilipiState(bookId, state) {
		    $.ajax({type: "POST",
		            url: "/api/pratilipi?_apiVer=2",
		            data: { pratilipiId: bookId, state: state },
		            success:function(response){
		            	var parsed_data = jQuery.parseJSON( response );
		      			if ( parsed_data.state == state ) {
		      				window.location.reload();
		      			}
		      			else {
		      				
		      			}
		    		},
		            fail:function(response){
						alert("Sorry, we could not process your request.");
		    		}			    		
		    		
		    });		
	}
</script>
<div role="tabpanel" class="tab-pane <#if publishedPratilipiList?has_content>active</#if>" id="author-activity">
    <#include "pratilipi-author-drafted-contents.ftl">
	<#include "pratilipi-author-published-contents.ftl">
</div>