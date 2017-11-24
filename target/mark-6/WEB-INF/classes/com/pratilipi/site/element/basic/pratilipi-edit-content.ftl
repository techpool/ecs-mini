<script>
	$( document ).ready(function() {
		<#if ( pratilipi.getType()?? ) >
		    $("#type").val("${ pratilipi.getType() }");
	    </#if>	
		<#if ( pratilipi.getLanguage()?? ) >
		    $("#language").val("${ pratilipi.getLanguage() }");
	    </#if>	        
	});
    function isEmpty(str) {
    	return ( str.length === 0 || !str.trim() );
    }
    
	function appendErrorMsg(origMsg, newMsg) {
		if ( isEmpty( origMsg ) ) {
			origMsg += newMsg;
		}
		else {
			origMsg += ( "\n" + newMsg );
		}
		return origMsg;
	}
	
	function validateSettingsForm() {
    	var form = $("#pratilipi_settings_form");
    	var error_message = "";
    	if( isEmpty( form.find( "#title" ).val() ) && isEmpty( form.find( "#titleEn" ).val() ) ) {
    		var title_error = "Please provide a title in english or other language.";
    		error_message = appendErrorMsg(error_message, title_error);
    	}
    	
    	if( isEmpty( form.find( "#type" ).val() ) ) {
    		var content_type_error = "Please select the type of content.";
    		error_message = appendErrorMsg(error_message, content_type_error);
    	}
    	
    	if( isEmpty( error_message ) ) {
    		AjaxSubmitForm();
    	}
    	else {
    		window.alert(error_message);
    	}
    }
	function AjaxSubmitForm() {
		var form = $("#pratilipi_settings_form");
        $.ajax({
            type:'POST',
            url: '/api/pratilipi?_apiVer=2',
            data:{ 	pratilipiId: "${pratilipi.getId()?c }",
            		title: form.find("#title").val() ,
            		titleEn: form.find("#titleEn").val(),
            		type: form.find("#type").val(),
            		language: "${ pratilipi.getLanguage() }",
            		summary: form.find("#summary").val()
	               },
            success:function(response){
            	
            	var parsed_data = jQuery.parseJSON( response );
      			window.location = ( window.location.href.split("/")[0] + parsed_data.pageUrl );
            },
            error: function(data){
            	var message = jQuery.parseJSON( response.responseText );
				alert(message);
            }
        });
	}	
</script>
<div class="pratilipi-shadow secondary-500 box">
		<a style="padding:7px;" href="${ pratilipi.getPageUrl() }" class="pull-left pratilipi-grey-button pratilipi-without-margin" ><div class="sprites-icon black-cross-icon"></div></a>
		<button style="padding:7px;" class="pull-right pratilipi-red-button pratilipi-without-margin" onclick="validateSettingsForm()"><div class="sprites-icon red-check-icon"></div></button>
		<h3 class="text-center pratilipi-red" style="margin-top: 10px;"> ${ _strings.pratilipi_edit_info } </h3>
</div>
<div class="pratilipi-shadow secondary-500 box">
	<form id="pratilipi_settings_form">
		<input type="hidden" name="pratilipiId" id="pratilipiId" value="${ pratilipi.getId()?c }">
		<div class="form-group">
			<label for="title">${ _strings.edit_pratilipi_title }</label>
			<input type="text" class="form-control" id="title" name="title" 
			<#if pratilipi.getTitle()?? >
				value="${ pratilipi.getTitle() }"
			</#if>	
			>
		</div>
		
		
		<div class="form-group">
			<label for="titleEn">${ _strings.edit_pratilipi_title_en }</label>
			<input type="text" class="form-control" id="titleEn" name="titleEn"
			<#if pratilipi.getTitleEn()?? >
				value="${ pratilipi.getTitleEn() }"
			</#if>	
			>
		</div>		
			
		<div class="form-group">
		  <label for="type">${ _strings.edit_pratilipi_type }:</label>
		  <select class="form-control" id="type" name="type">
		    <option value="BOOK">${ _strings._pratilipi_type_book }</option>
		    <option value="POEM">${ _strings._pratilipi_type_poem }</option>
		    <option value="STORY">${ _strings._pratilipi_type_story }</option>
		    <option value="ARTICLE">${ _strings._pratilipi_type_article }</option>
		    <option value="MAGAZINE">${ _strings._pratilipi_type_magazine }</option>
		  </select>
		</div>
		
		<fieldset disabled>
			<div class="form-group">
				<label for="language">${ _strings.edit_pratilipi_language }</label>	
			    <select id="language" name="language" class="form-control">
		        	<option value="HINDI">${ _strings.language_hi }</option>
		        	<option value="GUJARATI">${ _strings.language_gu }</option>
		        	<option value="TAMIL">${ _strings.language_ta }</option>
		        	<option value="MARATHI">${ _strings.language_mr }</option>
		        	<option value="MALAYALAM">${ _strings.language_ml }</option>
		        	<option value="BENGALI">${ _strings.language_bn }</option>
		        	<option value="TELUGU">${ _strings.language_te }</option>
		        	<option value="KANNADA">${ _strings.language_kn }</option>
		        </select>
			</div>
		</fieldset>	

		<div class="form-group">
		  	<label for="summary">${ _strings.edit_pratilipi_summary }:</label>
		  	<textarea rows="10" class="form-control" id="summary" name="summary">
		  		<#if pratilipi.getSummary()?? >
					${ pratilipi.getSummary() }
				</#if>
		  	</textarea>
		</div>
		
	</form>
</div>