function() {
	/* TODO: Remove hardcoding */
	<#assign current_event_id="">
	<#if language == "HINDI">
		<#assign current_event_id="6497591024943104">
	</#if>
	<#if language == "BENGALI">
		<#assign current_event_id="5998859556749312">
	</#if>
	<#if language == "GUJARATI">
		<#assign current_event_id="5887009884209152">
	</#if>
	<#if language == "MARATHI">
		<#assign current_event_id="6109272160075776">
	</#if>
	<#if language == "TAMIL">
		<#assign current_event_id="6609811322961920">
	</#if>
	<#if language == "TELUGU">
		<#assign current_event_id="5272200650162176">
	</#if>
	<#if language == "KANNADA">
		<#assign current_event_id="4640217838387200">
	</#if>
	<#if language == "MALAYALAM">
		<#assign current_event_id="6013749990260736">
	</#if>

	var self = this;
	var dataAccessor = new DataAccessor();

	var defaultEvent = {
    			"eventId": null,
    			"name": null,
    			"nameEn": null,
    			"language": null,
    			"pageUrl": null,
    			"bannerImageUrl": null,
	};


    this.event = ko.mapping.fromJS( defaultEvent, {}, self.event );
    this.isLoading = ko.observable();

    this.updateEvent = function( event ) {
        if( event == null )
            event = defaultEvent;
        ko.mapping.fromJS( event, {}, self.event );
        MetaTagUtil.setMetaTagsForEvent( ko.mapping.toJS( self.event ) );
    };

	this.fetchEvent = function() {
		self.isLoading( true );
		dataAccessor.getEventById( "${ current_event_id }",
			function( event ) {
				if( event == null ) return;
				self.isLoading( false );
				self.updateEvent( event );
		});
	};

	this.fetchEvent();

}