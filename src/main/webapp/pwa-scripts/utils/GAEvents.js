/* Google Analytics */
/* 
 * GA has 4 parameters to record:
 * Category, Action, Label, Value
 * 
 * https://drive.google.com/drive/u/1/folders/0B1GuDK5-Y90aVE0zVldHRm95RU0
 * Event Category => The entity being tracked - pratilipi, author, notification, review etc.
 * Event Action => The action the user is taking.
 * Event Label => The page / location from where it is taken.
 * Event Value => Search Keyword only
 * 
 * 
 * Function Names and Definition:
 * ga_CALV => category, action, location, value
 * ga_CAL => category, action, location
 * ga_CA => category, action
 * 
 */

function ga_CALV( category, action, location, value ) {
	if( category == null || action == null || location == null || value == null ) {
		console.log( "ga_CALV". category, action, location, value );
		return;
	}
	ga( 'send', 'event', category, action, location, value );
	return true;
}

function ga_CAL( category, action, location ) {
	if( category == null || action == null || location == null ) {
		console.log( "ga_CAL", category, action, location );
		return;
	}
	ga( 'send', 'event', category, action, location );
    return true;
}

function ga_CA( category, action ) {
	if( category == null || action == null ) {
		console.log( "ga_CA", category, action );
		return;
	}
	ga_CAL( category, action, getLocation().ga_value );
}

function ga_CAV( category, action, value ) {
	if( category == null || action == null || value == null ) {
        console.log( "ga_CAV", category, action );
        return;
    }
    ga_CALV( category, action, getLocation().ga_value, value );
}