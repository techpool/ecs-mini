<script>
	  function AddToLibrary(bookId, addToLib){
	    $.ajax({type: "POST",
	            url: "/api/userpratilipi/library",
	            data: { pratilipiId: bookId, addedToLib: addToLib },
	            success:function(response){
	            	
	            	var parsed_data = jQuery.parseJSON( response );
	      			if ( parsed_data.addedToLib == addToLib ) {
	      				window.location.reload();
	      			}
	      			else {
	      				alert( "${ _strings.server_error_message }" );
	      			}
	    		},
	            fail:function(response){
					alert( "${ _strings.server_error_message }" );
	    		}			    		
	    		
	    });
	  }
</script>
<#list publishedPratilipiList as pratilipi>
	<#include "pratilipi-pratilipi-card-mini.ftl">
</#list>